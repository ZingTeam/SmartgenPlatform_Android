package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.fragment.AllFragment;
import com.example.txjju.smartgenplatform_android.fragment.CommentFragment;
import com.example.txjju.smartgenplatform_android.fragment.ReceivingFragment;
import com.example.txjju.smartgenplatform_android.fragment.SippingFragment;
import com.example.txjju.smartgenplatform_android.fragment.WaitFragment;
import com.example.txjju.smartgenplatform_android.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvAll;
    private TextView mTvWait;
    private TextView tvAll,tvWait,tvComment,tvSipping,tvReceiving;
    private View v1,v2,v3,v4,v5;
    private LinearLayout mLl;
    private FrameLayout fl;
    private ImageView ivBack;
    private int currPosition = 0;
    private List<Fragment> list = new ArrayList<>();
    private String[] tags = new String[]{"AllFragment","WaitFragment",
            "SippingFragment","ReceivingFragment","CommentFragment",};
    private android.support.v4.app.FragmentManager fm;//fragment管理器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        initFragment();
    }

    private void initFragment() {
        list = new ArrayList<>();
        list.add(new AllFragment());
        list.add(new WaitFragment());
        list.add(new SippingFragment());
        list.add(new ReceivingFragment());
        list.add(new CommentFragment());
        //调用管理器,管理Fragment，一个事物管理器管理一个Fragment
        fm = getSupportFragmentManager();//实例化Fragment对象
        FragmentTransaction ft = fm.beginTransaction();//实例化Fragment事务管理器
        for(int i = 0; i < list.size();i++){//将四个Fragment装在容器中，交给ft管理
            ft.add(R.id.fl_container,list.get(i),tags[i]);
        }
        ft.commit();//提交保存
        showFragment(0);//默认显示首页Fragment
    }

    private void showFragment(int i) {
        FragmentTransaction ft = fm.beginTransaction();//实例化Fragment事务管理器
        hiddenFragment();//先隐藏所有Fragment
        ft.show(list.get(i));//显示Fragment
        ft.commit();
    }

    private void hiddenFragment() {
        FragmentTransaction ft = fm.beginTransaction();//实例化Fragment事务管理器
        for(Fragment fg:list){
            ft.hide(fg);
        }
        ft.commit();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_order_back);
        mTvAll = findViewById(R.id.tvAll);
        mTvWait = findViewById(R.id.tvWait);
        mLl = findViewById(R.id.ll);
        fl =  findViewById(R.id.fl_container);
        tvAll=findViewById(R.id.tvAll);
        tvWait=findViewById(R.id.tvWait);
        tvComment=findViewById(R.id.tvComment);
        tvSipping=findViewById(R.id.tvSipping);
        tvReceiving=findViewById(R.id.tvReceiving);
        v1=findViewById(R.id.v1);
        v2=findViewById(R.id.v2);
        v3=findViewById(R.id.v3);
        v4=findViewById(R.id.v4);
        v5=findViewById(R.id.v5);
        ivBack.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        tvWait.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        tvSipping.setOnClickListener(this);
        tvReceiving.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_order_back:
                this.finish();
                break;
            case R.id.tvAll:
                setTvAll();
                showFragment(0);
                clickAgain(0);
                break;
            case R.id.tvWait:
                setTvWait();
                showFragment(1);
                clickAgain(1);
                break;
            case R.id.tvSipping:
                setTvSipping();
                showFragment(2);
                clickAgain(2);
                break;
            case R.id.tvReceiving:
                setTvReceiving();
                showFragment(3);
                clickAgain(3);
                break;
            case R.id.tvComment:
                setTvPaid();
                showFragment(4);
                clickAgain(4);
                break;
        }
    }

    private void clickAgain(int i) {
        if(currPosition == i){      // 当前显示为某页面，并再次点击了此页面的导航按钮
            EventBus.getDefault().post(new MessageEvent(tags[i]));  // 通知Fragment刷新
        }
        currPosition = i;
    }
    //设置字和线颜色变换
    private void setTvAll(){
        v1.setBackgroundColor(getResources().getColor(R.color.blue));
        v2.setBackgroundColor(getResources().getColor(R.color.white));
        v3.setBackgroundColor(getResources().getColor(R.color.white));
        v4.setBackgroundColor(getResources().getColor(R.color.white));
        v5.setBackgroundColor(getResources().getColor(R.color.white));
        tvAll.setEnabled(false);
        tvWait.setEnabled(true);
        tvComment.setEnabled(true);
        tvSipping.setEnabled(true);
        tvReceiving.setEnabled(true);
    }
    private void setTvWait(){
        v1.setBackgroundColor(getResources().getColor(R.color.white));
        v2.setBackgroundColor(getResources().getColor(R.color.blue));
        v3.setBackgroundColor(getResources().getColor(R.color.white));
        v4.setBackgroundColor(getResources().getColor(R.color.white));
        v5.setBackgroundColor(getResources().getColor(R.color.white));
        tvAll.setEnabled(true);
        tvWait.setEnabled(false);
        tvComment.setEnabled(true);
        tvSipping.setEnabled(true);
        tvReceiving.setEnabled(true);
    }
    private void setTvPaid(){
        v1.setBackgroundColor(getResources().getColor(R.color.white));
        v2.setBackgroundColor(getResources().getColor(R.color.white));
        v3.setBackgroundColor(getResources().getColor(R.color.blue));
        v4.setBackgroundColor(getResources().getColor(R.color.white));
        v5.setBackgroundColor(getResources().getColor(R.color.white));
        tvAll.setEnabled(true);
        tvWait.setEnabled(true);
        tvComment.setEnabled(false);
        tvSipping.setEnabled(true);
        tvReceiving.setEnabled(true);
    }
    private void setTvSipping(){
        v1.setBackgroundColor(getResources().getColor(R.color.white));
        v2.setBackgroundColor(getResources().getColor(R.color.white));
        v3.setBackgroundColor(getResources().getColor(R.color.white));
        v4.setBackgroundColor(getResources().getColor(R.color.blue));
        v5.setBackgroundColor(getResources().getColor(R.color.white));
        tvAll.setEnabled(true);
        tvWait.setEnabled(true);
        tvComment.setEnabled(true);
        tvSipping.setEnabled(false);
        tvReceiving.setEnabled(true);
    }
    private void setTvReceiving(){
        v1.setBackgroundColor(getResources().getColor(R.color.white));
        v2.setBackgroundColor(getResources().getColor(R.color.white));
        v3.setBackgroundColor(getResources().getColor(R.color.white));
        v4.setBackgroundColor(getResources().getColor(R.color.white));
        v5.setBackgroundColor(getResources().getColor(R.color.blue));
        tvAll.setEnabled(true);
        tvWait.setEnabled(true);
        tvComment.setEnabled(true);
        tvSipping.setEnabled(true);
        tvReceiving.setEnabled(false);
    }
}
