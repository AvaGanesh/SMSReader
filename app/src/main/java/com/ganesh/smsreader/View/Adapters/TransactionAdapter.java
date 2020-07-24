package com.ganesh.smsreader.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.ganesh.smsreader.Controller.DatabaseHelper;
import com.ganesh.smsreader.Model.Transaction;
import com.ganesh.smsreader.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private Context context;
    private onTransactionClickListener onTransactionClickListener;


    public TransactionAdapter(List<Transaction> transactionList,Context context, onTransactionClickListener onTransactionClickListener) {
        Collections.sort(transactionList, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return -o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });
        this.transactionList = transactionList;
        this.context = context;
        this.onTransactionClickListener = onTransactionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.custom_transaction_view, parent, false);


        // Return a new holder instance
        return new ViewHolder(contactView,onTransactionClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        // Set item views based on your views and data model
        TextView date = holder.dateTextView;
        TextView transactionType = holder.transactionTypeView;
        TextView transactionAmount = holder.transactionAmountView;
        RelativeLayout relativeLayout = holder.parentLayout;
        Chip chip = holder.chip;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        String tag = databaseHelper.getTagById(Integer.parseInt(transactionList.get(position).getId()));

        if(tag!= null) {
            chip.setText(tag);
        } else {
            chip.setText("None");
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTransactionClickListener.transactionCardListener(position);
                notifyDataSetChanged();
            }
        });

        Timestamp stamp = new Timestamp(transaction.getTimestamp());
        String pattern = "dd-MM-yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date stmapDate = new Date(stamp.getTime());

        date.setText(simpleDateFormat.format(stmapDate));

        if(transaction.getTransactionType().toString().equals("INCOME")) {
            transactionType.setTextColor(ContextCompat.getColor(context,
                    R.color.green));
        } else {
            transactionType.setTextColor(ContextCompat.getColor(context,
                    R.color.red));
        }


        transactionType.setText(transaction.getTransactionType().toString());
        String amount = "\u20B9"+ transaction.getAmount();
        transactionAmount.setText(amount);


    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         TextView dateTextView;
         TextView transactionTypeView;
         TextView transactionAmountView;
         RelativeLayout parentLayout;
         onTransactionClickListener onTransactionClickListener;
         Chip chip;

        public ViewHolder(View itemView,onTransactionClickListener onTransactionClickListener) {

            super(itemView);
            this.onTransactionClickListener = onTransactionClickListener;
            dateTextView = itemView.findViewById(R.id.date);
            transactionTypeView = itemView.findViewById(R.id.transactionType);
            transactionAmountView = itemView.findViewById(R.id.transactionAmount);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            chip = itemView.findViewById(R.id.chipView);
        }

        @Override
        public void onClick(View v) {
            onTransactionClickListener.transactionCardListener(getAdapterPosition());
        }
    }

    public interface onTransactionClickListener {
        void transactionCardListener(int position);
    }


}
