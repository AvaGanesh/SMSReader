package com.ganesh.smsreader.Controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.ganesh.smsreader.Model.Sms;
import com.ganesh.smsreader.Model.Transaction;
import com.ganesh.smsreader.Model.TransactionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsHandler {

    private Context context;
    private static final int REQ_SMS_READ_PERMISSION = 201;

    public SmsHandler(Context context) {
        this.context = context;
    }

    public List<Sms> getSms(Long lowerbound, Long upperbound) {
        Log.d("sms: ", "Reached all sms" + new Date(lowerbound).toString()+" "+new Date(upperbound).toString());
        List<Sms> lstSms = new ArrayList<>();
        Sms objSms;

        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
            Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) { // must check the result to prevent exception
                    do {
                        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                        String address = cursor.getString(2);
                        Long date = cursor.getLong(4);
                        String folderName;
                        if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")) {
                            folderName = "inbox";
                        } else {
                            folderName = "sent";
                        }
                        String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                        if(body.matches("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)")) {
                            Log.d("sms: ","transaction message" + body);
                        }
                        if(body.matches("(?i)(?:\\sat\\s|in\\*)([A-Za-z0-9]*\\s?-?\\s?[A-Za-z0-9]*\\s?-?\\.?)")) {
                            Log.d("sms: "," transaction"+ body);
                        }
                        if(body.matches("\"(?=.*[Aa]ccount.*|.*[Aa]/[Cc].*|.*[Aa][Cc][Cc][Tt].*|.*[Cc][Aa][Rr][Dd].*)(?=.*[Cc]redit.*|.*[Dd]ebit.*)(?=.*[Ii][Nn][Rr].*|.*[Rr][Ss].*)\"")){
                            Log.d("sms: ", "acc transaction"+ body);
                        }
                        if(date > lowerbound && date < upperbound) {
                            objSms = new Sms(id,address,body,date,folderName);
                            lstSms.add(objSms);
                        }
                    } while (cursor.moveToNext());
                } else {
                    return lstSms;
                }
            }

        } else {
            Toast.makeText(context, "Please provide the permission for reading the SMS", Toast.LENGTH_LONG);
            return lstSms;
        }
        printSms(lstSms);
        return lstSms;
    }

    private void printSms(List<Sms> lstSms) {
        for(Sms sms: lstSms) {
            Log.d("sms:" ,sms.toString());
        }
    }

    public List<Transaction>  parsevalues(List<Sms> body_val) {
        Log.d("sms: ", "Reached sms parser");
        List<Transaction> transactionList = new ArrayList<>();
        ArrayList<Sms> resSms = new ArrayList<>();
        for (int i = 0; i < body_val.size(); i++) {
            Sms smsDto = body_val.get(i);
            Pattern regEx
                    = Pattern.compile("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)");
// Find instance of pattern matches
            Matcher m = regEx.matcher(smsDto.get_msg());
            if (m.find()) {
                try {
                    Log.e("amount_value= ", "" + m.group(0));
                    String amount = (m.group(0).replaceAll("inr", ""));
                    amount = amount.replaceAll("rs", "");
                    amount = amount.replaceAll("inr", "");
                    amount = amount.replaceAll(" ", "");
                    amount = amount.replaceAll(",", "");

                    if (smsDto.get_msg().contains("debited") ||
                            smsDto.get_msg().contains("purchasing") || smsDto.get_msg().contains("purchase") || smsDto.get_msg().contains("dr")) {
                        Log.d("sms: type", TransactionType.EXPENSE.toString() + " " + body_val.get(i).get_msg());
                        transactionList.add(new Transaction(body_val.get(i).get_id(),body_val.get(i).get_address(),TransactionType.EXPENSE, body_val.get(i).get_time(),amount));
                    } else if (smsDto.get_msg().contains("credited") || smsDto.get_msg().contains("cr")) {
                        Log.d("sms: type",TransactionType.INCOME.toString() + " " +body_val.get(i).get_msg());
                        transactionList.add(new Transaction(body_val.get(i).get_id(),body_val.get(i).get_address(),TransactionType.INCOME, body_val.get(i).get_time(),amount));
                    }
                    Log.e("matchedValue= ", "" + amount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("No_matchedValue ", "No_matchedValue ");
            }
        }
        printTransactions(transactionList);
        return transactionList;
    }

    private void printTransactions(List<Transaction> transactionList) {
        for(Transaction t: transactionList){
            Log.d("transaction:", t.toString());
        }
    }

    public long getSum(List<Transaction> transactionList) {
        long totalAmount = 0;
        try {
            for (Transaction t: transactionList) {
                if(t.getTransactionType().equals(TransactionType.INCOME)) {
                    Log.d("amount", "" + t.getAmount());
                    totalAmount += t.getAmount();
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return totalAmount;
    }

}
