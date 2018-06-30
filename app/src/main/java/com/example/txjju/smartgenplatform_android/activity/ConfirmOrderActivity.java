package com.example.txjju.smartgenplatform_android.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.ConfirmOrderProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.HomeProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.HomeProjectAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pay.PayResult;
import com.example.txjju.smartgenplatform_android.pay.SignUtils;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.Purchase;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseaddress;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseitem;
import com.example.txjju.smartgenplatform_android.pojo.Shoppingcart;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ConfirmOrderActivity";

    private TextView tvConfirmOrderUserName,tvConfirmOrderUserPhone,tvConfirmOrderUserAddress,tvOrderTotalNum,tvConfirmOrderActuallyPrice;
    private EditText evConfirmOrderMessage;
    private ImageView ivConfirmOrderBack,ivConfirmOrderEdit;
    private RecyclerView rvProduct;// 购买的产品列表控件
    private RadioButton rbConfirmOrderZfb,rbConfirmOrderWx;
    private Button btnConfirmOrderPay;
    private ConfirmOrderProductAdapter confirmOrderProductAdapter;

    private User user;
    private int userId;//保存用户ID
    public int purchaseAddressId;//保存收货地址ID
    private int orderId;//保存订单ID
    public String addressId;//保存地址ID
    public String productId;//保存产品ID
    private String [] productIds = new String[100];//在购物车里限制产品在100份以内
    private int productIdCount;
    private String productBuyCount;//保存用户购买产品数量
    private String [] productBuyCounts = new String[100];//在购物车里限制产品在100份以内
    private String shoppingCartId;
    private String [] shoppingCartIds = new String[100];//在购物车里限制产品在100份以内
    private String userName,userPhone,userAddress;//保存用户数据
    private double actuallyPrice = 0.0;
    private String condition = "";//拼接条件


    private String addressResult,productResult,orderResult;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();

    private List<Product> productList = new ArrayList<>();
    //支付相关定义数据
    // 商户PID
    public static final String PARTNER = "2088021347632879";
    // 商户收款账号
    public static final String SELLER = "public@youchexiang.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALhSxLLX9qFDqJxd6JfdrKmFzNa7lleKzMHqoqbNHkyDwPxkFypy8B8L3g7vXK1CCQ5q7sTlm6K4VCKYgRfCslDflGE3sOxZuWKHkYi/ETLqTPuLEXSKiT9x6WMYyzD6Qjbu5BOJp0VeRuZFDjbU9iMLRH+QdYuGz+d6AsZ8JOfdAgMBAAECgYEAssQLY0zTNLpS6Dyn3covsEZFUSmj+Qlz5sqyr0WWkiziWaaVVSUeP24aErARWTQHZoVNR4dMt7dAWkFASpQYrXOJAkTV7RQJy3zlKz6MUEDcpkcEcwJi3B13bSYjhn28Uyi3fOT31MiPCnh1TRerexd50z3S4BKDXZiWICfM6iECQQDdmmXwT+T96J8cuovGB5FYQfB4JjZ1CgFI3OjhAiC9h+hwV72qNqbacSGYlV4KIsIM8sN6qDZYuNXe7AgrhCkVAkEA1O8F7rnkABEP8pF0ZKEgiWRmThX6xFIcedpTsw5nkCxBtNmefjz/g8iLoeiUPPhp83gcb3sSqYbteFsPog7lqQJADPC+MSlJMvaJjBDsppS8jQ3Ur/9zQKRj7NBRnQoVxVuRXDYTckQcvDbNAm7+fdMHx9/JQHlgKxAoFQttcrUV/QJBAJc7FAmTOR3bAGqVaAc4cPVju01mSu44K0VYDO41ItTAugIKNYkPJaKhQprBptcOz6E+A2QiEwUYcXxzoEzHK9ECQQCvM3oQPSggEIo5ya6h+AtlIwC0pnk6S1YChYl28hqrhWhU4CuBsfGy4k8mCcrGoRZBcNZovDF5DZFHeWdYUKp1";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "-----BEGIN PUBLIC KEY-----MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB-----END PUBLIC KEY-----";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtils.Toast(ConfirmOrderActivity.this,"支付成功",0);
                        ConfirmOrderActivity.this.finish();
                        //发送修改支付状态
                        updateOrderState();
                        Intent intent = new Intent(ConfirmOrderActivity.this,PaySuccessActivity.class);//跳转到订单成功页面
                        intent.putExtra("userName",userName);
                        intent.putExtra("userPhone",userPhone);
                        intent.putExtra("userAddress",userAddress);
                        startActivity(intent);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.Toast(ConfirmOrderActivity.this,"支付结果确认中",0);

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

                            ToastUtils.Toast(ConfirmOrderActivity.this,"支付失败",0);
                            ConfirmOrderActivity.this.finish();
                            Intent intent = new Intent(ConfirmOrderActivity.this,OrderDetailWaitActivity.class);//跳转到订单成功页面
                            intent.putExtra("userName",userName);
                            intent.putExtra("userPhone",userPhone);
                            intent.putExtra("userAddress",userAddress);
                            //Log.i(TAG,"订单号"+orderId);
                            intent.putExtra("orderId",Integer.toString(orderId));
                            startActivity(intent);
                        }
                    }
                    break;
                }
               /* case SDK_CHECK_FLAG: {
                    Toast.makeText(ConfirmOrderActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }*/
                default:
                    break;
            }
        };
    };

    private void updateOrderState() {
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        //拼接参数
        formBody.add("purchasePatternOfPayment","支付宝");
        formBody.add("id",Integer.toString(orderId));
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_UPDATEORDERSTATE)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ConfirmOrderActivity","完成订单：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"完成订单：查到了?");
                Log.i(TAG,"response.code()=="+response.code());
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"完成订单：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    orderResult = response.body().string();
                    Log.i(TAG,"完成订单：结果："+orderResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"完成订单：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ConfirmOrderActivity.this, orderResult, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() ){   // 信息获取成功,有数据
                                       // ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
                                    }else{
                                        ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        //获取Intent对象
        Intent intent = getIntent();
        //取出key对应的value值
        productId = intent.getStringExtra("productId");
        productIdCount = productId.split(";").length;
        for(int i = 0;i < productIdCount;i++){
            productIds[i] = productId.split(";")[i];
            Log.i(TAG,"产品跳转传过来的数据,产品ID:"+productIds[i]);
        }
        productBuyCount = intent.getStringExtra("productBuyCount");
        for(int i = 0;i < productIdCount;i++){
            productBuyCounts[i] = productBuyCount.split(";")[i];
            Log.i(TAG,"产品跳转传过来的数据，产品数量:"+productBuyCounts[i]);
        }
        shoppingCartId = intent.getStringExtra("shoppingCartIds");
        for(int i = 0;i < productIdCount;i++){
            Log.i(TAG,"有没有购物车"+shoppingCartId);
            if(shoppingCartId !=null){
                shoppingCartIds[i] = shoppingCartId.split(";")[i];
                Log.i(TAG,"产品跳转传过来的数据，购物车产品数量:"+shoppingCartIds[i]);
            }
        }
        //获取用户信息
        user = SPUtil.getUser(ConfirmOrderActivity.this);
        userId = user.getId();
        loadData();
        init();
        initRecyclerView();
    }

    private void loadData() {
        initAddress();//初始化用户地址信息
        initOrder();//初始化订单信息
    }

    private void initOrder() {
        final ProgressDialog pgDialog = new ProgressDialog(ConfirmOrderActivity.this);
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        //拼接参数

        Log.i(TAG,"拼接条件前："+productIdCount);
        for(int i = 0;i < productIdCount;i++){
            if(i == 0){
                condition +="id="+productIds[i];
            }else{
                condition +=" or id="+productIds[i];
            }
        }
        Log.i(TAG,"拼接条件后："+condition);
        formBody.add("queryParam.condition",condition);//获取购买的产品的详细信息
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_GET)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ConfirmOrderActivity","确认订单：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"确认订单：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"确认订单：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    productResult = response.body().string();
                    Log.i(TAG,"确认订单：结果："+productResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"确认订单：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ConfirmOrderActivity.this, productResult, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Product> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        Log.i(TAG,"确认订单：结果"+list.toString());
                                        for(int i = 0;i < list.size();i++){
                                            list.get(i).setProductBuyCount(productBuyCounts[i]);
                                            actuallyPrice += (list.get(i).getProductPrice()*Integer.parseInt(productBuyCounts[i]));
                                        }
                                        tvConfirmOrderActuallyPrice.setText(actuallyPrice+"");
                                        pgDialog.dismiss();
                                        productList.clear();
                                        productList.addAll(list);
                                        confirmOrderProductAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                                    }else{
                                        pgDialog.dismiss();
                                        ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
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

    private void initAddress() {
        final ProgressDialog pgDialog = new ProgressDialog(ConfirmOrderActivity.this);
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
                Log.d("ConfirmOrderActivity","确认订单：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"确认订单：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"确认订单：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    addressResult = response.body().string();
                    Log.i(TAG,"确认订单：结果："+addressResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"确认订单：测试");
                            BasePojo<Purchaseaddress> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ConfirmOrderActivity.this, addressResult, new TypeToken<BasePojo<Purchaseaddress>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Purchaseaddress> list  = basePojo.getDatas();
                                        Log.i(TAG,"确认订单：结果"+list.toString());
                                        tvConfirmOrderUserName.setText(list.get(0).getPuraddressUserName());
                                        tvConfirmOrderUserPhone.setText(list.get(0).getPuraddressUserPhone());
                                        tvConfirmOrderUserAddress.setText(list.get(0).getPuraddressAddress());
                                        userName = list.get(0).getPuraddressUserName();
                                        userPhone = list.get(0).getPuraddressUserPhone();
                                        userAddress = list.get(0).getPuraddressAddress();
                                        purchaseAddressId = list.get(0).getId();//记录收货地址Id
                                        pgDialog.dismiss();
                                    }else{
                                        pgDialog.dismiss();
                                        ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
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

    private void initRecyclerView() {
        //适配产品的RecyclerView
        // RecyclerView必须用LinearLayoutManager进行配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(ConfirmOrderActivity.this);
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvProduct.setHasFixedSize(true);
        rvProduct.setNestedScrollingEnabled(false);
        // 配置LinearLayoutManager
        rvProduct.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvProduct.addItemDecoration(new RecycleViewDivider(ConfirmOrderActivity.this,
                LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.white)));

        confirmOrderProductAdapter = new ConfirmOrderProductAdapter(ConfirmOrderActivity.this, productList);
        rvProduct.setAdapter(confirmOrderProductAdapter);
    }

    private void init() {
        tvConfirmOrderUserName = findViewById(R.id.tv_confirmOrder_userName);
        tvConfirmOrderUserPhone = findViewById(R.id.tv_confirmOrder_userPhone);
        tvConfirmOrderUserAddress = findViewById(R.id.tv_confirmOrder_userAddress);
        tvOrderTotalNum = findViewById(R.id.tv_order_total_num);
        tvConfirmOrderActuallyPrice = findViewById(R.id.tv_confirmOrder_actuallyPrice);
        evConfirmOrderMessage = findViewById(R.id.ev_confirmOrder_message);
        rvProduct = findViewById(R.id.rv_confirmOrder_product);
        rbConfirmOrderZfb = findViewById(R.id.rb_confirmOrder_zfb);
        rbConfirmOrderWx = findViewById(R.id.rb_confirmOrder_wx);
        btnConfirmOrderPay = findViewById(R.id.btn_confirmOrder_pay);
        ivConfirmOrderBack = findViewById(R.id.iv_confirmOrder_back);
        ivConfirmOrderEdit = findViewById(R.id.iv_confirmOrder_edit);

        ivConfirmOrderBack.setOnClickListener(this);
        ivConfirmOrderEdit.setOnClickListener(this);
        rbConfirmOrderZfb.setOnClickListener(this);
        rbConfirmOrderWx.setOnClickListener(this);
        btnConfirmOrderPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_confirmOrder_back:
                this.finish();
                break;
            case R.id.iv_confirmOrder_edit:
                Intent addressIntent = new Intent(this,UpdateAdressActivity.class);//跳转到地址编辑器
                startActivityForResult(addressIntent,1);//这里要回调，在回调中更新收货地址，且要更新收货地址ID
                break;
            case R.id.rb_confirmOrder_zfb:
                break;
            case R.id.rb_confirmOrder_wx:
                break;
            case R.id.btn_confirmOrder_pay:
                createOrder();//订单生成，订单状态为未付款
                //支付
                break;
        }

    }
    // 用户跳转到项目详情或产品详情后执行回调,进行刷新
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"回调没");
        addressId = data.getStringExtra("addressId");
        Log.i(TAG,"回调的数据"+requestCode+"|"+resultCode+"|"+addressId);
        if(requestCode == 1 && resultCode == 0){
            // 检测网络
            if (!checkNetwork(ConfirmOrderActivity.this)) {
                Toast toast = Toast.makeText(ConfirmOrderActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            Log.i(TAG,"回调函数");
            if(addressId == null){
                initAddress();
            }else{
                refreshAddress();//初始化用户地址信息
            }
        }
    }

    private void refreshAddress() {
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        Log.i(TAG,"提交订单用户ID"+userId);
        formBody.add("queryParam.condition","id="+addressId);//获取用户收货详细信息
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_PURCHASEADDRESS)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ConfirmOrderActivity","确认订单：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"确认订单：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"确认订单：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    addressResult = response.body().string();
                    Log.i(TAG,"确认订单：结果："+addressResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"确认订单：测试");
                            BasePojo<Purchaseaddress> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ConfirmOrderActivity.this, addressResult, new TypeToken<BasePojo<Purchaseaddress>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Purchaseaddress> list  = basePojo.getDatas();
                                        Log.i(TAG,"确认订单：结果"+list.toString());
                                        tvConfirmOrderUserName.setText(list.get(0).getPuraddressUserName());
                                        tvConfirmOrderUserPhone.setText(list.get(0).getPuraddressUserPhone());
                                        tvConfirmOrderUserAddress.setText(list.get(0).getPuraddressAddress());
                                        userName = list.get(0).getPuraddressUserName();
                                        userPhone = list.get(0).getPuraddressUserPhone();
                                        userAddress = list.get(0).getPuraddressAddress();
                                        purchaseAddressId = list.get(0).getId();//记录收货地址Id
                                    }else{
                                        ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
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


    private void createOrder() {
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        //拼接参数
        Log.i(TAG,"创建订单:"+productIdCount+"|"+"purchaseAddressId");
        for(int i = 0;i < productIdCount;i++){
            Log.i(TAG,"数量："+productBuyCounts[i]);
            Log.i(TAG,"留言："+evConfirmOrderMessage.getText());
            Log.i(TAG,"用户ID："+userId);
            Log.i(TAG,"地址ID："+purchaseAddressId);
            formBody.add("purchases["+i+"].purchaseitemCount",productBuyCounts[i]);//获取购买的产品的详细信息
            formBody.add("purchases["+i+"].purchaseitemMsg",""+evConfirmOrderMessage.getText());//获取购买的产品的详细信息
            formBody.add("purchases["+i+"].productId",productIds[i]);//获取购买的产品的详细信息
        }
        formBody.add("user.id",""+userId);//获取购买的产品的用户ID信息
        formBody.add("purchaseaddress.id",""+purchaseAddressId);//获取购买的产品的收货地址信息
        /*formBody.add("purchases[0].purchaseitemCount","1");//获取购买的产品的详细信息
        formBody.add("purchases[0].purchaseitemMsg","456");//获取购买的产品的详细信息
        formBody.add("purchases[0].productId","2");//获取购买的产品的详细信息
        formBody.add("user.id","2");//获取购买的产品的用户ID信息
        formBody.add("purchaseaddress.id","1");//获取购买的产品的收货地址信息*/
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_CREATORDER)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ConfirmOrderActivity","生成订单：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"生成订单：查到了?");
                Log.i(TAG,"response.code()=="+response.code());
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"生成订单：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    productResult = response.body().string();
                    Log.i(TAG,"生成订单：结果："+productResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"生成订单：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ConfirmOrderActivity.this, productResult, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() ){   // 信息获取成功,有数据
                                        getOrderId();
                                        List<Product> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
                                        if(shoppingCartId !=null){
                                            deleteCartProduct();//订单生成，删除购物车
                                        }
                                    }else{
                                        ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
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

    private void deleteCartProduct() {
        Log.i(TAG,"要删除的数据长度"+productIdCount);
        for(int i = 0;i<productIdCount;i++){
            Log.i(TAG,"要删除的数据"+shoppingCartIds[i]);
            deleteCart(shoppingCartIds[i]);
        }
    }

    private void deleteCart(String shoppingCartId) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("id",shoppingCartId);
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_DETELECART)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ShoppingCartActivity","删除购物车详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"删除购物车详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"删除购物车详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"删除购物车详情：结果："+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"删除购物车详情：测试");
                            BasePojo<Shoppingcart> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ConfirmOrderActivity.this, result, new TypeToken<BasePojo<Shoppingcart>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        Log.i(TAG,"删除购物车成功");
                                    }else{
                                        Log.i(TAG,"删除购物车详情：后台传来失败了"+basePojo.getMsg());
                                        ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
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


    private void getOrderId() {
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        //拼接参数
        formBody.add("queryParam.condition","user_id="+userId);//获取订单详细信息
        formBody.add("queryParam.orderBy","id");//按项目点赞数来排序
        formBody.add("queryParam.orderByInTurn","DESC");//降序排列
        formBody.add("queryParam.pageSize","1");//取6个最火热的产品
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_GETORDERDETAILS)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"获取订单号：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"获取订单号：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"获取订单号：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    productResult = response.body().string();
                    Log.i(TAG,"获取订单号：结果："+productResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"获取订单号：测试");
                            BasePojo<Purchase> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ConfirmOrderActivity.this, productResult, new TypeToken<BasePojo<Purchase>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Purchase> list = basePojo.getDatas();
                                        orderId = list.get(0).getPurchaseId();
                                        pay();//调用支付窗口
                                    }else{
                                        ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
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
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    //
                    finish();
                }
            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo("众智品牌", "该测试商品的详细描述", "0.01");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ConfirmOrderActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                System.out.println("result = " + result);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }


    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
