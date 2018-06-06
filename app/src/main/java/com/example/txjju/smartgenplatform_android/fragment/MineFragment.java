package com.example.txjju.smartgenplatform_android.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.AboutMeActivity;
import com.example.txjju.smartgenplatform_android.adapter.CreativeProjectAdapter;
import com.example.txjju.smartgenplatform_android.pojo.CreativeProject;
import com.example.txjju.smartgenplatform_android.view.CircleImageView;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;


/**
 * 首页的Fragment
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private RadioGroup rgPublic,rgCollect;
    private TextView tvName,tvPublic,tvCollect,tvPublicLine,tvCollectLine,tvEdit;
    private CircleImageView imgUser;
    private RadioButton rbOrder,rbComment,rbCart,rbhistory;
    private RecyclerView rvCreativeProject;
    private List<CreativeProject> creativeProjectlist = new ArrayList<>();
    private CreativeProjectAdapter creativeProjectAdapter;

    //fragment中布局以及控件的获取都写在此方法中
    //onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        //获取新建视图View中布局文件的控件
        initViews(view);
        initRecyclerView();
        return view;
    }

    private void initViews(View view) {
        rvCreativeProject=view.findViewById(R.id.rv_creative_project_mine);
        imgUser=view.findViewById(R.id.img_mine_user);
        tvEdit=view.findViewById(R.id.tv_mine_edit);
        imgUser.setOnClickListener(this);
        tvEdit.setOnClickListener(this);

    }

    private void initRecyclerView() {
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 配置LinearLayoutManager
        rvCreativeProject.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvCreativeProject.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.bgLightGray)));
        creativeProjectAdapter = new CreativeProjectAdapter(getActivity(), creativeProjectlist);
        rvCreativeProject.setAdapter(creativeProjectAdapter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.img_mine_user:

            case R.id.tv_mine_edit:
                Intent intent = new Intent();
                //AboutMeActivity.class为想要跳转的Activity
                intent.setClass(getActivity(), AboutMeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
