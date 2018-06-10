package com.example.txjju.smartgenplatform_android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.HomeProjectAdapter;
import com.example.txjju.smartgenplatform_android.adapter.MarketProjectAdapter;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.CreativeProject;
import com.example.txjju.smartgenplatform_android.pojo.News;
import com.example.txjju.smartgenplatform_android.util.FileUtils;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.view.MyRefreshLayout;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 首页的Fragment
 */
public class MarketFragment extends BaseFragment {

    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "MainActivity";

    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private RecyclerView rvProject;    // 项目列表控件
    private MarketProjectAdapter projectAdapter;    // 项目列表适配器

    private List<CreativeProject> projectList = new ArrayList<>();
    private MyRefreshLayout myRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadData(); // 加载服务端数据
    }

    //fragment中布局以及控件的获取都写在此方法中
    //onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        //获取新建视图View中布局文件的控件
        initData();//初始化假的数据，用于测试
        initViews(view);// 获取新建视图View中布局文件的控件
        initRefreshLayout();//设置刷新
        initRecyclerView();//初始化RecyclerView
        //PriceUtils.convertPriceSize(getActivity(),newPrice,"￥50", UIUtils.dp2px(getActivity(),16));//设置价格字体大小
        return view;
    }


    private void initRefreshLayout() {
        myRefreshLayout = new MyRefreshLayout(refreshLayout);
        myRefreshLayout.initRefreshLayout();
    }

    private void initRecyclerView() {
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvProject.setHasFixedSize(true);
        rvProject.setNestedScrollingEnabled(false);

        rvProject.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvProject.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.bgLightGray)));
//        if (entity != null && entity.topic != null && entity.topic.items != null && entity.topic.items.size() > 0) {
//            List<SpeedHourEntity.TopicBean.ItemsBean.ListBean> listBeen = entity.topic.items.get(0).list;
//            if (listBeen != null && listBeen.size() > 0)
//                speedHourAdapter.setList(listBeen);
//            rvProduct.setAdapter(speedHourAdapter);
//        }
        projectAdapter = new MarketProjectAdapter(getActivity(), projectList);
        rvProject.setAdapter(projectAdapter);
    }

    private void initViews(View view) {
        refreshLayout = view.findViewById(R.id.refresh_market);
        rvProject = view.findViewById(R.id.rc_project_market);
    }

    /**
     * 测试用的假数据
     */
    private void initData() {
        Log.i(TAG,"SUSU");
        String data = FileUtils.readAssert(getActivity(),"creprojects.txt");
        Log.i(TAG,data);
        try {
            BasePojo<CreativeProject> basePojo = JsonUtil.getBaseFromJson(getActivity(),
                    data, new TypeToken<BasePojo<CreativeProject>>(){}.getType());
            Log.i(TAG,"123");
            Log.i(TAG,basePojo.toString());
            Log.i(TAG,"456");
            List<CreativeProject> list = basePojo.getList();
            Log.i(TAG,"789");
            Log.i(TAG,"error"+list.size());
            projectList.addAll(list);
            Log.i(TAG,projectList.get(0).getCreproject_title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
