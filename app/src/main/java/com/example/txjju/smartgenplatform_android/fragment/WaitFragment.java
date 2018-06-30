package com.example.txjju.smartgenplatform_android.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.CollectProductActivity;
import com.example.txjju.smartgenplatform_android.activity.ConfirmOrderActivity;
import com.example.txjju.smartgenplatform_android.activity.OrderDetailWaitActivity;
import com.example.txjju.smartgenplatform_android.activity.PaySuccessActivity;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.activity.ProjectDetailsActivity;
import com.example.txjju.smartgenplatform_android.adapter.HomeProductAdapter;
import com.example.txjju.smartgenplatform_android.adapter.HomeProjectAdapter;
import com.example.txjju.smartgenplatform_android.adapter.WaitPayProductAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pay.PayResult;
import com.example.txjju.smartgenplatform_android.pay.SignUtils;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.Purchase;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseitem;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.example.txjju.smartgenplatform_android.util.UIUtils;
import com.example.txjju.smartgenplatform_android.view.MyRefreshLayout;
import com.example.txjju.smartgenplatform_android.view.RecycleViewDivider;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitFragment extends Fragment {
    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "OrderActivity";

    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private RecyclerView rvProduct;    // 产品列表控件
    private WaitPayProductAdapter waitPayProductAdapter;    // 产品列表适配器
    private List<Purchaseitem> productList = new ArrayList<>();
    private List<Purchase> purchaseList = new ArrayList<>();
    private String purchaseItemId;
    private String purchaseId;
    private LinearLayout llNull;
    private int userId;//保存用户ID
    private User user;

    private String orderResult;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();

    private MyRefreshLayout myRefreshLayout;
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
                        ToastUtils.Toast(getActivity(),"支付成功",0);
                        //发送修改支付状态
                        updateOrderState();
                        loadData();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.Toast(getActivity(),"支付结果确认中",0);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.Toast(getActivity(),"支付失败",0);
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
        formBody.add("id",purchaseId);
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
                                        getActivity(), orderResult, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() ){   // 信息获取成功,有数据
                                        // ToastUtils.Toast(ConfirmOrderActivity.this,basePojo.getMsg(),0);
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
        formBody.add("queryParam.condition","user.id="+userId+" and purchaseState=0");//获取订单详细信息
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
                                        List<Purchaseitem> list = new ArrayList<>();
                                        Log.i(TAG,"数据"+list.toString());
                                        llNull.setVisibility(View.GONE);
                                        Log.i(TAG,"购物车数量"+purchaseList.size()+"|"+purchaseList.get(0).getPurchaseitems().size());
                                       // list.addAll(purchaseList.get(0).getPurchaseitems());
                                        //list.addAll(purchaseList.get(1).getPurchaseitems());
                                        productList.clear();
                                        for(int i = 0;i<purchaseList.size();i++){
                                            list.addAll(purchaseList.get(i).getPurchaseitems());
                                            /*for(int j = 0;j<purchaseList.get(i).getPurchaseitems().size();j++){
                                                Log.i(TAG,"获取未付款订单：结果"+purchaseList.size()+"|"+purchaseList.get(i).getPurchaseitems().size());
                                                Log.i(TAG,"等待付款订单查询");
                                            }*/
                                        }
                                        productList.addAll(list);
                                        waitPayProductAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                                        refreshLayout.finishRefresh();
                                    }else{
                                        llNull.setVisibility(View.VISIBLE);
                                        productList.clear();
                                        waitPayProductAdapter.notifyDataSetChanged(); // 通知适配器更新列表
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

        waitPayProductAdapter = new WaitPayProductAdapter(getActivity(), productList);
        waitPayProductAdapter.setModifyCountInterface(new WaitPayProductAdapter.ModifyCountInterface() {
            @Override
            public void payOrder(int position) {
                purchaseId = Integer.toString(purchaseList.get(position).getPurchaseId());
                pay();//调用支付窗口
            }

            @Override
            public void cancelOrder(int position) {//取消订单
                purchaseItemId = Integer.toString(purchaseList.get(position).getPurchaseId());
                deletePurchaseItem();

            }
        });
        rvProduct.setAdapter(waitPayProductAdapter);
        //监听产品项目item
        waitPayProductAdapter.setOnItemClickListener(new WaitPayProductAdapter.OnItemClickListener() {
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

    private void deletePurchaseItem() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        //拼接参数
        formBody.add("id",purchaseItemId);//取消订单详细信息
        Log.i(TAG,"发请求"+purchaseItemId);
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
                                        getActivity(),orderResult , new TypeToken<BasePojo<Purchase>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){   // 信息获取成功,有数据
                                        ToastUtils.Toast(getActivity(),"您的订单取消成功",0);
                                        loadData();
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

    private void initViews(View view) {
        refreshLayout = view.findViewById(R.id.refresh_market);
        rvProduct = view.findViewById(R.id.rc_project_market);
        llNull = view.findViewById(R.id.ll_null);
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(getActivity()).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    //
                    getActivity().finish();
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
                PayTask alipay = new PayTask(getActivity());
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
