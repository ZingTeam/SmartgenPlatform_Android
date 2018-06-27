package com.example.txjju.smartgenplatform_android.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.LoginActivity;
import com.example.txjju.smartgenplatform_android.activity.MainActivity;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.activity.ProjectDetailsActivity;
import com.example.txjju.smartgenplatform_android.adapter.MarketProjectAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.FileUtils;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.example.txjju.smartgenplatform_android.view.MyRefreshLayout;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 众智市场的Fragment
 */
public class MarketFragment extends BaseFragment {

    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "MainActivity";

    private int lastVisibleItem = 0;//可见的最后一个item
    private final int PAGE_COUNT = 5;//每次加载的数据量
    private static final int REQUEST_PROJECTDETAILS = 97;//请求码
    private static final int RESULT_PROJECTDETAILS = 1;//1代表成功，0代表失败

    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private MyRefreshLayout myRefreshLayout;
    private RecyclerView rvProject;// 项目列表控件
    private List<Creativeproject> projectList = new ArrayList<>();
    private GridLayoutManager mLayoutManager;
    private MarketProjectAdapter projectAdapter; // 项目列表适配器

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    private Handler mHandler = new Handler(Looper.getMainLooper());//获取主线程的Handler

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

    private void loadData() {
        final ProgressDialog pgDialog = new ProgressDialog(getActivity());
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("queryParam.condition"," (Creproject_state=0 or Creproject_state=1)");//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.CREPROJECT_GET)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MainActivity","市场：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"市场：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"市场：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"市场：结果："+result);
                    //result = "{\"msg\":\"查询成功\",\"pageSize\":11,\"total\":11,\"page\":1,\"success\":true,\"datas\":[{\"companyId\":null,\"creprojectClassify\":1,\"creprojectContent\":\"这次设计的目的主要是解决婴儿车在使用过程中存在的存\\r\\n\\t\\t\\t\\t在的一些问题。例如，使用周期短，占地空间大等。在功能上使其多样化，能够有一\\r\\n\\t\\t\\t\\t个较长的使用周期陪伴婴儿的成长，在使用的舒适安全的方面也经过了设计，在减震 效果上进行了改良，三轮系的设计，使转弯更灵活。\",\"creprojectEvaluateOpinion\":\"\",\"creprojectEvaluateResult\":0,\"creprojectEvaluateTime\":\"2017-12-27\",\"creprojectLabel\":\"\",\"creprojectModifyTime\":\"2017-12-26\",\"creprojectPicture\":\"\\/SmartgenPlatform\\/img\\/sy6.png\",\"creprojectPlan\":\"\\/SmartgrnPatrform\\/web\\/file\",\"creprojectPraise\":0,\"creprojectReleaseTime\":\"2017-12-26\",\"creprojectState\":0,\"creprojectTitle\":\"多功能婴儿车\",\"creprojectVideo\":\"fgd\",\"expertJobNumber\":null,\"id\":null,\"products\":[],\"userId\":2}]}";
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"市场：测试");
                            BasePojo<Creativeproject> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        getActivity(), result, new TypeToken<BasePojo<Creativeproject>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getTotal() > 0){ //信息获取成功,回传有数据
                                            List<Creativeproject> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                            Log.i(TAG,"市场：结果:"+list.size());
                                            projectList.clear();
                                            projectList.addAll(list);
                                            pgDialog.dismiss();//隐藏进度栏
                                            projectAdapter.resetDatas();//刷新时，数据源置为空
                                            updateRecyclerView(0, PAGE_COUNT);// 通知适配器更新列表
                                            refreshLayout.finishRefresh();  //停止刷新
                                        }else{
                                            Log.i(TAG,"市场：后台传来数据为空"+basePojo.getMsg());
                                            ToastUtils.Toast(getActivity(),basePojo.getMsg(),0);
                                            pgDialog.dismiss();//隐藏进度栏
                                            projectAdapter.resetDatas();//刷新时，数据源置为空
                                            refreshLayout.finishRefresh();  //停止刷新
                                        }
                                    }else{
                                        pgDialog.dismiss();
                                        Log.i(TAG,"市场：后台传来失败了"+basePojo.getMsg());
                                        ToastUtils.Toast(getActivity(),basePojo.getMsg(),0);
                                    }
                                   /* if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Creativeproject> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        Log.i(TAG,"市场：结果:"+list.size());
                                        projectList.clear();
                                        projectList.addAll(list);
                                        pgDialog.dismiss();
                                        projectAdapter.resetDatas();//刷新时，数据源置为空
                                        updateRecyclerView(0, PAGE_COUNT);// 通知适配器更新列表
                                        refreshLayout.finishRefresh();  //停止刷新
                                    }else{
                                        Log.i(TAG,"市场：后台传来失败了"+basePojo.getMsg());
                                        ToastUtils.Toast(getActivity(),basePojo.getMsg(),0);
                                    }*/
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

    //fragment中布局以及控件的获取都写在此方法中
    //onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        //获取新建视图View中布局文件的控件
        //initData();//初始化假的数据，用于测试
        initViews(view);// 获取新建视图View中布局文件的控件
        initRefreshLayout();//设置刷新
        initRecyclerView();//初始化RecyclerView
        //PriceUtils.convertPriceSize(getActivity(),newPrice,"￥50", UIUtils.dp2px(getActivity(),16));//设置价格字体大小
        return view;
    }

    private void initViews(View view) {
        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_market);
        rvProject = (RecyclerView) view.findViewById(R.id.rc_project_market);

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
        // 初始化RecyclerView的Adapter
        // 第一个参数为数据，上拉加载的原理就是分页，所以我设置常量PAGE_COUNT=5，即每次加载5个数据
        // 第二个参数为Context
        // 第三个参数为hasMore，是否有新数据
        projectAdapter = new MarketProjectAdapter(getDatas(0, PAGE_COUNT), getActivity(), getDatas(0, PAGE_COUNT).size() > 0 ? true : false);
        mLayoutManager = new GridLayoutManager(getActivity(), 1);

        // 添加分割线（重新绘制分割线）
        rvProject.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.bgLightGray)));
        rvProject.setLayoutManager(mLayoutManager);
        rvProject.setAdapter(projectAdapter);
        rvProject.setItemAnimator(new DefaultItemAnimator());

        // 实现上拉加载重要步骤，设置滑动监听器，RecyclerView自带的ScrollListener
        rvProject.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 在newState为滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果没有隐藏footView，那么最后一个条目的位置就比我们的getItemCount少1
                    if (projectAdapter.isFadeTips() == false && lastVisibleItem + 1 == projectAdapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 然后调用updateRecyclerview方法更新RecyclerView
                                updateRecyclerView(projectAdapter.getRealLastPosition(), projectAdapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目就要比getItemCount要少2
                    if (projectAdapter.isFadeTips() == true && lastVisibleItem + 2 == projectAdapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 然后调用updateRecyclerview方法更新RecyclerView
                                updateRecyclerView(projectAdapter.getRealLastPosition(), projectAdapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在滑动完成后，拿到最后一个可见的item的位置
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        //监听item
        projectAdapter.setOnItemClickListener(new MarketProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG,"市场项目跳转");
                int projectId = projectList.get(position).getId();
                Log.i(TAG,"项目id"+projectId);
                Intent intent = new Intent(getActivity(), ProjectDetailsActivity.class);//跳转到项目详情
                intent.putExtra("creProjectId",Integer.toString(projectId));
                //这里使用startActivityForResult进行跳转是为了方便有回传，回传里可以刷新列表
                startActivityForResult(intent,REQUEST_PROJECTDETAILS);//REQUEST_PROJECTDETAILS是请求码
            }
        });
    }

    // 用户跳转到项目详情后执行回调,进行刷新
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
            loadData(); // 重新加载数据
        }
    }

    private List<Creativeproject> getDatas(final int firstIndex, final int lastIndex) {
        List<Creativeproject> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < projectList.size()) {
                resList.add(projectList.get(i));
            }
        }
        return resList;
    }

    // 上拉加载时调用的更新RecyclerView的方法
    private void updateRecyclerView(int fromIndex, int toIndex) {
        // 获取从fromIndex到toIndex的数据
        List<Creativeproject> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            // 然后传给Adapter，并设置hasMore为true
            projectAdapter.updateList(newDatas, true);
        } else {
            projectAdapter.updateList(null, false);
        }
    }

   /* @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        adapter.resetDatas();
        updateRecyclerView(0, PAGE_COUNT);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }*/

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
