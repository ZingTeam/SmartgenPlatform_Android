package com.example.txjju.smartgenplatform_android.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.adapter.SippingPayProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.WaitPayProductAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Purchase;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseitem;
import com.example.txjju.smartgenplatform_android.pojo.User;
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
 * A simple {@link Fragment} subclass.
 */
public class SippingFragment extends Fragment {
    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "OrderActivity";

    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private RecyclerView rvProduct;    // 产品列表控件
    private SippingPayProductAdapter sippingPayProductAdapter;    // 产品列表适配器
    private List<Purchaseitem> productList = new ArrayList<>();
    private List<Purchase> purchaseList = new ArrayList<>();
    private LinearLayout llNull;
    private int userId;//保存用户ID
    private User user;

    private String orderResult;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();

    private MyRefreshLayout myRefreshLayout;

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
        //获取用户信息
        user = SPUtil.getUser(getActivity());
        if(user != null){
            userId = user.getId();
            Log.i(TAG,"产品用户ID"+userId);
        }
        loadData(); // 加载服务端数据
    }

    private void loadData() {
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        //拼接参数
        formBody.add("queryParam.condition","user.id="+userId+" and purchaseState=1");//获取订单详细信息
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_GETORDERDETAILS)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"获取未付款订单：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"获取未付款订单：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"获取未付款订单：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    orderResult = response.body().string();
                    Log.i(TAG,"获取未付款订单：结果："+orderResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"获取未付款订单：测试");
                            BasePojo<Purchase> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        getActivity(), orderResult, new TypeToken<BasePojo<Purchase>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        purchaseList  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        Log.i(TAG,"获取未付款订单：结果"+purchaseList.toString());
                                        List<Purchaseitem> list = new ArrayList<>();
                                        llNull.setVisibility(View.GONE);
                                        productList.clear();
                                        for(int i = 0;i<purchaseList.size();i++){
                                            list.addAll(purchaseList.get(i).getPurchaseitems());
                                            /*for(int j = 0;j<purchaseList.get(i).getPurchaseitems().size();j++){
                                                Log.i(TAG,"获取未付款订单：结果"+purchaseList.size()+"|"+purchaseList.get(i).getPurchaseitems().size());
                                                Log.i(TAG,"等待付款订单查询");
                                            }*/
                                        }
                                        productList.addAll(list);
                                        sippingPayProductAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                                        refreshLayout.finishRefresh();
                                    }else{
                                        llNull.setVisibility(View.VISIBLE);
                                        refreshLayout.finishRefresh();
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


    // fragment中布局以及控件的获取都写在此方法中
    // onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wait, container, false);
        //initData();//初始化假的数据，用于测试
        initViews(view);// 获取新建视图View中布局文件的控件
        initRefreshLayout();//设置刷新
        initRecyclerView();//初始化RecyclerView
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

    private void initRecyclerView() {
        //适配产品的RecyclerView
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvProduct.setHasFixedSize(true);
        rvProduct.setNestedScrollingEnabled(false);
        // 配置LinearLayoutManager
        rvProduct.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvProduct.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color.bgLightGray)));

        //适配项目的RecyclerView
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManagers = new LinearLayoutManager(getActivity());
        // 设置列表的排列方向
        layoutManagers.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManagers.setSmoothScrollbarEnabled(true);
        layoutManagers.setAutoMeasureEnabled(true);

        sippingPayProductAdapter = new SippingPayProductAdapter(getActivity(), productList);
        sippingPayProductAdapter.setModifyCountInterface(new SippingPayProductAdapter.ModifyCountInterface() {
            @Override
            public void payOrder(int position) {

            }

            @Override
            public void cancelOrder(int position) {

            }
        });
        rvProduct.setAdapter(sippingPayProductAdapter);
        //监听产品项目item
        sippingPayProductAdapter.setOnItemClickListener(new SippingPayProductAdapter.OnItemClickListener() {
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
                startActivityForResult(intent,1);//1是请求码
            }
        });

    }

    private void initViews(View view) {
        refreshLayout = view.findViewById(R.id.refresh_market);
        rvProduct = view.findViewById(R.id.rc_project_market);
        llNull = view.findViewById(R.id.ll_null);
    }


}
