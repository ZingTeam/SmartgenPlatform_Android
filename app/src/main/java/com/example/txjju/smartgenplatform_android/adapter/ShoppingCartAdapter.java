package com.example.txjju.smartgenplatform_android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.ShoppingCartActivity;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.ShoppingCartBean;
import com.example.txjju.smartgenplatform_android.pojo.Shoppingcart;
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
 * Created by Administrator on 2018/6/10.
 */

public class ShoppingCartAdapter extends BaseAdapter {

    public final static String TAG = "ShoppingCartActivity";

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }
    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }

    private boolean isShow = true;//是否显示编辑/完成
    private List<Shoppingcart> shoppingCartBeanList;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private Context context;

    public ShoppingCartAdapter(Context context) {
        this.context = context;
    }

    public void setShoppingCartBeanList(List<Shoppingcart> shoppingCartBeanList) {
        this.shoppingCartBeanList = shoppingCartBeanList;
        notifyDataSetChanged();
    }

    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getCount() {
        return shoppingCartBeanList == null ? 0 : shoppingCartBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCartBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 是否显示可编辑
     *
     * @param flag
     */
    public void isShow(boolean flag) {
        isShow = flag;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Shoppingcart shoppingCartBean = shoppingCartBeanList.get(position);
        holder.tv_commodity_name.setText(shoppingCartBean.getProductName()+"---"+shoppingCartBean.getProductMsg());
        holder.tv_price.setText("￥:" + shoppingCartBean.getProductPrice());
        holder.ck_chose.setChecked(shoppingCartBean.isChoosed());
        holder.tv_show_num.setText(shoppingCartBean.getProductCount()+" ");
        holder.tv_num.setText("X" + shoppingCartBean.getProductCount());
        // 图片加载
        Glide.with(context).load(shoppingCartBeanList.get(position).getProductPicture().split(";")[0]).placeholder(R.mipmap.base).into(holder.iv_show_pic);

        //单选框按钮
        holder.ck_chose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingCartBean.setChoosed(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                    }
                }
        );

        //增加按钮
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, holder.tv_show_num, holder.ck_chose.isChecked());//暴露增加接口
            }
        });

        //删减按钮
        holder.iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, holder.tv_show_num, holder.ck_chose.isChecked());//暴露删减接口
            }
        });


        //删除弹窗
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle("提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
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
                                modifyCountInterface.childDelete(position);//删除 从item中移除
                                deleteCart(shoppingCartBean.getId());
                            }
                        });
                alert.show();
            }
        });

        //判断是否在编辑状态下
        if (isShow) {
            //holder.tv_commodity_name.setVisibility(View.VISIBLE);
           // holder.rl_edit.setVisibility(View.GONE);
            holder.tv_delete.setVisibility(View.GONE);
        } else {
            //holder.tv_commodity_name.setVisibility(View.VISIBLE);
            //holder.rl_edit.setVisibility(View.VISIBLE);
            holder.tv_delete.setVisibility(View.VISIBLE);
        }

        return convertView;
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
                                        context, result, new TypeToken<BasePojo<Shoppingcart>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        Log.i(TAG,"删除购物车成功");
                                        ToastUtils.Toast(context,basePojo.getMsg(),0);
                                    }else{
                                        Log.i(TAG,"删除购物车详情：后台传来失败了"+basePojo.getMsg());
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


    //初始化控件
    class ViewHolder {
        ImageView iv_show_pic, iv_sub, iv_add;
        TextView tv_commodity_name, tv_price, tv_num, tv_delete, tv_show_num;
        CheckBox ck_chose;
        RelativeLayout rl_edit;

        public ViewHolder(View itemView) {
            ck_chose =  itemView.findViewById(R.id.ck_chose);
            iv_show_pic =  itemView.findViewById(R.id.iv_show_pic);
            iv_sub =  itemView.findViewById(R.id.iv_sub);
            iv_add =  itemView.findViewById(R.id.iv_add);

            tv_commodity_name =  itemView.findViewById(R.id.tv_commodity_name);
            tv_price =  itemView.findViewById(R.id.tv_price);
            tv_num =  itemView.findViewById(R.id.tv_num);
            tv_delete =  itemView.findViewById(R.id.tv_delete);
            tv_show_num =  itemView.findViewById(R.id.tv_show_num);
            rl_edit =  itemView.findViewById(R.id.rl_edit);

        }

    }

}