package com.example.txjju.smartgenplatform_android.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.HomeProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.HomeProjectAdapter;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.util.FileUtils;
import com.example.txjju.smartgenplatform_android.util.GlideImageLoader;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.example.txjju.smartgenplatform_android.util.UIUtils;
import com.example.txjju.smartgenplatform_android.view.MyRefreshLayout;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 首页的Fragment
 */
public class HomeFragment extends BaseFragment {

    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "MainActivity";

    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private Banner banner;  // 广告栏控件
    private RecyclerView rvProduct;    // 产品列表控件
    private RecyclerView rvProject;    // 项目列表控件
    private HomeProductAdapter productAdapter;    // 产品列表适配器
    private HomeProjectAdapter projectAdapter;    // 项目列表适配器

    private List<Product> productList = new ArrayList<>();
    private List<Creativeproject> projectList = new ArrayList<>();
    private List<String> bannerImgList = new ArrayList<>();
    private List<String> bannerTitleList = new ArrayList<>();
    private MyRefreshLayout myRefreshLayout;

//计时器相关变量
    private TextView tvDay,tvHour,tvMinute,tvSecond,newPrice,originalPrice;
    private long mDay;
    private long mHour;
    private long mMinute;
    private long mSecond;
    private long  abortTime = (12*24*3600+10 * 3600 + 8 * 60 + 53) * 1000;//截止时间
    private Timer timer = null;
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Log.i(TAG,"huahua");
            countDown();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadData(); // 加载服务端数据
    }

    /**
     * 处理后台来的数据
     */
    private void loadData() {
        bannerImgList.add("");
//        final ProgressDialog pgDialog = new ProgressDialog(getActivity());
//        pgDialog.setMessage("记载中，请稍后...");
//        pgDialog.show();
//        // 加载广告数据
//        new AsyncHttpClient().get(Constant.BANNER_GET_URL, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                try {
//                    if (json != null) {
//                        BasePojo<Banners> basePojo = JsonUtil.getBaseFromJson(getActivity(),
//                                json, new TypeToken<BasePojo<Banners>>(){}.getType());
//                        if(basePojo != null){
//                            pgDialog.dismiss();
//                            bannerImgList.clear();
//                            bannerTitleList.clear();
//                            // 依次取出广告图片地址以及广告标题，存入集合中
//                            for (int i=0; i<basePojo.getList().size(); i++){
//                                bannerImgList.add(basePojo.getList().get(i).getUrl());
//                                bannerTitleList.add(basePojo.getList().get(i).getTitle());
//                            }
//                            initBanner();
//                            refreshLayout.finishRefresh();  //停止刷新
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });

//        new AsyncHttpClient().get(Constant.NEW_GET_URL, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                try {
//                    if (json != null) {
//                        BasePojo<Creativeproject> basePojo = JsonUtil.getBaseFromJson(getActivity(),
//                                json, new TypeToken<BasePojo<Creativeproject>>(){}.getType());
//                        if(basePojo != null){
//                            pgDialog.dismiss();
//                            List<Creativeproject> list = basePojo.getList();
//                            CreativeprojectList.clear();
//                            CreativeprojectList.addAll(list);
//                            CreativeprojectAdapter.notifyDataSetChanged(); // 通知适配器更新列表
//                            refreshLayout.finishRefresh();  //停止刷新
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });
    }

    // fragment中布局以及控件的获取都写在此方法中
    // onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initData();//初始化假的数据，用于测试
        initViews(view);// 获取新建视图View中布局文件的控件
        initRefreshLayout();//设置刷新
        initBanner();//初始化轮播图
        initRecyclerView();//初始化RecyclerView
        //PriceUtils.convertPriceSize(getActivity(),newPrice,"￥50", UIUtils.dp2px(getActivity(),16));//设置价格字体大小
        UIUtils.setTextFlag(originalPrice,true);
        setTime(abortTime);
        start();
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
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvProduct.setHasFixedSize(true);
        rvProduct.setNestedScrollingEnabled(false);

        // 配置LinearLayoutManager
        rvProduct.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvProduct.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.bgLightGray)));

        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManagers = new LinearLayoutManager(getActivity());
        // 设置列表的排列方向
        layoutManagers.setOrientation(LinearLayoutManager.HORIZONTAL);


        layoutManagers.setSmoothScrollbarEnabled(true);
        layoutManagers.setAutoMeasureEnabled(true);

        rvProject.setHasFixedSize(true);
        rvProject.setNestedScrollingEnabled(false);

        rvProject.setLayoutManager(layoutManagers);
        // 添加分割线（重新绘制分割线）
        rvProject.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.bgLightGray)));
