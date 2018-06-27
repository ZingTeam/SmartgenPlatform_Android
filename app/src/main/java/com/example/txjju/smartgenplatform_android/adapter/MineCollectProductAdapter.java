package com.example.txjju.smartgenplatform_android.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.Shoppingcart;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
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
 * 创意市场--产品列表适配器
 */
public class MineCollectProductAdapter extends RecyclerView.Adapter<MineCollectProductAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private User user;
    private int userId;//保存用户ID
    public int productId;//保存产品ID
    private MineCollectProductAdapter.ModifyCountInterface modifyCountInterface;

    private Context context;
    private List<Product> list;

    public MineCollectProductAdapter(Context context , List<Product> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 创建视图，并绑定ViewHolder
     * @param parent   :
     * @param viewType :
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建行视图
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_collect_product_mine, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
    }

    @Override
    public void onClick(View view) {
    }
    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(MineCollectProductAdapter.ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    //监听item的接口
    public interface OnItemClickListener{
        void onItemClick(View view , int position);//重写方法
    }
    private MineCollectProductAdapter.OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(MineCollectProductAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).getProductName());
        //0为生活手工 1为家具家居 2为科技数码 3为艺术娱乐 4为医疗健康 5为户外运动 6为其他
       /* Log.i("MainActivity","分类"+list.get(position).getProductClassify());
        switch((Integer)list.get(position).getProductClassify()){
            case 0:
                holder.tvClassify.setText("生活手工");
                break;
            case 1:
                holder.tvClassify.setText("家具家居");
                break;
            case 2:
                holder.tvClassify.setText("科技数码");
                break;
            case 3:
                holder.tvClassify.setText("艺术娱乐");
                break;
            case 4:
                holder.tvClassify.setText("医疗健康");
                break;
            case 5:
                holder.tvClassify.setText("户外运动");
                break;
            case 6:
                holder.tvClassify.setText("其他");
                break;
        }*/
        holder.tvPrice.setText(list.get(position).getProductPrice()+"");
        // 图片加载
        Glide.with(context).load(list.get(position).getProductPicture().split(";")[0]).placeholder(R.mipmap.base).into(holder.ivPicture);
        //监听item
        View itemView = ((LinearLayout) holder.itemView).getChildAt(1);
        if (mOnItemClickListener != null) {
            Log.i("MainActivity","首页产品监听进来了");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        holder.btn_details.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user = SPUtil.getUser(context);
                        if(user != null){
                            userId = user.getId();
                        }
                        productId = list.get(position).getId();
                        AlertDialog alert = new AlertDialog.Builder(context).create();
                        alert.setTitle("提示");
                        alert.setMessage("您确定要取消收藏吗？");
                        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                        alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        modifyCountInterface.childDelete(position);//删除 目前只是从item中移除
                                        deteleCollectProduct();
                                    }
                                });
                        alert.show();
                    }
                }
        );
    }

    private void deteleCollectProduct() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("user.id",Integer.toString(userId));//传递键值对参数
        formBody.add("product.id",Integer.toString(productId));//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.PRODUCT_COLLECT)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MainActivity","删除产品收藏详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"删除产品收藏详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"删除产品收藏详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"删除产品收藏详情：结果："+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"删除产品收藏详情：测试");
                            BasePojo<Shoppingcart> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        context, result, new TypeToken<BasePojo<Shoppingcart>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        Log.i(TAG,"取消收藏收藏详情");
                                        ToastUtils.Toast(context,"取消收藏",0);
                                    }else{
                                        Log.i(TAG,"删除产品收藏详情：后台传来失败了"+basePojo.getMsg());
                                        ToastUtils.Toast(context,basePojo.getMsg(),0);
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
     * 配置列表行数
     */
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * 定义行布局中的控件，并获取控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button btn_details;
        private ImageView ivPicture;
        private TextView tvTitle, tvClassify, tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            btn_details = itemView.findViewById(R.id.btn_collect_prouct_details_mine);
            ivPicture = itemView.findViewById(R.id.iv_collect_product_picture_mine);
            tvTitle = itemView.findViewById(R.id.tv_collect_product_title_mine);
            tvClassify = itemView.findViewById(R.id.tv_collect_product_classify_mine);
            tvPrice = itemView.findViewById(R.id.tv_collect_product_price_mine);
            btn_details.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }
}



