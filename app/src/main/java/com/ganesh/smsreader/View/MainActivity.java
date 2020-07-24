package com.ganesh.smsreader.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.ganesh.smsreader.R;
import com.ganesh.smsreader.View.Adapters.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int REQ_SMS_READ_PERMISSION = 201;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllTransactionFragment(), "All");
        adapter.addFragment(new TodayTransactionFragment(), "Today");
        adapter.addFragment(new MonthTransactionFragment(), "Month");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission","Granted");
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{Manifest.permission.READ_SMS}, REQ_SMS_READ_PERMISSION);
                }
            }
        }).start();

//        TabLayout.Tab tab1 = tabs.newTab();
//
//        tab1.setCustomView(allRecyclerView);
//        tab1.setText("All");
//
//
//
//        TabLayout.Tab tab2 = tabs.newTab();
//
//        tab2.setCustomView(todayRecyclerView);
//        tab2.setText("Today");
//
//        tabs.addTab(tab1);
//        tabs.addTab(tab2);



    }



}
