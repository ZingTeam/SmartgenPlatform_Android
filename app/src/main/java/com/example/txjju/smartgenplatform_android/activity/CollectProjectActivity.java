package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.MineCollectProjectAdapter;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;
import com.example.txjju.smartgenplatform_android.util.FileUtils;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectProjectActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private Button btnGoHome;
    private LinearLayout llNull;

    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "CollectProjectActivity";

    private RecyclerView rvProject;    // 项目列表控件
    private MineCollectProjectAdapter projectAdapter;    // 项目列表适配器

    private List<Creativeproject> projectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_project);
        init();
       // initData();//初始化假的数据，用于测试
        //initRecyclerView();//初始化RecyclerView
    }

    private void init() {
        iv_back = findViewById(R.id.iv_collect_project_back);
        btnGoHome = findViewById(R.id.btn_collect_project_go_home);
        rvProject = findViewById(R.id.rc_collect_project_mine);
        llNull = findViewById(R.id.ll_null);
        llNull.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        btnGoHome.setOnClickListener(this);
    }

    private void initRecyclerView() {
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(CollectProjectActivity.this);
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvProject.setHasFixedSize(true);
        rvProject.setNestedScrollingEnabled(false);

        rvProject.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvProject.addItemDecoration(new RecycleViewDivider(CollectProjectActivity.this,
                LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.bgLightGray)));
//        if (entity != null && entity.topic != null && entity.topic.items != null && entity.topic.items.size() > 0) {
//            List<SpeedHourEntity.TopicBean.ItemsBean.ListBean> listBeen = entity.topic.items.get(0).list;
//            if (listBeen != null && listBeen.size() > 0)
//                speedHourAdapter.setList(listBeen);
//            rvProduct.setAdapter(speedHourAdapter);
//        }
        projectAdapter = new MineCollectProjectAdapter(CollectProjectActivity.this, projectList);
        rvProject.setAdapter(projectAdapter);
    }


    /**
     * 测试用的假数据
     */
    private void initData() {
        llNull.setVisibility(View.VISIBLE);
        /*
        Log.i(TAG,"SUSU");
        String data = FileUtils.readAssert(CollectProjectActivity.this,"creprojects.txt");
        Log.i(TAG,data);
        try {
            BasePojo<Creativeproject> basePojo = JsonUtil.getBaseFromJson(CollectProjectActivity.this,
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
        }*/
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_collect_product_back:
                    CollectProjectActivity.this.finish();
                    break;
                case R.id.btn_collect_project_go_home:
                    CollectProjectActivity.this.finish();
                    Intent intent = new Intent(CollectProjectActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
