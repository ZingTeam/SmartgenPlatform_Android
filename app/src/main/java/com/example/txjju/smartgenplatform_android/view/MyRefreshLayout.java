package com.example.txjju.smartgenplatform_android.view;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

/**
 * 下拉刷新 工具类
 */

public class MyRefreshLayout {
    private MaterialRefreshLayout refreshLayout;//下拉刷新控件

    public MyRefreshLayout(MaterialRefreshLayout refreshLayout){//构造函数
        this.refreshLayout = refreshLayout;
    }
    //下拉刷新的方法
    public void initRefreshLayout(){
        this.refreshLayout.setIsOverLay(false);//是否是侵入式下拉刷新
        this.refreshLayout.setWaveShow(true);//是否显示波浪
        //this.refreshLayout.setWaveColor(0xffffffff);//设置波浪颜色
        this.refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

            }
        });
    }
}
