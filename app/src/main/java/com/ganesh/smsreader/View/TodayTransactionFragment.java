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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodayTransactionFragment extends Fragment implements TransactionAdapter.onTransactionClickListener{
    private Calendar calendar;
    private RecyclerView recyclerView;
    private TextView textView;
    private SmsHandler smsHandler;
    private List<Transaction> transactionList;
    private List<Sms> allSmsList;
    private Button button;
    private TransactionAdapter transactionAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_transaction_layout, container, false);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long today = calendar.getTimeInMillis();
        textView = view.findViewById(R.id.sumTextViewToday);
        button = view.findViewById(R.id.chartBtnToday);

        smsHandler = new SmsHandler(getContext());
        Log.d("today", new Date(today).toString());
        allSmsList = smsHandler.getSms(today, System.currentTimeMillis());
        transactionList = smsHandler.parsevalues(allSmsList);

        recyclerView = view.findViewById(R.id.recyclerViewToday);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChartsActivity.class);
                intent.putExtra("TYPE",2);
                startActivity(intent);
            }
        });
        String totalAmount = "\u20B9"+ " "+smsHandler.getSum(transactionList);
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
}

