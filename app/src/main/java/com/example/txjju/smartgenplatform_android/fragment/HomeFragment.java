package com.example.txjju.smartgenplatform_android.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.MainActivity;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.activity.ProjectDetailsActivity;
import com.example.txjju.smartgenplatform_android.adapter.HomeProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.HomeProjectAdapter;
import com.example.txjju.smartgenplatform_android.adapter.MarketProjectAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 首页的Fragment
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PROJECTDETAILS = 96;//请求码
    private static final int RESULT_PROJECTDETAILS = 1;//1代表成功，0代表失败
    private static final int REQUEST_PRODUCTDETAILS = 95;
    private static final int RESULT_PRODUCTDETAILS = 1;//1代表成功，0代表失败

    private TextView tvProductMoreView,tvProjectMoreView,tvSpecifysaleMoreView;
    private ImageView ivRecommendLeftHome,ivRecommendRightHome;

    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private Banner banner;  // 广告栏控件
    private RecyclerView rvProduct;    // 产品列表控件
    private RecyclerView rvProject;    // 项目列表控件
    private HomeProductAdapter productAdapter;    // 产品列表适配器
    private HomeProjectAdapter projectAdapter;    // 项目列表适配器

    private String creprojectResult,productResult;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();

    private List<Product> productList = new ArrayList<>();
    private List<Creativeproject> projectList = new ArrayList<>();
    private List<String> bannerImgList = new ArrayList<>();
    private List<String> bannerTitleList = new ArrayList<>();
    private MyRefreshLayout myRefreshLayout;
    private OnButtonClick onButtonClick;//2、定义接口成员变量

    // 定义接口变量的get方法
    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }
    //定义接口变量的set方法
    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }
    //1、定义接口
    public interface OnButtonClick{
        public void onClick(View view);
    }


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
            countDown();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 检测网络
        if (!checkNetwork(getActivity())) {
            Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        loadData(); // 加载服务端数据
    }
    /**
     * 处理后台来的数据
     */
    private void loadData() {
        initCreproject();
        initProduct();
    }

    /**
     * 加载创意产品数据
     */
    private void initProduct() {
        final ProgressDialog pgDialog = new ProgressDialog(getActivity());
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("queryParam.orderBy","productSell");//按项目点赞数来排序
        formBody.add("queryParam.orderByInTurn","DESC");//降序排列
        formBody.add("queryParam.pageSize","6");//取6个最火热的产品
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_GET)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MainActivity","首页产品：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"首页产品：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"首页产品：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    productResult = response.body().string();
                    Log.i(TAG,"首页产品：结果："+productResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"首页产品：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        getActivity(), productResult, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Product> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        Log.i(TAG,"首页产品：结果"+list.toString());
                                        pgDialog.dismiss();
                                        productList.clear();
                                        productList.addAll(list);
                                        productAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                                        refreshLayout.finishRefresh();  //停止刷新
                                    }else{
                                        pgDialog.dismiss();
                                        ToastUtils.Toast(getActivity(),basePojo.getMsg(),0);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 加载创意项目数据
     */
    private void initCreproject() {
        final ProgressDialog pgDialog = new ProgressDialog(getActivity());
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("queryParam.condition"," (Creproject_state=0 or Creproject_state=1)");//筛选条件
        formBody.add("queryParam.orderBy","creprojectPraise");//按项目点赞数来排序
        formBody.add("queryParam.orderByInTurn","DESC");//降序排列
        formBody.add("queryParam.pageSize","6");//取6个最火热的项目
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.CREPROJECT_GET)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MainActivity","首页项目：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"首页项目：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"首页项目：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    creprojectResult = response.body().string();
                    Log.i(TAG,"首页项目：结果："+creprojectResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"首页项目：测试");
                            BasePojo<Creativeproject> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        getActivity(), creprojectResult, new TypeToken<BasePojo<Creativeproject>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getTotal() > 0){// 信息获取成功,有数据
                                            List<Creativeproject> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                            Log.i(TAG,"首页项目：结果"+list.toString());
                                            pgDialog.dismiss();
                                            projectList.clear();
                                            projectList.addAll(list);
                                            projectAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                                            refreshLayout.finishRefresh();  //停止刷新
                                        }else{
                                            pgDialog.dismiss();
                                        }
                                    }else {
                                        pgDialog.dismiss();
                                        Log.i(TAG,"首页项目：后台传来失败了"+basePojo.getMsg());
                                        ToastUtils.Toast(getActivity(),basePojo.getMsg(),0);
                                    }
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Creativeproject> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        Log.i(TAG,"首页项目：结果"+list.toString());
                                        pgDialog.dismiss();
                                        projectList.clear();
                                        projectList.addAll(list);
                                        projectAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                                        refreshLayout.finishRefresh();  //停止刷新
                                    }else{
                                        ToastUtils.Toast(getActivity(),basePojo.getMsg(),0);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    // fragment中布局以及控件的获取都写在此方法中
    // onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //initData();//初始化假的数据，用于测试
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
        // 注册下拉刷新监听器
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            // 下拉刷新执行的方法
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                // 检测网络
                if (!checkNetwork(getActivity())) {
                    Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    refreshLayout.finishRefresh();
                    return;
                }
                loadData(); // 重新加载数据
            }
        });
    }

    /**
     * 检测网络
     * @return 返回网络检测结果
     */
    private boolean checkNetwork(Context context) {
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //判断网络是否连接
        if (manager.getActiveNetworkInfo()!=null){
            boolean flag=manager.getActiveNetworkInfo().isAvailable();
            if (flag){
                NetworkInfo.State state = manager.getActiveNetworkInfo().getState();
                if (state==NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

    private void initRecyclerView() {
        //适配产品的RecyclerView
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
                LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.bgLightGray)));

        //适配项目的RecyclerView
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
                LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.bgLightGray)));

        productAdapter = new HomeProductAdapter(getActivity(), productList);
        projectAdapter = new HomeProjectAdapter(getActivity(), projectList);
        rvProduct.setAdapter(productAdapter);
        rvProject.setAdapter(projectAdapter);
        //监听产品项目item
        productAdapter.setOnItemClickListener(new HomeProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG,"商城产品跳转");
                // 检测网络
                if (!checkNetwork(getActivity())) {
                    Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                int productId = productList.get(position).getId();
                Log.i(TAG,"项目id"+productId);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);//跳转到产品详情
                intent.putExtra("productId",Integer.toString(productId));
                //这里使用startActivityForResult进行跳转是为了方便有回传，回传里可以刷新列表
                startActivityForResult(intent,REQUEST_PRODUCTDETAILS);//REQUEST_PRODUCTDETAILS是请求码
            }
        });
        //监听首页项目item
        projectAdapter.setOnItemClickListener(new HomeProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG,"市场项目跳转");
                // 检测网络
                if (!checkNetwork(getActivity())) {
                    Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                int projectId = projectList.get(position).getId();
                Log.i(TAG,"项目id"+projectId);
                Intent intent = new Intent(getActivity(), ProjectDetailsActivity.class);//跳转到项目详情
                intent.putExtra("creProjectId",Integer.toString(projectId));
                //这里使用startActivityForResult进行跳转是为了方便有回传，回传里可以刷新列表
                startActivityForResult(intent,REQUEST_PROJECTDETAILS);//REQUEST_PROJECTDETAILS是请求码，为了直观，就没有定义常量
            }
        });
    }

    // 用户跳转到项目详情或产品详情后执行回调,进行刷新
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PROJECTDETAILS && resultCode == RESULT_PROJECTDETAILS){
            // 检测网络
            if (!checkNetwork(getActivity())) {
                Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            initCreproject(); // 重新加载数据
        }
        if(requestCode == REQUEST_PRODUCTDETAILS && resultCode == RESULT_PRODUCTDETAILS){
            // 检测网络
            if (!checkNetwork(getActivity())) {
                Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            initProduct(); // 重新加载数据
        }
    }

    private void initBanner() {
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner1.jpg");
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner2.jpg");
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner3.jpg");
        bannerImgList.add("http://p0vpex4u9.bkt.clouddn.com/base.jpg");
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
        rvProduct = view.findViewById(R.id.rc_product_home);
        rvProject = view.findViewById(R.id.rc_project_home);
        tvDay = view.findViewById(R.id.tv_day);
        tvHour = view.findViewById(R.id.tv_hour);
        tvMinute = view.findViewById(R.id.tv_minute);
        tvSecond = view.findViewById(R.id.tv_second);
        newPrice = view.findViewById(R.id.new_price);
        originalPrice = view.findViewById(R.id.original_price);
        tvProductMoreView = view.findViewById(R.id.tv_product_more_view);
        tvProjectMoreView = view.findViewById(R.id.tv_project_more_view);
        tvSpecifysaleMoreView = view.findViewById(R.id.tv_specifysale_more_view);
        ivRecommendLeftHome = view.findViewById(R.id.iv_recommend_left_home);
        ivRecommendRightHome = view.findViewById(R.id.iv_recommend_right_home);
        tvProductMoreView.setOnClickListener(this);
        tvProjectMoreView.setOnClickListener(this);
        tvSpecifysaleMoreView.setOnClickListener(this);
        ivRecommendLeftHome.setOnClickListener(this);
        ivRecommendRightHome.setOnClickListener(this);
    }

    /**
     * 监听控件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_product_more_view:
                // 检测网络
                if (!checkNetwork(getActivity())) {
                    Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                ((MainActivity)getActivity()).showFragment(2);//实现fragment之间的转换
                ((MainActivity)getActivity()).bottomNavigationBar.selectTab(2);//实现底部导航栏的切换
                break;
            case R.id.tv_project_more_view:
                // 检测网络
                if (!checkNetwork(getActivity())) {
                    Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                ((MainActivity)getActivity()).showFragment(1);//实现fragment之间的转换
                ((MainActivity)getActivity()).bottomNavigationBar.selectTab(1);//实现底部导航栏的切换
                break;
            case R.id.tv_specifysale_more_view:
                Log.i("MainActivity","特价");
                break;
            case R.id.iv_recommend_left_home:
                // 检测网络
                if (!checkNetwork(getActivity())) {
                    Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                Log.i("MainActivity","左边");
                break;
            case R.id.iv_recommend_right_home:
                // 检测网络
                if (!checkNetwork(getActivity())) {
                    Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                Log.i("MainActivity","右边");
                break;
        }
    }
    /*private void changeToAnotherFragment(){
        FragmentManager fm = getActivity().getSupportFragmentManager();// 这里要导android.support.v4.app.FragmentManager这个包
        FragmentTransaction ft = fm.beginTransaction();// import android.support.v4.app.FragmentTransaction;
        ft.replace(R.id.fl_container, new MarketFragment());// 这里的R.id为所附着Activity的XML文件中FrameLayout标签的id
        ft.commit(); // 最后不要忘记commit
    }*/

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


