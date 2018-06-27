package com.example.txjju.smartgenplatform_android.fragment;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.adapter.HomeProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.MineCollectProductAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.FileUtils;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
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
 * 收藏商品的fragment
 */
public class CollectProductFragment extends Fragment {

    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PRODUCTDETAILS = 95;
    private static final int RESULT_PRODUCTDETAILS = 1;//1代表成功，0代表失败

    private int userId;//保存用户ID

    private String productResult;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();

    private RecyclerView rvProduct;    // 商品列表控件
    private MineCollectProductAdapter productAdapter;    // 商品列表适配器

    private List<Product> productList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取用户信息
        User user = SPUtil.getUser(getActivity());
        if(user != null){
            userId = user.getId();
            Log.i(TAG,"产品用户ID"+userId);
        }
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
        Log.i(TAG,"用户ID"+userId);
        formBody.add("userId",Integer.toString(userId));//通过用户id来查询用户收藏的产品
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_COLLECTLIST)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MainActivity","个人中心：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"个人中心：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"个人中心：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    productResult = response.body().string();
                    Log.i(TAG,"个人中心：结果："+productResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"个人中心：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        getActivity(), productResult, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Product> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        Log.i(TAG,"个人中心：结果"+list.toString());
                                        pgDialog.dismiss();
                                        productList.clear();
                                        productList.addAll(list);
                                        productAdapter.notifyDataSetChanged(); // 通知适配器更新列表
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
        View view = inflater.inflate(R.layout.fragment_collect_product, container, false);
        //获取新建视图View中布局文件的控件
        //initData();//初始化假的数据，用于测试
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

        rvProduct.setHasFixedSize(true);
        rvProduct.setNestedScrollingEnabled(false);

        rvProduct.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvProduct.addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.bgLightGray)));
//        if (entity != null && entity.topic != null && entity.topic.items != null && entity.topic.items.size() > 0) {
//            List<SpeedHourEntity.TopicBean.ItemsBean.ListBean> listBeen = entity.topic.items.get(0).list;
//            if (listBeen != null && listBeen.size() > 0)
//                speedHourAdapter.setList(listBeen);
//            rvProduct.setAdapter(speedHourAdapter);
//        }
        productAdapter = new MineCollectProductAdapter(getActivity(), productList);
        rvProduct.setAdapter(productAdapter);
        //监听产品项目item
        productAdapter.setOnItemClickListener(new MineCollectProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG,"商城产品跳转");
                int productId = productList.get(position).getId();
                Log.i(TAG,"项目id"+productId);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);//跳转到产品详情
                intent.putExtra("productId",Integer.toString(productId));
                //这里使用startActivityForResult进行跳转是为了方便有回传，回传里可以刷新列表
                startActivityForResult(intent,REQUEST_PRODUCTDETAILS);//REQUEST_PRODUCTDETAILS是请求码
            }
        });
    }

    // 用户跳转到项目详情或产品详情后执行回调,进行刷新
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PRODUCTDETAILS && resultCode == RESULT_PRODUCTDETAILS){
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

    private void initViews(View view) {
        rvProduct = view.findViewById(R.id.rc_collect_product_mine);
    }

    /**
     * 测试用的假数据
     */
    private void initData() {
        Log.i(TAG,"SUSU");
        String data = FileUtils.readAssert(getActivity(),"products.txt");
        Log.i(TAG,data);
        try {
            BasePojo<Product> basePojo = JsonUtil.getBaseFromJson(getActivity(),
                    data, new TypeToken<BasePojo<Product>>(){}.getType());
            Log.i(TAG,"123");
            Log.i(TAG,basePojo.toString());
            Log.i(TAG,"456");
            List<Product> list = basePojo.getDatas();
            Log.i(TAG,"789");
            Log.i(TAG,"error"+list.size());
            productList.addAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
