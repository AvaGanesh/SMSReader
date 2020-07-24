package com.ganesh.smsreader.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.ganesh.smsreader.Controller.DatabaseHelper;
import com.ganesh.smsreader.Controller.SmsHandler;
import com.ganesh.smsreader.Model.Sms;
import com.ganesh.smsreader.Model.Transaction;
import com.ganesh.smsreader.R;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartsActivity extends AppCompatActivity {

    private SmsHandler smsHandler;
    private List<Sms> smsList;
    private List<Transaction> transactionList;
    private DatabaseHelper databaseHelper;
    private Map<String, Long> chartDataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        AnyChartView anyChartView = findViewById(R.id.pieChart);
        Pie pie = AnyChart.pie();

        Calendar calendar = Calendar.getInstance();
        long lowerBound = 0;
        databaseHelper = new DatabaseHelper(getApplicationContext());
        String title = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           int value = bundle.getInt("TYPE");
           switch (value) {
               case 0: // ALL
                   calendar.add(Calendar.YEAR, -1);
                   lowerBound = calendar.getTimeInMillis();
                   title = "ALL";
                   break;
               case 1:
                   title = "MONTH";
                   lowerBound = getTimeStampOfMonth(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1);
                   break;
               case 2:
                   title = "TODAY";
                   calendar.set(Calendar.HOUR_OF_DAY, 0);
                   calendar.set(Calendar.MINUTE, 0);
                   calendar.set(Calendar.SECOND, 0);
                   calendar.set(Calendar.MILLISECOND, 0);
                   lowerBound = calendar.getTimeInMillis();
                   break;
           }
        }


        chartDataMap = new HashMap<>();
        smsHandler = new SmsHandler(getApplicationContext());
        smsList = smsHandler.getSms(lowerBound, System.currentTimeMillis());
        transactionList = smsHandler.parsevalues(smsList);
        List<DataEntry> data = new ArrayList<>();

        for(Transaction transaction: transactionList) {
            String tag = databaseHelper.getTagById(Integer.parseInt(transaction.getId()));
            if(tag != null) {
                if(chartDataMap.containsKey(tag)) {
                    long totalAmount = chartDataMap.get(tag);
                    chartDataMap.replace(tag, totalAmount + transaction.getAmount());
                } else {
                    chartDataMap.put(tag,transaction.getAmount());
                }
            } else {
                if(chartDataMap.containsKey("None")) {
                    long totalAmount = chartDataMap.get("None");
                    chartDataMap.replace("None", totalAmount + transaction.getAmount());
                } else {
                    chartDataMap.put("None",transaction.getAmount());
                }
            }
        }

        for (Map.Entry<String,Long> entry : chartDataMap.entrySet()) {
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }


        pie.data(data);

        pie.title(title);

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("TAGS")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);



    }

    private static long getTimeStampOfMonth(int year, int month) {
        LocalDate march1985 = LocalDate.of(year, month, 1);
        return march1985.atStartOfDay(ZoneId.of("Asia/Kolkata"))
                .toInstant()
                .toEpochMilli();
    }
}
