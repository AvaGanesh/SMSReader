package com.ganesh.smsreader.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ganesh.smsreader.View.Adapters.TransactionAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ganesh.smsreader.Model.Sms;
import com.ganesh.smsreader.Model.Transaction;
import com.ganesh.smsreader.R;

import java.util.List;

public class AlertDialogController {

    private Context context;
    private List<Sms> allSmsList;
    private int position;
    private List<Transaction> transactionList;
    private LayoutInflater layoutInflater;
    private DatabaseHelper databaseHelper;
    private TransactionAdapter transactionAdapter;

    public AlertDialogController(Context context, List<Sms> allSmsList,
                                 int position, List<Transaction> transactionList,
                                 LayoutInflater layoutInflater,TransactionAdapter transactionAdapter) {
        this.context = context;
        this.allSmsList = allSmsList;
        this.position = position;
        this.transactionList = transactionList;
        this.layoutInflater = layoutInflater;
        this.transactionAdapter = transactionAdapter;
    }

    public void showAlertDialog() {
        databaseHelper = new DatabaseHelper(context);
        View dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null);
        TextView address = dialogView.findViewById(R.id.addressTextView);
        TextView body = dialogView.findViewById(R.id.bodyTextView);
//        Chip selectedChip = dialogView.findViewById(R.id.selectedChip);
        ChipGroup chipGroup = dialogView.findViewById(R.id.chipGroup);

        address.setText(transactionList.get(position).getAddress());
        String bodyText;

        for(Sms sms: allSmsList) {
            if(transactionList.get(position).getId().equals(sms.get_id())) {
                bodyText = sms.get_msg();
                body.setText(bodyText);
            }
        }


        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = chipGroup.findViewById(checkedId);
                if(chip!=null) {
                    if(transactionList.get(position).getTags()!=null) {
//                    updateTag()
                        try {
                            databaseHelper.updateTagById(Integer.parseInt(transactionList.get(position).getId()),chip.getText().toString());
                            Toast.makeText(context, "Tag updated successfully", Toast.LENGTH_LONG).show();
                            transactionList.get(position).setTags(chip.getText().toString());
                            transactionAdapter.notifyItemChanged(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error while updating the tag!!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if(databaseHelper.addDataToTransaction(Integer.parseInt(transactionList.get(position).getId()),chip.getText().toString())) {
                            Toast.makeText(context, "Tag added successfully!!",Toast.LENGTH_LONG).show();
                            transactionList.get(position).setTags(chip.getText().toString());
                            transactionAdapter.notifyItemChanged(position);
                        } else {
                            Toast.makeText(context, "Error while adding the tag!!",Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.CustomDialog);
        builder.setBackground(context.getDrawable(R.drawable.rounded_corner));
        builder.setView(dialogView);
        builder.show();
    }


}
