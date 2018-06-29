package com.example.txjju.smartgenplatform_android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.MineCollectProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.MineUpdateAddressAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseaddress;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
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

public class UpdateAdressActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String TAG = "UpdateAdressActivity";
    private ImageView ivUpdateAddressBack;
    private TextView tvUpdateAddressAdd;
    private RecyclerView rvaddress;    // 地址列表控件
    private MineUpdateAddressAdapter mineUpdateAddressAdapter;
    private List<Purchaseaddress> addressList = new ArrayList<>();
    private static final int REQUEST_ADDRESSLIST = 94;
    private static final int RESULT_ADDRESSLIST = 1;//1代表成功，0代表失败
    private User user;
    private int userId;//保存用户ID
    public String productId;//保存产品ID
    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private MyRefreshLayout myRefreshLayout;

    private String addressResult;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_adress);
        //获取用户信息
        user = SPUtil.getUser(UpdateAdressActivity.this);
        if(user != null){
            userId = user.getId();
            Log.i(TAG,"产品用户ID"+userId);
        }
        init();
        initRefreshLayout();//设置刷新
        initRecyclerView();//初始化RecyclerView
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
                if (!checkNetwork(UpdateAdressActivity.this)) {
                    Toast toast = Toast.makeText(UpdateAdressActivity.this,"网络未连接", Toast.LENGTH_SHORT);
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
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(UpdateAdressActivity.this);
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvaddress.setHasFixedSize(true);
        rvaddress.setNestedScrollingEnabled(false);

        rvaddress.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvaddress.addItemDecoration(new RecycleViewDivider(UpdateAdressActivity.this,
                LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.white)));
//        if (entity != null && entity.topic != null && entity.topic.items != null && entity.topic.items.size() > 0) {
//            List<SpeedHourEntity.TopicBean.ItemsBean.ListBean> listBeen = entity.topic.items.get(0).list;
//            if (listBeen != null && listBeen.size() > 0)
//                speedHourAdapter.setList(listBeen);
//            rvProduct.setAdapter(speedHourAdapter);
//        }
        mineUpdateAddressAdapter = new MineUpdateAddressAdapter(UpdateAdressActivity.this, addressList);
        mineUpdateAddressAdapter.setModifyCountInterface(new MineUpdateAddressAdapter.ModifyCountInterface() {
            @Override
            public void updateAddress(int position) {//修改地址的跳转
                Intent intent = new Intent(UpdateAdressActivity.this, LoginActivity.class);
                intent.putExtra("purchaseAddressId",addressList.get(position).getId());
                startActivityForResult(intent,REQUEST_ADDRESSLIST);
            }
        });
        rvaddress.setAdapter(mineUpdateAddressAdapter);
        //监听地址item，选中该地址为收货地址
        mineUpdateAddressAdapter.setOnItemClickListener(new MineUpdateAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG,"地址跳转");
                int productId = addressList.get(position).getId();
                Log.i(TAG,"项目id"+productId);
                Intent intent = new Intent(UpdateAdressActivity.this, ProductDetailsActivity.class);//跳转到产品详情
                intent.putExtra("productId",Integer.toString(productId));
                startActivity(intent);
                UpdateAdressActivity.this.finish();
            }
        });
    }

    // 用户跳转到项目详情或产品详情后执行回调,进行刷新
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"回调没");
        Log.i(TAG,"回调的数据"+requestCode+"|"+resultCode);
        if(requestCode == REQUEST_ADDRESSLIST && resultCode == 0){
            // 检测网络
            if (!checkNetwork(UpdateAdressActivity.this)) {
                Toast toast = Toast.makeText(UpdateAdressActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            Log.i(TAG,"回调函数");
            loadData(); // 重新加载数据
        }
    }

    private void loadData() {
        final ProgressDialog pgDialog = new ProgressDialog(UpdateAdressActivity.this);
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        Log.i(TAG,"提交订单用户ID"+userId);
        formBody.add("queryParam.condition","user.id="+Integer.toString(userId));//获取用户收货详细信息
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_PURCHASEADDRESS)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("UpdateAdressActivity","地址列表：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"地址列表：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"地址列表：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    addressResult = response.body().string();
                    Log.i(TAG,"地址列表：结果："+addressResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"地址列表：测试");
                            BasePojo<Purchaseaddress> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        UpdateAdressActivity.this, addressResult, new TypeToken<BasePojo<Purchaseaddress>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Purchaseaddress> list  = basePojo.getDatas();
                                        Log.i(TAG,"地址列表：结果"+list.toString());
                                        pgDialog.dismiss();
                                        addressList.clear();
                                        addressList.addAll(list);
                                        mineUpdateAddressAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                                        refreshLayout.finishRefresh();  //停止刷新
                                    }else{
                                        pgDialog.dismiss();
                                        refreshLayout.finishRefresh();  //停止刷新
                                        //ToastUtils.Toast(UpdateAdressActivity.this,basePojo.getMsg(),0);
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

    private void init() {
        ivUpdateAddressBack = findViewById(R.id.iv_update_address_back);
        tvUpdateAddressAdd = findViewById(R.id.tv_update_address_add);
        rvaddress = findViewById(R.id.rc_address_update_address);
        refreshLayout = (MaterialRefreshLayout)findViewById(R.id.refresh_update_address);
        ivUpdateAddressBack.setOnClickListener(this);
        tvUpdateAddressAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_update_address_back:
                UpdateAdressActivity.this.finish();
                break;
            case R.id.tv_update_address_add:
                Intent intent = new Intent(UpdateAdressActivity.this,AddAddressActivity.class);
                startActivityForResult(intent,REQUEST_ADDRESSLIST);
                break;
        }

    }
}