//        if (entity != null && entity.topic != null && entity.topic.items != null && entity.topic.items.size() > 0) {
//            List<SpeedHourEntity.TopicBean.ItemsBean.ListBean> listBeen = entity.topic.items.get(0).list;
//            if (listBeen != null && listBeen.size() > 0)
//                speedHourAdapter.setList(listBeen);
//            rvProduct.setAdapter(speedHourAdapter);
//        }
        productAdapter = new HomeProductAdapter(getActivity(), productList);
        projectAdapter = new HomeProjectAdapter(getActivity(), projectList);
        rvProduct.setAdapter(productAdapter);
        rvProject.setAdapter(projectAdapter);
    }

    private void initBanner() {
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner1.jpg");
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner2.jpg");
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner3.jpg");
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner4.jpg");
//        bannerTitleList.add("倾向于没有开始，直到你有一杯咖啡");
//        bannerTitleList.add("创意的价值无法用金钱衡量");
//        bannerTitleList.add("我们帮你完成成功路上的第一步");
//        bannerTitleList.add("不做咸鱼，年轻出去看看");
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(bannerImgList);
       // banner.setBannerTitles(bannerTitleList);
        banner.start();
    }

    private void initViews(View view) {
        refreshLayout = view.findViewById(R.id.refresh_home);
        banner = view.findViewById(R.id.banner_home);
//        gv = view.findViewById(R.id.gv_home);
        rvProduct = view.findViewById(R.id.rc_product_home);
        rvProject = view.findViewById(R.id.rc_project_home);
//        classifyAdapter = new ClassifyAdapter();
//        gv.setAdapter(classifyAdapter);
        tvDay = view.findViewById(R.id.tv_day);
        tvHour = view.findViewById(R.id.tv_hour);
        tvMinute = view.findViewById(R.id.tv_minute);
        tvSecond = view.findViewById(R.id.tv_second);
        newPrice = view.findViewById(R.id.new_price);
        originalPrice = view.findViewById(R.id.original_price);
    }

    /**
     * 测试用的假数据
     */
    private void initData() {
        String creprojectData = FileUtils.readAssert(getActivity(),"creprojects.txt");
        String productData = FileUtils.readAssert(getActivity(),"products.txt");
        try {
            BasePojo<Creativeproject> basePojo1 = JsonUtil.getBaseFromJson(getActivity(),
                    creprojectData, new TypeToken<BasePojo<Creativeproject>>(){}.getType());

            BasePojo<Product> basePojo2 = JsonUtil.getBaseFromJson(getActivity(),
                    productData, new TypeToken<BasePojo<Product>>(){}.getType());

            List<Creativeproject> list1 = basePojo1.getDatas();

            List<Product> list2 = basePojo2.getDatas();

            projectList.addAll(list1);
            productList.addAll(list2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计时器
     */
    public void setTime(long leftTime) {
        Log.i(TAG,"sususususu");
        long time = leftTime / 1000;
        long day = time/(3600*24);
        long hours = (time-day*3600*24)/ 3600;
        long minutes = (time - day*3600*24-hours * 3600) / 60;
        long seconds = time - day*3600*24-hours * 3600 - minutes * 60;
        setTime(day, hours, minutes, seconds);
    }

    public void setTime(long day, long hour, long min, long sec) {

        if (hour >= 60 || min >= 60 || sec >= 60 || hour < 0 || min < 0
                || sec < 0) {
            throw new RuntimeException("Time format is error");
        }
        mDay = day;
        mHour = hour;
        mMinute = min;
        mSecond = sec;

        setTextTime();
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //保证天，时，分，秒都两位显示，不足的补0
    private void setTextTime() {
        if (mDay<10){
            tvDay.setText("0"+mDay);
        }else {
            tvDay.setText(mDay+"");
        }

        if (mHour<10){
            tvHour.setText("0"+mHour);
        }else {
            tvHour.setText(mHour + "");
        }

        if (mMinute<10){
            tvMinute.setText("0"+mMinute );
        }else {
            tvMinute.setText(mMinute + "");
        }

        if (mSecond<10){
            tvSecond.setText("0"+mSecond );
        }else {
            tvSecond.setText(mSecond + "");
        }
    }

    private void countDown() {
        if (isCarry4Unit(tvSecond)) {
            if (isCarry4Unit(tvMinute)) {
                if (isCarry4Unit(tvHour)) {
                    if (isCarry4Unit(tvDay)) {
                        ToastUtils.showToast("倒计时结束了");
                        stop();
                    }
                }
            }
        }
    }

    private boolean isCarry4Unit(TextView tv) {
        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 59;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }
    }
}


