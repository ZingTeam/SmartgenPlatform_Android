package com.example.txjju.smartgenplatform_android.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.ConfirmOrderProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.OrderDetailWaitProductAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pay.PayResult;
import com.example.txjju.smartgenplatform_android.pay.SignUtils;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.Purchase;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseitem;
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

public class OrderDetailWaitActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OrderDetailWaitActivity";

    private TextView tvOrderDetailOrderUserName,tvOrderDetailOrderUserPhone,tvOrderDetailOrderUserAddress,
            tvOrderDetailOrderTotalPrice,tvOrderDetailOrderNeedPrice,tvOrderDetailOrderPurchaseNo,tvItemOrderCancel,tvItemOrderSubmit;
    private ImageView ivOrderDetailOrderBack;
    private RecyclerView rvProduct;// 购买的产品列表控件
    private OrderDetailWaitProductAdapter orderDetailWaitProductAdapter;

    private User user;
    private int userId;//保存用户ID
    public int purchaseAddressId;//保存收货地址ID
    private String orderId;//保存订单ID
    public String productId;//保存产品ID
    private String [] productIds = new String[100];//在购物车里限制产品在100份以内
    private int productIdCount;
    private String productBuyCount;//保存用户购买产品数量
    private double actuallyPrice = 0.0;
    private String [] productBuyCounts = new String[100];//在购物车里限制产品在100份以内

    private String userName,userPhone,userAddress;//保存用户数据

    private List<Purchaseitem> productList = new ArrayList<>();
    private List<Purchase> purchaseList = new ArrayList<>();

    private String productResult,orderResult;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();
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
                        ToastUtils.Toast(OrderDetailWaitActivity.this,"支付成功",0);
                        OrderDetailWaitActivity.this.finish();
                        //发送修改支付状态
                        updateOrderState();
                        Intent intent = new Intent(OrderDetailWaitActivity.this,PaySuccessActivity.class);//跳转到订单成功页面
                        intent.putExtra("userName",userName);
                        intent.putExtra("userPhone",userPhone);
                        intent.putExtra("userAddress",userAddress);
                        startActivity(intent);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.Toast(OrderDetailWaitActivity.this,"支付结果确认中",0);

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

                            ToastUtils.Toast(OrderDetailWaitActivity.this,"支付失败",0);
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
        formBody.add("purchasePatternOfPayment","支付宝");//获取购买的产品的用户ID信息
        formBody.add("id",orderId);//获取购买的产品的收货地址信息
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_UPDATEORDERSTATE)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OrderDetailWaitActivity","完成订单：获取数据失败了");
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
                                        OrderDetailWaitActivity.this, orderResult, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() ){   // 信息获取成功,有数据
                                        //ToastUtils.Toast(OrderDetailWaitActivity.this,basePojo.getMsg(),0);
                                    }else{
                                        ToastUtils.Toast(OrderDetailWaitActivity.this,basePojo.getMsg(),0);
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
        setContentView(R.layout.activity_order_detail_wait);
        //获取Intent对象
        Intent intent = getIntent();
        //取出key对应的value值
        userName = intent.getStringExtra("userName");
        userPhone = intent.getStringExtra("userPhone");
        userAddress = intent.getStringExtra("userAddress");
        orderId = intent.getStringExtra("orderId");
        //获取用户信息
        user = SPUtil.getUser(OrderDetailWaitActivity.this);
        userId = user.getId();
        loadData();
        init();
        initRecyclerView();
    }

    private void loadData() {
        final ProgressDialog pgDialog = new ProgressDialog(OrderDetailWaitActivity.this);
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        //拼接参数
        Log.i(TAG,"订单ID"+orderId);
        formBody.add("queryParam.condition","id="+orderId);//获取订单详细信息
        Log.i(TAG,"发请求"+orderId);
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_GETORDERDETAILS)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"等待付款订单：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"等待付款订单：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"等待付款订单：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    productResult = response.body().string();
                    Log.i(TAG,"等待付款订单：结果："+productResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"等待付款订单：测试");
                            BasePojo<Purchase> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        OrderDetailWaitActivity.this, productResult, new TypeToken<BasePojo<Purchase>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                         purchaseList  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        Log.i(TAG,"等待付款订单：结果"+purchaseList.toString());
                                        actuallyPrice += purchaseList.get(0).getPurchasePrice();
                                        tvOrderDetailOrderTotalPrice.setText("￥ "+actuallyPrice+"");
                                        tvOrderDetailOrderNeedPrice.setText("￥ "+actuallyPrice+"");
                                        tvOrderDetailOrderPurchaseNo.setText(purchaseList.get(0).getPurchaseNo()+"");
                                        List<Purchaseitem> list = purchaseList.get(0).getPurchaseitems();
                                        pgDialog.dismiss();
                                        productList.clear();
                                        productList.addAll(list);
                                        orderDetailWaitProductAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                                    }else{
                                        ToastUtils.Toast(OrderDetailWaitActivity.this,basePojo.getMsg(),0);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetailWaitActivity.this);
        // 设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvProduct.setHasFixedSize(true);
        rvProduct.setNestedScrollingEnabled(false);
        // 配置LinearLayoutManager
        rvProduct.setLayoutManager(layoutManager);
        // 添加分割线（重新绘制分割线）
        rvProduct.addItemDecoration(new RecycleViewDivider(OrderDetailWaitActivity.this,
                LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.white)));

        orderDetailWaitProductAdapter = new OrderDetailWaitProductAdapter(OrderDetailWaitActivity.this, productList);
        rvProduct.setAdapter(orderDetailWaitProductAdapter);
    }

    private void init() {
        ivOrderDetailOrderBack = findViewById(R.id.iv_orderDetail_order_back);
        tvOrderDetailOrderUserName = findViewById(R.id.tv_orderDetail_order_userName);
        tvOrderDetailOrderUserPhone = findViewById(R.id.tv_orderDetail_order_userPhone);
        tvOrderDetailOrderUserAddress = findViewById(R.id.tv_orderDetail_order_userAddress);
        tvOrderDetailOrderTotalPrice = findViewById(R.id.tv_orderDetail_order_totalPrice);
        tvOrderDetailOrderNeedPrice = findViewById(R.id.tv_orderDetail_order_needPrice);
        tvOrderDetailOrderPurchaseNo = findViewById(R.id.tv_orderDetail_order_purchaseNo);
        tvItemOrderSubmit = findViewById(R.id.tv_item_order_submit);
        tvItemOrderCancel = findViewById(R.id.tv_item_order_cancel);
        rvProduct = findViewById(R.id.rv_orderDetail_order_product);

        ivOrderDetailOrderBack.setOnClickListener(this);
        tvItemOrderSubmit.setOnClickListener(this);
        tvItemOrderCancel.setOnClickListener(this);

        tvOrderDetailOrderUserName.setText(userName);
        tvOrderDetailOrderUserPhone.setText(userPhone);
        tvOrderDetailOrderUserAddress.setText(userAddress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_orderDetail_order_back://付款
                OrderDetailWaitActivity.this.finish();
                break;
            case R.id.tv_item_order_submit://付款
                pay();
                break;
            case R.id.tv_item_order_cancel://取消订单
                deleteOrder();
                break;
        }

    }

    private void deleteOrder() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        //拼接参数
        formBody.add("id",orderId);//取消订单详细信息
        Log.i(TAG,"发请求"+orderId);
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_CANCELORDER)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"取消订单：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"取消订单：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"取消订单：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    orderResult = response.body().string();
                    Log.i(TAG,"取消订单：结果："+orderResult);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"取消订单：测试");
                            BasePojo<Purchase> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        OrderDetailWaitActivity.this, productResult, new TypeToken<BasePojo<Purchase>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){   // 信息获取成功,有数据
                                        ToastUtils.Toast(OrderDetailWaitActivity.this,"您的订单取消成功",0);
                                        OrderDetailWaitActivity.this.finish();
                                    }else{
                                        ToastUtils.Toast(OrderDetailWaitActivity.this,basePojo.getMsg(),0);
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
                PayTask alipay = new PayTask(OrderDetailWaitActivity.this);
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
