package com.example.txjju.smartgenplatform_android.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.ProductDetailPagerAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.fragment.ProductDetailFragment;
import com.example.txjju.smartgenplatform_android.fragment.ProductFragment;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseaddress;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.example.txjju.smartgenplatform_android.view.MorePopupWindow;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductDetailsActivity extends BaseActivity implements ProductFragment.OnScrollTabChangeListener, View.OnClickListener {
    private TabLayout tabLayout;//顶部导航栏
    private ViewPager viewPager;
    private ImageView ivProductBack,ivProductToolbarMore,ivProductCollect,ivProductPicture,ivCancel,ivProductcountSubtract,ivProductcountAdd, ivShoppingCartCancel,ivShoppingCartProductPicture,ivShoppingCartProductcountAdd,ivShoppingCartProductcountSubtract;
    private LinearLayout llProductAddCart,llProductCollect,llProductPay,llProductcomment;
    private ProductDetailPagerAdapter productPagerAdapter = null;
    private MorePopupWindow popupWindow = null;
    private Dialog warningDialog,purchaseDialog,shoppingCartDialog,addressDialog;
    private Button btnCancel,btnSure,purchaseBtnSure,shoppingCartBtnSure,addressBtnCancel,addressBtnSure;
    private TextView tvProductPrice,tvProductCount,tvProductBuyCount,tvShoppingCartProductPrice,tvShoppingCartProductCount,tvShoppingCartProductBuyCount;

    private List<Fragment> mFragments;
    private String[] titles = new String[]{"商品", "详情"};
    public final static String TAG = "ProductDetailsActivity";
    private User user;
    private int userId;//保存用户ID
    public String productId;//保存产品ID

    private String addressResult,result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //获取Intent对象
        Intent intent = getIntent();
        //取出key对应的value值
        productId = intent.getStringExtra("productId");
        Log.i(TAG,"产品跳转传过来的数据:"+productId);
        init();
        // 检测网络
        if (!checkNetwork(this)) {
            Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        //获取用户信息
         user = SPUtil.getUser(ProductDetailsActivity.this);
        if(user != null){
            userId = user.getId();
            //userId = 2;//假数据
            Log.i(TAG,"产品用户ID"+userId);
            initCollectRequest();//初始化收藏状态请求
        }
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

    /**
     * 初始化收藏状态请求
     */
    private void initCollectRequest() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("queryParam.condition","user.id="+Integer.toString(userId)+" and "+"product.id="+productId);//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象
                .url(Constant.PRODUCT_INITCOLLECT)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ProductDetailsActivity","产品详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"产品详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"产品详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"产品详情收藏初始化：结果："+result);
                    //result = "{\"msg\":\"查询成功\",\"pageSize\":11,\"total\":11,\"page\":1,\"success\":true,\"datas\":[{\"companyId\":null,\"creprojectClassify\":1,\"creprojectContent\":\"这次设计的目的主要是解决婴儿车在使用过程中存在的存\\r\\n\\t\\t\\t\\t在的一些问题。例如，使用周期短，占地空间大等。在功能上使其多样化，能够有一\\r\\n\\t\\t\\t\\t个较长的使用周期陪伴婴儿的成长，在使用的舒适安全的方面也经过了设计，在减震 效果上进行了改良，三轮系的设计，使转弯更灵活。\",\"creprojectEvaluateOpinion\":\"\",\"creprojectEvaluateResult\":0,\"creprojectEvaluateTime\":\"2017-12-27\",\"creprojectLabel\":\"\",\"creprojectModifyTime\":\"2017-12-26\",\"creprojectPicture\":\"\\/SmartgenPlatform\\/img\\/sy6.png\",\"creprojectPlan\":\"\\/SmartgrnPatrform\\/web\\/file\",\"creprojectPraise\":0,\"creprojectReleaseTime\":\"2017-12-26\",\"creprojectState\":0,\"creprojectTitle\":\"多功能婴儿车\",\"creprojectVideo\":\"fgd\",\"expertJobNumber\":null,\"id\":null,\"products\":[],\"userId\":2}]}";
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"产品详情：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ProductDetailsActivity.this, result, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getTotal() > 0){ //该用户对该产品已进行收藏
                                            //处理数据
                                                Glide.with(ProductDetailsActivity.this).load(R.mipmap.ic_collect_select).placeholder(R.mipmap.ic_collect_select).into(ivProductCollect);
                                        }else{//该用户对该产品未进行收藏
                                            Log.i(TAG,"产品详情：后台传来数据为空"+basePojo.getMsg());
                                           // ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
                                        }
                                    }else{
                                        Log.i(TAG,"产品详情：后台传来失败了"+basePojo.getMsg());
                                      //  ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
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

    private void init() {
        ivProductBack = findViewById(R.id.iv_productDetails_back);
        ivProductCollect = findViewById(R.id.iv_productDetails_collect);
        llProductCollect = findViewById(R.id.ll_productDetails_collect);
        llProductcomment = findViewById(R.id.ll_productDetails_comment);
        llProductAddCart = findViewById(R.id.ll_productDetails_add_cart);
        llProductPay = findViewById(R.id.ll_productDetails_pay);
        ivProductToolbarMore = findViewById(R.id.iv_productDetails_more);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        ivProductBack.setOnClickListener(this);
        ivProductToolbarMore.setOnClickListener(this);
        llProductCollect.setOnClickListener(this);
        llProductPay.setOnClickListener(this);
        llProductAddCart.setOnClickListener(this);

        mFragments = new ArrayList<>();
        mFragments.add(new ProductFragment());
        mFragments.add(new ProductDetailFragment());

        productPagerAdapter = new ProductDetailPagerAdapter(getSupportFragmentManager(), mFragments, Arrays.asList(titles));
        Log.i(TAG,"结果："+productPagerAdapter.getCount());
        viewPager.setOffscreenPageLimit(productPagerAdapter.getCount());
        viewPager.setAdapter(productPagerAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            popupWindow.dismiss();
        }
    };


    public static void open(Context context) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void currentTab(int tabId) {
        Log.i(TAG,"当前："+tabId);
        viewPager.setCurrentItem(tabId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_productDetails_back:
               this.finish();
                break;
            case R.id.iv_productDetails_more:
                List list = new ArrayList<String>();
                list.add("消息");
                list.add("服务社");
                list.add("购物车");
                Log.i(TAG,"更多");
                popupWindow = new MorePopupWindow(this, onItemClickListener);
                popupWindow.setList(list);
                popupWindow.show(ivProductToolbarMore, 20);
                break;
            case R.id.ll_productDetails_collect:
                // 检测网络
                if (!checkNetwork(this)) {
                    Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if(user == null){
                    //弹框显示让用户去登录
                    showWarningDialog();
                    return;
                }
                collectRequest();//收藏请求
                break;
            case R.id.ll_productDetails_comment:
                // 检测网络
                if (!checkNetwork(this)) {
                    Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if(user == null){
                    //弹框显示让用户去登录
                    showWarningDialog();
                    return;
                }
                //进行评论,暂时不做
                break;
            case R.id.ll_productDetails_add_cart:
                // 检测网络
                if (!checkNetwork(this)) {
                    Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if(user == null){
                    //弹框显示让用户去登录
                    showWarningDialog();
                    return;
                }
                //弹框选择商品属性，确认加入购物车
                Log.i(TAG,"购物车");
                showShoppingCartDialog();
                break;
            case R.id.ll_productDetails_pay:
                // 检测网络
                if (!checkNetwork(this)) {
                    Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if(user == null){
                    //弹框显示让用户去登录
                    showWarningDialog();
                    return;
                }
                //弹框选择商品属性
                initAddress();//获取用户收货地址信息
                //showPurchaseDialog();
                break;

            //警告弹话框的操作
            case R.id.warning_dialog_btn_cancel://取消按钮
                warningDialog.dismiss();
                break;
            case R.id.warning_dialog_btn_sure://确定按钮
                Log.i(TAG,"确定按钮");
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                warningDialog.dismiss();
                this.finish();
                break;

            //购买弹话框的操作
            case R.id.purchase_dialog_iv_cancel://取消图标
                purchaseDialog.dismiss();
                break;
            case R.id.purchase_dialog_btn_sure://确定按钮
                Log.i(TAG,"确定按钮");
                Intent buyIntent = new Intent(this,ConfirmOrderActivity.class);
                buyIntent.putExtra("productId",productId+";");
                buyIntent.putExtra("productBuyCount",tvProductBuyCount.getText()+";");
                startActivity(buyIntent);
                purchaseDialog.dismiss();
                this.finish();
                break;
            case R.id.purchase_dialog_product_count_add://加号图标
                if(Integer.parseInt((String)tvProductBuyCount.getText()) >= 10){
                    ToastUtils.Toast(ProductDetailsActivity.this,"限购10份",0);
                    return;
                }
                int tempAdd = Integer.parseInt((String)tvProductBuyCount.getText());
                tempAdd = tempAdd+1;
                tvProductBuyCount.setText(tempAdd+"");
                break;
            case R.id.purchase_dialog_product_count_subtract://减号图标
                if(Integer.parseInt((String)tvProductBuyCount.getText()) == 1 ){
                    return;
                }
                int tempSubtract = Integer.parseInt((String)tvProductBuyCount.getText());
                tempSubtract = tempSubtract-1;
                tvProductBuyCount.setText(tempSubtract+"");
                break;

            //加入购物车，选择商品属性弹话框的操作
            case R.id.shopping_cart_dialog_iv_cancel://取消图标
                shoppingCartDialog.dismiss();
                break;
            case R.id.shopping_cart_dialog_btn_sure://确定按钮
                Log.i(TAG,"确定按钮");
                shoppingCartDialog.dismiss();
                addShoppingCart();
                this.finish();
                break;
            case R.id.shopping_cart_dialog_product_count_add://加号图标
                if(Integer.parseInt((String)tvShoppingCartProductBuyCount.getText()) >= 10){
                    ToastUtils.Toast(ProductDetailsActivity.this,"限购10份",0);
                    return;
                }
                int cartAdd = Integer.parseInt((String)tvShoppingCartProductBuyCount.getText());
                cartAdd = cartAdd+1;
                tvShoppingCartProductBuyCount.setText(cartAdd+"");
                break;
            case R.id.shopping_cart_dialog_product_count_subtract://减号图标
                if(Integer.parseInt((String)tvShoppingCartProductBuyCount.getText()) == 1 ){
                    return;
                }
                int cartSubtract = Integer.parseInt((String)tvShoppingCartProductBuyCount.getText());
                cartSubtract = cartSubtract-1;
                tvShoppingCartProductBuyCount.setText(cartSubtract+"");
                break;
            case R.id.address_dialog_btn_cancel://取消按钮
                addressDialog.dismiss();
                break;
            case R.id.address_dialog_btn_sure://确定按钮
                Log.i(TAG,"确定按钮");
                Intent addressIntent = new Intent(this,UpdateAdressActivity.class);
                startActivity(addressIntent);
                addressDialog.dismiss();
                break;
        }

    }
    private void initAddress() {
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
                Log.d("ProductDetailsActivity","确认订单：获取数据失败了");
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
                                        ProductDetailsActivity.this, addressResult, new TypeToken<BasePojo<Purchaseaddress>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Purchaseaddress> list  = basePojo.getDatas();
                                        Log.i(TAG,"确认订单：结果"+list.toString());
                                        showPurchaseDialog();
                                    }else{
                                        showAdressDialog();
                                        //ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
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

    private void showAdressDialog() {
        addressDialog = new Dialog(ProductDetailsActivity.this, R.style.NormalDialogStyle);
        View view = View.inflate(ProductDetailsActivity.this, R.layout.address_dialog, null);
        addressBtnCancel = view.findViewById(R.id.address_dialog_btn_cancel);
        addressBtnSure = view.findViewById(R.id.address_dialog_btn_sure);

        addressDialog.setContentView(view);
        addressDialog.setCanceledOnTouchOutside(false);

        addressBtnCancel.setOnClickListener(this);
        addressBtnSure.setOnClickListener(this);
        addressDialog.show();
    }

    private void addShoppingCart() {
        // 加载创意产品列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("user.id",Integer.toString(userId));
        formBody.add("product.id",productId);
        formBody.add("productCount",tvShoppingCartProductBuyCount.getText()+"");
        Log.i(TAG,"传过去的数据"+Integer.toString(userId)+"|"+"productId"+"|"+tvShoppingCartProductBuyCount.getText()+"");
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_CREATESHOPPINGCART)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ProductDetailsActivity","产品详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"产品详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"产品详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"产品详情：结果："+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"产品详情：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ProductDetailsActivity.this, result, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        ToastUtils.Toast(ProductDetailsActivity.this,"成功加入购物车",0);
                                    }else{
                                        Log.i(TAG,"产品详情：后台传来失败了"+basePojo.getMsg());
                                       // ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
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

    private void showShoppingCartDialog() {
        shoppingCartDialog = new Dialog(ProductDetailsActivity.this, R.style.purchaseDialogStyle);
        View view = View.inflate(ProductDetailsActivity.this, R.layout.shopping_cart_dialog, null);
        shoppingCartBtnSure = view.findViewById(R.id.shopping_cart_dialog_btn_sure);
        ivShoppingCartCancel = view.findViewById(R.id.shopping_cart_dialog_iv_cancel);
        ivShoppingCartProductPicture = view.findViewById(R.id.shopping_cart_dialog_product_picture);
        ivShoppingCartProductcountAdd = view.findViewById(R.id.shopping_cart_dialog_product_count_add);
        ivShoppingCartProductcountSubtract = view.findViewById(R.id.shopping_cart_dialog_product_count_subtract);
        tvShoppingCartProductPrice = view.findViewById(R.id.shopping_cart_dialog_product_price);
        tvShoppingCartProductCount = view.findViewById(R.id.shopping_cart_dialog_product_count);
        tvShoppingCartProductBuyCount = view.findViewById(R.id.shopping_cart_dialog_product_buy_count);

        shoppingCartDialog.setContentView(view);
        shoppingCartDialog.setCanceledOnTouchOutside(false);


        shoppingCartBtnSure.setOnClickListener(this);
        ivShoppingCartCancel.setOnClickListener(this);
        ivShoppingCartProductcountAdd.setOnClickListener(this);
        ivShoppingCartProductcountSubtract.setOnClickListener(this);

        shoppingCartloadData();

        shoppingCartDialog.show();
    }

    private void shoppingCartloadData() {
        // 加载创意产品列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("queryParam.condition","id="+productId);
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_GET)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ProductDetailsActivity","产品详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"产品详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"产品详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"产品详情：结果："+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"产品详情：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ProductDetailsActivity.this, result, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getTotal() > 0){ //信息获取成功,回传有数据
                                            List<Product> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                            Log.i(TAG,"产品详情：结果"+list.toString());
                                            //处理所获得数据
                                            tvShoppingCartProductPrice.setText(list.get(0).getProductPrice()+"");
                                            tvShoppingCartProductCount.setText(list.get(0).getProductCount()+"");
                                            // 图片加载
                                            Glide.with(ProductDetailsActivity.this).load(list.get(0).getProductPicture().split(";")[0]).placeholder(R.mipmap.base).into(ivShoppingCartProductPicture);
                                        }else{
                                            Log.i(TAG,"产品详情：后台传来数据为空"+basePojo.getMsg());
                                            ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
                                        }
                                    }else{
                                        Log.i(TAG,"产品详情：后台传来失败了"+basePojo.getMsg());
                                      //  ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
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

    private void showPurchaseDialog() {
        purchaseDialog = new Dialog(ProductDetailsActivity.this, R.style.purchaseDialogStyle);
        View view = View.inflate(ProductDetailsActivity.this, R.layout.purchase_dialog, null);
        purchaseBtnSure = view.findViewById(R.id.purchase_dialog_btn_sure);
        ivCancel = view.findViewById(R.id.purchase_dialog_iv_cancel);
        ivProductPicture = view.findViewById(R.id.purchase_dialog_product_picture);
        ivProductcountAdd = view.findViewById(R.id.purchase_dialog_product_count_add);
        ivProductcountSubtract = view.findViewById(R.id.purchase_dialog_product_count_subtract);
        tvProductPrice = view.findViewById(R.id.purchase_dialog_product_price);
        tvProductCount = view.findViewById(R.id.purchase_dialog_product_count);
        tvProductBuyCount = view.findViewById(R.id.purchase_dialog_product_buy_count);

        purchaseDialog.setContentView(view);
        purchaseDialog.setCanceledOnTouchOutside(false);


        purchaseBtnSure.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivProductcountAdd.setOnClickListener(this);
        ivProductcountSubtract.setOnClickListener(this);

        loadData();

        purchaseDialog.show();
    }

    private void loadData() {
        // 加载创意产品列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("queryParam.condition","id="+productId);
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_GET)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ProductDetailsActivity","产品详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"产品详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"产品详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"产品详情：结果："+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"产品详情：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ProductDetailsActivity.this, result, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getTotal() > 0){ //信息获取成功,回传有数据
                                            List<Product> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                            Log.i(TAG,"产品详情：结果"+list.toString());
                                            //处理所获得数据
                                            tvProductPrice.setText(list.get(0).getProductPrice()+"");
                                            tvProductCount.setText(list.get(0).getProductCount()+"");
                                            // 图片加载
                                            Glide.with(ProductDetailsActivity.this).load(list.get(0).getProductPicture().split(";")[0]).placeholder(R.mipmap.base).into(ivProductPicture);
                                        }else{
                                            Log.i(TAG,"产品详情：后台传来数据为空"+basePojo.getMsg());
                                            ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
                                        }
                                    }else{
                                        Log.i(TAG,"产品详情：后台传来失败了"+basePojo.getMsg());
                                        //ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
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

    private void showWarningDialog() {
        warningDialog = new Dialog(ProductDetailsActivity.this, R.style.NormalDialogStyle);
        View view = View.inflate(ProductDetailsActivity.this, R.layout.warning_dialog, null);
        btnCancel = view.findViewById(R.id.warning_dialog_btn_cancel);
        btnSure = view.findViewById(R.id.warning_dialog_btn_sure);

        warningDialog.setContentView(view);
        warningDialog.setCanceledOnTouchOutside(false);

        btnCancel.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        warningDialog.show();
    }

    /**
     * 收藏请求？
     */
    private void collectRequest() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("user.id",Integer.toString(userId));//传递键值对参数
        formBody.add("product.id",productId);//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_COLLECT)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ProjectDetailsActivity","产品详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"产品详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"产品详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"产品详情：结果："+result);
                    //result = "{\"msg\":\"查询成功\",\"pageSize\":11,\"total\":11,\"page\":1,\"success\":true,\"datas\":[{\"companyId\":null,\"creprojectClassify\":1,\"creprojectContent\":\"这次设计的目的主要是解决婴儿车在使用过程中存在的存\\r\\n\\t\\t\\t\\t在的一些问题。例如，使用周期短，占地空间大等。在功能上使其多样化，能够有一\\r\\n\\t\\t\\t\\t个较长的使用周期陪伴婴儿的成长，在使用的舒适安全的方面也经过了设计，在减震 效果上进行了改良，三轮系的设计，使转弯更灵活。\",\"creprojectEvaluateOpinion\":\"\",\"creprojectEvaluateResult\":0,\"creprojectEvaluateTime\":\"2017-12-27\",\"creprojectLabel\":\"\",\"creprojectModifyTime\":\"2017-12-26\",\"creprojectPicture\":\"\\/SmartgenPlatform\\/img\\/sy6.png\",\"creprojectPlan\":\"\\/SmartgrnPatrform\\/web\\/file\",\"creprojectPraise\":0,\"creprojectReleaseTime\":\"2017-12-26\",\"creprojectState\":0,\"creprojectTitle\":\"多功能婴儿车\",\"creprojectVideo\":\"fgd\",\"expertJobNumber\":null,\"id\":null,\"products\":[],\"userId\":2}]}";
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"产品详情：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ProductDetailsActivity.this, result, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getTotal() > 0){ //信息获取成功,回传有数据
                                            //处理数据
                                            // 收藏
                                            ToastUtils.Toast(ProductDetailsActivity.this,"收藏",0);
                                            Glide.with(ProductDetailsActivity.this).load(R.mipmap.ic_collect_select).placeholder(R.mipmap.ic_collect_select).into(ivProductCollect);
                                        }else{//取消收藏
                                            ToastUtils.Toast(ProductDetailsActivity.this,"取消收藏",0);
                                            Glide.with(ProductDetailsActivity.this).load(R.mipmap.ic_collect_normal).placeholder(R.mipmap.ic_collect_normal).into(ivProductCollect);
                                            Log.i(TAG,"产品详情：后台传来数据为空"+basePojo.getMsg());
                                            //ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
                                        }
                                    }else{
                                        Log.i(TAG,"产品详情：后台传来失败了"+basePojo.getMsg());
                                       // ToastUtils.Toast(ProductDetailsActivity.this,basePojo.getMsg(),0);
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
    }

