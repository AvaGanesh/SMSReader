package com.ganesh.smsreader.View.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ganesh.smsreader.Model.Transaction;

import java.util.List;

class MyOnClickListener implements View.OnClickListener {

    private Context context;
    private RecyclerView recyclerView;
    private List<Transaction> transactionList;

    public MyOnClickListener(Context context, RecyclerView recyclerView, List<Transaction> transactionList) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.transactionList = transactionList;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = recyclerView.getChildLayoutPosition(v);
        Transaction item = transactionList.get(itemPosition);
        Toast.makeText(context, item.toString(), Toast.LENGTH_LONG).show();
    }
}
