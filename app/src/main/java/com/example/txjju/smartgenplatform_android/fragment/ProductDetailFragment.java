package com.example.txjju.smartgenplatform_android.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 */
public class ProductDetailFragment extends Fragment {

    public final static String TAG = "ProductDetailsActivity";

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    private TextView tvProductDetailsContent;
    private ImageView ivProductDetailsPicture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(); // 加载服务端数据
    }

    private void loadData() {
        Log.i(TAG,"加载数据");
        final ProgressDialog pgDialog = new ProgressDialog(getActivity());
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        // 加载创意产品列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("queryParam.condition","id="+((ProductDetailsActivity)getActivity()).productId);//fragment访问父Activity的数据
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
                                        getActivity(), result, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getTotal() > 0){ //信息获取成功,回传有数据
                                            List<Product> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                            Log.i(TAG,"产品详情：结果"+list.toString());
                                            pgDialog.dismiss();
                                            //处理所获得数据
                                            tvProductDetailsContent.setText(list.get(0).getProductMsg());
                                            Glide.with(getActivity()).load(list.get(0).getProductPicture().split(";")[0]).into(ivProductDetailsPicture);
                                        }else{
                                            Log.i(TAG,"产品详情：后台传来数据为空"+basePojo.getMsg());
                                            ToastUtils.Toast(getActivity(),basePojo.getMsg(),0);
                                            pgDialog.dismiss();//隐藏进度栏
                                        }
                                    }else{
                                        pgDialog.dismiss();
                                        Log.i(TAG,"产品详情：后台传来失败了"+basePojo.getMsg());
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

    //fragment中布局以及控件的获取都写在此方法中
    //onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pruduct_detail_detail, container, false);
        //获取新建视图View中布局文件的控件
        initViews(view);// 获取新建视图View中布局文件的控件
        return view;
    }

    private void initViews(View view) {
        tvProductDetailsContent = view.findViewById(R.id.iv_productDetails_details_content);
        ivProductDetailsPicture = view.findViewById(R.id.iv_productDetails_details_picture);
    }
}
