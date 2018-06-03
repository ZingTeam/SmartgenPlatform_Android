package com.example.txjju.smartgenplatform_android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.txjju.smartgenplatform_android.R;


/**
 * 首页的Fragment
 */
public class MarketFragment extends BaseFragment {

    //fragment中布局以及控件的获取都写在此方法中
    //onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        //获取新建视图View中布局文件的控件

        return view;
    }
}
