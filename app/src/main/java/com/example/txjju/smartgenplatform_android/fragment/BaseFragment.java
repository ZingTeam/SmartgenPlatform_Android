package com.example.txjju.smartgenplatform_android.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;


import com.example.txjju.smartgenplatform_android.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);   // 注册EventBus
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); // 注销EventBus
    }

    // 通过EventBus接收Activity的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(getActivity(), event.getMsg(),Toast.LENGTH_SHORT).show();
    }

}
