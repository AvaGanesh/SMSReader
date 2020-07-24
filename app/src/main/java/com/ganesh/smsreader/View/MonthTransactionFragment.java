package com.ganesh.smsreader.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesh.smsreader.Controller.AlertDialogController;
import com.ganesh.smsreader.Model.Sms;
import com.ganesh.smsreader.Model.Transaction;
import com.ganesh.smsreader.R;
import com.ganesh.smsreader.Controller.SmsHandler;
import com.ganesh.smsreader.View.Adapters.TransactionAdapter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

public class MonthTransactionFragment extends Fragment implements TransactionAdapter.onTransactionClickListener{
    private Calendar calendar;
    private RecyclerView recyclerView;
    private TextView textView;
    private SmsHandler smsHandler;
    private List<Sms> allSmsList;
    private List<Transaction> transactionList;
    private Button button;
    private TransactionAdapter transactionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_transaction_view, container, false);
        calendar = Calendar.getInstance();
        long oneMonthAgo = getTimeStampOfMonth(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1);
        button = view.findViewById(R.id.chartBtnMonth);

        smsHandler = new SmsHandler(getContext());
        allSmsList = smsHandler.getSms(oneMonthAgo, System.currentTimeMillis());
        transactionList = smsHandler.parsevalues(allSmsList);
        recyclerView = view.findViewById(R.id.recyclerViewMonth);
        textView = view.findViewById(R.id.sumTextViewMonth);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChartsActivity.class);
                intent.putExtra("TYPE",1);
                startActivity(intent);
            }
        });

        String totalAmount = "\u20B9"+ " "+ smsHandler.getSum(transactionList);

        textView.setText(totalAmount);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        transactionAdapter = new TransactionAdapter(transactionList,getContext(),this);
        recyclerView.setAdapter(transactionAdapter);
        return view;
    }

    @Override
    public void transactionCardListener(int position) {
        LayoutInflater inflater = getLayoutInflater();
        new AlertDialogController(getContext(),allSmsList,position,transactionList,inflater,transactionAdapter).showAlertDialog();
    }

    private static long getTimeStampOfMonth(int year, int month) {
        Log.d("local date",year+" "+month);
        LocalDate march1985 = LocalDate.of(year, month, 1);
        return march1985.atStartOfDay(ZoneId.of("Asia/Kolkata"))
                .toInstant()
                .toEpochMilli();
    }
}
