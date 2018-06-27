package com.example.txjju.smartgenplatform_android.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.MainActivity;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.util.GlideImageLoader;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.OnScrollViewChangeListener;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.example.txjju.smartgenplatform_android.view.CountDownView;
import com.example.txjju.smartgenplatform_android.view.ScrollViewWrapper;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

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
 * 商品详情的商品介绍页
 */
public class ProductFragment extends BaseFragment {
    private Banner banner;  // 广告栏控件
    private ScrollViewWrapper scrollViewWrapper;
    private CountDownView countdownView;
    private TextView tvProductTitle,tvProductPrice,tvProductShare,tvProductSell,tvProductProductCount,tvProductProductSell,tvProductProductBestCount,tvProductProductMiddleCount,tvProductProductBadCount;

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    private List<String> bannerImgList = new ArrayList<>();
    private String[] bannerImgs =new String[]{"","","","",""};
    private int ImgCount;
//    private List<String> bannerTitleList = new ArrayList<>();
    public final static String TAG = "ProductDetailsActivity";
    private long testTime = (2 * 24 * 3600 + 10 * 3600 + 18 * 60 + 53) * 1000;


    private OnScrollTabChangeListener listener=null;

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
                                            tvProductTitle.setText(list.get(0).getProductName());
                                            tvProductPrice.setText(list.get(0).getProductPrice()+"");
                                            tvProductProductCount.setText(list.get(0).getProductCount()+"");
                                            tvProductProductBestCount.setText("好评"+list.get(0).getProductBestCount()+"");
                                            tvProductProductMiddleCount.setText("中评"+list.get(0).getProductMiddleCount()+"");
                                            tvProductProductBadCount.setText("差评"+list.get(0).getProductBadCount()+"");
                                            if(list.get(0).getProductSell() == null){
                                                tvProductSell.setText("暂无销量");
                                                tvProductProductSell.setText("暂无销量");
                                            }else{
                                                tvProductSell.setText("热销"+list.get(0).getProductSell()+"件");
                                                tvProductProductSell.setText(list.get(0).getProductSell()+"");
                                            }
                                            //将图片分离出来放进数组
                                            ImgCount = list.get(0).getProductPicture().split(";").length;
                                            for(int i = 0;i<ImgCount;i++){
                                                bannerImgs[i] = list.get(0).getProductPicture().split(";")[i];
                                            }
                                            Log.i(TAG,"图片数量"+ImgCount);
                                            initBanner();

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

    //fragment中布局以及控件的获取都写在此方法中
    //onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_layout, container, false);
        //获取新建视图View中布局文件的控件
        initViews(view);// 获取新建视图View中布局文件的控件
        //initBanner();
        initScrooll();
        return view;
    }


    private void initViews(View view) {
        banner = view.findViewById(R.id.detail_banner);
        scrollViewWrapper = view.findViewById(R.id.scrollViewWrapper);
        tvProductTitle = view.findViewById(R.id.tv_productDetails_title);
        tvProductPrice = view.findViewById(R.id.tv_productDetails_price);
        tvProductShare = view.findViewById(R.id.tv_productDetails_share);
        tvProductSell = view.findViewById(R.id.tv_productDetails_sell);
        tvProductProductCount = view.findViewById(R.id.product_detail_productCount);
        tvProductProductSell = view.findViewById(R.id.product_detail_productSell);
        tvProductProductBestCount = view.findViewById(R.id.product_detail_productBestCount);
        tvProductProductMiddleCount = view.findViewById(R.id.product_detail_productMiddleCount);
        tvProductProductBadCount = view.findViewById(R.id.product_detail_productBadCount);
    }

    private void initBanner() {
        Log.i(TAG,"进来了");
        Log.i(TAG,"进来了吗？"+ImgCount);
        for(int i = 0;i < ImgCount;i++){
            Log.i(TAG,bannerImgs[i]+"没有进来");
            bannerImgList.add(bannerImgs[i]);
        }
        /*bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner1.jpg");*/
        /*bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner2.jpg");
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner3.jpg");*/
        /*bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner4.jpg");
        bannerImgList.add("http://p88c3g279.bkt.clouddn.com/image/banner4.jpg");*/
//        bannerTitleList.add("倾向于没有开始，直到你有一杯咖啡");
//        bannerTitleList.add("创意的价值无法用金钱衡量");
//        bannerTitleList.add("我们帮你完成成功路上的第一步");
//        bannerTitleList.add("不做咸鱼，年轻出去看看");
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.LEFT);
        banner.setImages(bannerImgList);
        // banner.setBannerTitles(bannerTitleList);
        banner.start();
    }

    private void initScrooll() {
        listener=(OnScrollTabChangeListener)getActivity();
        scrollViewWrapper.setOnScrollChangeListener(new OnScrollViewChangeListener() {
            @Override
            public void change(int tabId) {
                if (listener!=null){
                    listener.currentTab(tabId);
                }
            }
        });
    }


    //滚动的时候TabLayout的id切换
    public interface OnScrollTabChangeListener{
        void currentTab(int tabId);
    }
}
