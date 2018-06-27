package com.example.txjju.smartgenplatform_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;


import com.example.txjju.smartgenplatform_android.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);   // 注册EventBus
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); // 注销EventBus
    }

    // 通过EventBus接收Activity的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
       // Toast.makeText(getActivity(), event.getMsg(),Toast.LENGTH_SHORT).show();
        if(event.getMsg().equals(getTag())){
            // 做此页面在连续点击导航按钮后需要做的事情，比如刷新数据
           // Toast.makeText(getActivity(), event.getMsg(),Toast.LENGTH_SHORT).show();
        }
    }

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);   // 注册EventBus
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); // 注销EventBus
    }

    // 通过EventBus接收Activity的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.i("msg","evevtbu进来了");
        Toast.makeText(getActivity(), event.getMsg(),Toast.LENGTH_SHORT).show();
    }*/

}
