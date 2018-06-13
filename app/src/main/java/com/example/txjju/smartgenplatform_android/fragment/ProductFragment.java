package com.example.txjju.smartgenplatform_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.util.GlideImageLoader;
import com.example.txjju.smartgenplatform_android.util.OnScrollViewChangeListener;
import com.example.txjju.smartgenplatform_android.view.CountDownView;
import com.example.txjju.smartgenplatform_android.view.ScrollViewWrapper;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情的商品介绍页
 */
public class ProductFragment extends BaseFragment {
    private Banner banner;  // 广告栏控件
    private ScrollViewWrapper scrollViewWrapper;
    private CountDownView countdownView;

    private List<String> bannerImgList = new ArrayList<>();
//    private List<String> bannerTitleList = new ArrayList<>();
    public final static String TAG = "ProductDetailsActivity";
    private long testTime = (2 * 24 * 3600 + 10 * 3600 + 18 * 60 + 53) * 1000;


    private OnScrollTabChangeListener listener=null;

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
        View view = inflater.inflate(R.layout.fragment_product_layout, container, false);
        //获取新建视图View中布局文件的控件
        initViews(view);// 获取新建视图View中布局文件的控件
        initBanner();
        initScrooll();
        initData();
        return view;
    }

    private void initData() {
        countdownView.setTime(testTime);
        countdownView.start();
    }

    private void initViews(View view) {
        banner = view.findViewById(R.id.detail_banner);
        scrollViewWrapper = view.findViewById(R.id.scrollViewWrapper);
        countdownView = view.findViewById(R.id.countdown_view);
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

    private void initScrooll() {
        listener=(OnScrollTabChangeListener)getActivity();
        scrollViewWrapper.setOnScrollChangeListener(new OnScrollViewChangeListener() {
            @Override
            public void change(int tabId) {
                if (listener!=null){
                    listener.currentTab(tabId);
                }
            }
        });
    }


    //滚动的时候TabLayout的id切换
    public interface OnScrollTabChangeListener{
        void currentTab(int tabId);
    }
}
