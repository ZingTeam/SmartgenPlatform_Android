package com.example.txjju.smartgenplatform_android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.txjju.smartgenplatform_android.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_selector, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_market_selector, "Market"))
                .addItem(new BottomNavigationItem(R.drawable.ic_store_selector, "Store"))
                .addItem(new BottomNavigationItem(R.drawable.ic_mine_selector, "Mine"))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });
    }
}
