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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AllTransactionFragment extends Fragment implements TransactionAdapter.onTransactionClickListener{
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
        View view = inflater.inflate(R.layout.all_transaction_layout, container, false);

        smsHandler = new SmsHandler(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.sumTextView);
        button = view.findViewById(R.id.chartBtn);







        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        long oneYearAgo = calendar.getTimeInMillis();
        String pattern = "dd-MM-yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


        allSmsList =smsHandler.getSms(oneYearAgo, System.currentTimeMillis());
        transactionList = smsHandler.parsevalues(allSmsList);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        transactionAdapter = new TransactionAdapter(transactionList,getContext(),this);
        recyclerView.setAdapter(transactionAdapter);
        Log.d("sumAll",String.valueOf(smsHandler.getSum(transactionList)));
        String totalAmount = "\u20B9"+ " " + smsHandler.getSum(transactionList);
        textView.setText(totalAmount);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChartsActivity.class);
                intent.putExtra("TYPE", 0);
                startActivity(intent);

            }
        });

        return view;

    }

    @Override
    public void transactionCardListener(int position) {
        LayoutInflater inflater = getLayoutInflater();
        new AlertDialogController(getContext(),allSmsList,position,transactionList,inflater, transactionAdapter).showAlertDialog();
    }
}
