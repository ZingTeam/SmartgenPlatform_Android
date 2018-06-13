package com.example.txjju.smartgenplatform_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.MineCollectProjectAdapter;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.util.FileUtils;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏项目的fragment
 */
public class CollectCreProjectFragment extends Fragment {

    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "MainActivity";

    private RecyclerView rvProject;    // 项目列表控件
    private MineCollectProjectAdapter projectAdapter;    // 项目列表适配器

    private List<Creativeproject> projectList = new ArrayList<>();

    // fragment中布局以及控件的获取都写在此方法中
    // onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_cre_project, container, false);
        //获取新建视图View中布局文件的控件
        initData();//初始化假的数据，用于测试
        initViews(view);// 获取新建视图View中布局文件的控件
        initRecyclerView();//初始化RecyclerView

        return view;
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
        projectAdapter = new MineCollectProjectAdapter(getActivity(), projectList);
        rvProject.setAdapter(projectAdapter);
    }

    private void initViews(View view) {
        rvProject = view.findViewById(R.id.rc_collect_project_mine);
    }

    /**
     * 测试用的假数据
     */
    private void initData() {
        Log.i(TAG,"SUSU");
        String data = FileUtils.readAssert(getActivity(),"creprojects.txt");
        Log.i(TAG,data);
        try {
            BasePojo<Creativeproject> basePojo = JsonUtil.getBaseFromJson(getActivity(),
                    data, new TypeToken<BasePojo<Creativeproject>>(){}.getType());
            Log.i(TAG,"123");
            Log.i(TAG,basePojo.toString());
            Log.i(TAG,"456");
            List<Creativeproject> list = basePojo.getDatas();
            Log.i(TAG,"789");
            Log.i(TAG,"error"+list.size());
            projectList.addAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
