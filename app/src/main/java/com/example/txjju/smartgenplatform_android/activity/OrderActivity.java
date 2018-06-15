package com.example.txjju.smartgenplatform_android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.OrderPageAdapter;

import com.example.txjju.smartgenplatform_android.fragment.AllFragment;
import com.example.txjju.smartgenplatform_android.fragment.CommentFragment;
import com.example.txjju.smartgenplatform_android.fragment.ReceivingFragment;
import com.example.txjju.smartgenplatform_android.fragment.SippingFragment;
import com.example.txjju.smartgenplatform_android.fragment.WaitFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private String[] titles = new String[]{"全部", "待付款", "待发货", "待收货", "待评价"};
    private TabLayout tabLayout;
    private ViewPager vp;
    private List<Fragment> fragments;
    private OrderPageAdapter orderPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initViews();
        initData();
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new AllFragment());
        fragments.add(new WaitFragment());
        fragments.add(new SippingFragment());
        fragments.add(new ReceivingFragment());
        fragments.add(new CommentFragment());
        orderPageAdapter = new OrderPageAdapter(getSupportFragmentManager(), fragments, Arrays.asList(titles));

        vp.setOffscreenPageLimit(orderPageAdapter.getCount());
        vp.setAdapter(orderPageAdapter);
        vp.setCurrentItem(0);
        tabLayout.setupWithViewPager(vp);
    }

    private void initViews() {
        ivBack=findViewById(R.id.iv_order_back);
        tabLayout=findViewById(R.id.tab_layout_order);
        vp=findViewById(R.id.vp_order);
        ivBack.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // popupWindow.dismiss();
        }
    };


    public static void open(Context context) {
        Intent intent = new Intent(context, OrderActivity.class);
        context.startActivity(intent);
    }

    public void currentTab(int tabId) {
        vp.setCurrentItem(tabId);
    }

    @Override
    public void onClick(View view) {
        this.finish();
    }
}
