package com.example.txjju.smartgenplatform_android.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.Banners;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.util.GlideImageLoader;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.view.MyRefreshLayout;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * 首页的Fragment
 */
public class HomeFragment extends BaseFragment {

   // private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private Banner banner;  // 广告栏控件
   // private GridView gv;    // 分类导航控件
   // private RecyclerView rvNews;    // 资讯列表控件
    //private ClassifyAdapter classifyAdapter;    // 分类导航列表适配器
   // private NewsAdapter newsAdapter;    // 资讯列表适配器
    //private List<News> newsList = new ArrayList<>();
    private List<String> bannerImgList = new ArrayList<>();
    private List<String> bannerTitleList = new ArrayList<>();
    //private MyRefreshLayout myRefreshLayout = new MyRefreshLayout(refreshLayout);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadData(); // 加载服务端数据
    }

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
//                        BasePojo<News> basePojo = JsonUtil.getBaseFromJson(getActivity(),
//                                json, new TypeToken<BasePojo<News>>(){}.getType());
//                        if(basePojo != null){
//                            pgDialog.dismiss();
//                            List<News> list = basePojo.getList();
//                            newsList.clear();
//                            newsList.addAll(list);
//                            newsAdapter.notifyDataSetChanged(); // 通知适配器更新列表
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
        initViews(view);// 获取新建视图View中布局文件的控件
       // initRefreshLayout();//设置刷新
        initBanner();//初始化轮播图
        //initRecyclerView();
        return view;
    }

//    private void initRefreshLayout() {
//        myRefreshLayout.initRefreshLayout();
//
//    }

//    private void initRecyclerView() {
//        // RecyclerView必须用LinearLayoutManager进行配置
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        // 设置列表的排列方向
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        // 配置LinearLayoutManager
//        rvNews.setLayoutManager(layoutManager);
//        // 添加分割线（重新绘制分割线）
//        rvNews.addItemDecoration(new RecycleViewDivider(getActivity(),
//                LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.bgLightGray)));
//        newsAdapter = new NewsAdapter(getActivity(), newsList);
//        rvNews.setAdapter(newsAdapter);
//    }

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
        //refreshLayout = view.findViewById(R.id.refresh_home);
        banner = view.findViewById(R.id.banner_home);
//        gv = view.findViewById(R.id.gv_home);
//        rvNews = view.findViewById(R.id.rc_news_home);
//        classifyAdapter = new ClassifyAdapter();
//        gv.setAdapter(classifyAdapter);
    }
}
