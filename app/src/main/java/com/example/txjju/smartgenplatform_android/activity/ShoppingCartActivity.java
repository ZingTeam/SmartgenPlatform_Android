package com.example.txjju.smartgenplatform_android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.ShoppingCartAdapter;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.ShoppingCartBean;
import com.example.txjju.smartgenplatform_android.pojo.Shoppingcart;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
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


public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener
        ,ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface {

    public final static String TAG = "ShoppingCartActivity";

    public TextView tv_title, tv_settlement, tv_show_price;
    private TextView tv_all_check;
    private CheckBox ck_all;
    private ListView list_shopping_cart;
    private ImageView iv_shoppingCart_back;
    private ShoppingCartAdapter shoppingCartAdapter;
    private TextView tv_edit;
    private boolean flag = false;
    private List<Shoppingcart> shoppingCartList = new ArrayList<>();
    private boolean mSelect;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量

    private User user;
    private int userId;//保存用户ID
    public String productId;//保存产品ID
    private String productIds = "",productBuyCounts = "";

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        user = SPUtil.getUser(ShoppingCartActivity.this);
        userId = user.getId();
        initView();
        initData();
    }
    /**
     * 批量模式下，用来记录当前选中状态
     */
    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();


    protected int getLayout() {
        return R.layout.activity_shopping_cart;
    }


    protected void initView() {
        list_shopping_cart = findViewById(R.id.list_shopping_cart);
        ck_all = findViewById(R.id.ck_all);
        ck_all.setOnClickListener(this);
        iv_shoppingCart_back=findViewById(R.id.iv_shoppingCart_back);
        iv_shoppingCart_back.setOnClickListener(this);
        tv_show_price = findViewById(R.id.tv_show_price);
        tv_settlement = findViewById(R.id.tv_settlement);
        tv_settlement.setOnClickListener(this);
        tv_edit = findViewById(R.id.tv_shoppingCart_edit);
        tv_edit.setOnClickListener(this);
        shoppingCartAdapter = new ShoppingCartAdapter(this);
        shoppingCartAdapter.setCheckInterface(this);
        shoppingCartAdapter.setModifyCountInterface(this);
        list_shopping_cart.setAdapter(shoppingCartAdapter);
    }

    protected void initData() {
        final ProgressDialog pgDialog = new ProgressDialog(ShoppingCartActivity.this);
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("user.id",Integer.toString(userId));
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_GETCARTLIST)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ShoppingCartActivity","购物车详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"购物车详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"购物车详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"购物车详情：结果："+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"购物车详情：测试");
                            BasePojo<Shoppingcart> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ShoppingCartActivity.this, result, new TypeToken<BasePojo<Shoppingcart>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getTotal() > 0){ //信息获取成功,回传有数据
                                            List<Shoppingcart> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                            Log.i(TAG,"购物车详情：结果"+list.toString());
                                            //处理所获得数据
                                            shoppingCartList.clear();
                                            pgDialog.dismiss();//隐藏进度栏
                                            shoppingCartList.addAll(list);
                                            shoppingCartAdapter.setShoppingCartBeanList(shoppingCartList);
                                        }else{
                                            Log.i(TAG,"购物车详情：后台传来数据为空"+basePojo.getMsg());
                                            ToastUtils.Toast(ShoppingCartActivity.this,basePojo.getMsg(),0);
                                        }
                                    }else{
                                        Log.i(TAG,"购物车详情：后台传来失败了"+basePojo.getMsg());
                                        ToastUtils.Toast(ShoppingCartActivity.this,basePojo.getMsg(),0);
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

        /*for (int i = 0; i < 6; i++) {
            ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
            shoppingCartBean.setShoppingName("树叶回收垃圾桶——方便实惠，你值得拥有");
            shoppingCartBean.setPrice(25.0);
            shoppingCartBean.setCount(1);
            shoppingCartBean.getPicture();
            shoppingCartBeanList.add(shoppingCartBean);
        }*/
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shoppingCart_back:
                this.finish();
                break;
            case R.id.tv_settlement://结算
                settlement();
                break;
            //全选按钮
            case R.id.ck_all:
                if (shoppingCartList.size() != 0) {
                    if (ck_all.isChecked()) {
                        for (int i = 0; i < shoppingCartList.size(); i++) {
                            shoppingCartList.get(i).setChoosed(true);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < shoppingCartList.size(); i++) {
                            shoppingCartList.get(i).setChoosed(false);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.tv_shoppingCart_edit:
                flag = !flag;
                if (flag) {
                    tv_edit.setText("完成");
                    shoppingCartAdapter.isShow(false);
                } else {
                    tv_edit.setText("编辑");
                    shoppingCartAdapter.isShow(true);
                }
                break;
        }
    }

    private void settlement() {
        for (Shoppingcart group : shoppingCartList) {
            if (group.isChoosed()){//选中
                deleteCart(group.getId());
                productIds += group.getId()+";";
                productBuyCounts +=group.getProductCount()+";";
            }
        }
        Log.i(TAG,"购物车传递的数据"+productIds+"|"+productBuyCounts);
        Intent buyIntent = new Intent(this,ConfirmOrderActivity.class);
        buyIntent.putExtra("productId",productIds);
        buyIntent.putExtra("productBuyCount",productBuyCounts);
        startActivity(buyIntent);
        ShoppingCartActivity.this.finish();
    }

    private void deleteCart(int cartProductId) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("id",Integer.toString(cartProductId));
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
                                        ShoppingCartActivity.this, result, new TypeToken<BasePojo<Shoppingcart>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        Log.i(TAG,"删除购物车成功");
                                    }else{
                                        Log.i(TAG,"删除购物车详情：后台传来失败了"+basePojo.getMsg());
                                        ToastUtils.Toast(ShoppingCartActivity.this,basePojo.getMsg(),0);
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
     * 单选
     *
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {

        shoppingCartList.get(position).setChoosed(isChecked);

        if (isAllCheck())
            ck_all.setChecked(true);
        else
            ck_all.setChecked(false);

        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }


    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {

        for (Shoppingcart group : shoppingCartList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shoppingCartList.size(); i++) {
            Shoppingcart shoppingCartBean = shoppingCartList.get(i);
            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                totalPrice += shoppingCartBean.getProductPrice() * shoppingCartBean.getProductCount();
            }
        }
        tv_show_price.setText("合计:" + totalPrice);
        tv_settlement.setText("结算(" + totalCount + ")");
    }

    /**
     * 增加
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        Shoppingcart shoppingCartBean = shoppingCartList.get(position);
        int currentCount = shoppingCartBean.getProductCount();
        currentCount++;
        shoppingCartBean.setProductCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        Shoppingcart shoppingCartBean = shoppingCartList.get(position);
        int currentCount = shoppingCartBean.getProductCount();
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        shoppingCartBean.setProductCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();

    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void childDelete(int position) {
        shoppingCartList.remove(position);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();

    }


}