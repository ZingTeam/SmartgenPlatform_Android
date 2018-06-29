package com.example.txjju.smartgenplatform_android.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.LoginActivity;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseaddress;
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
 * 收货地址--地址列表适配器
 */
public class MineUpdateAddressAdapter extends RecyclerView.Adapter<MineUpdateAddressAdapter.ViewHolder> {


    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private User user;
    private int userId;//保存用户ID
    public int productId;//保存产品ID

    private Context context;
    private List<Purchaseaddress> list;
    private MineUpdateAddressAdapter.ModifyCountInterface modifyCountInterface;

    public MineUpdateAddressAdapter(Context context , List<Purchaseaddress> list) {
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
                R.layout.item_update_address_layout, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
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
        void updateAddress(int position);
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(MineUpdateAddressAdapter.ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    //监听item的接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);//重写方法
    }
    private MineUpdateAddressAdapter.OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(MineUpdateAddressAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvUpdateAddressName.setText(list.get(position).getPuraddressUserName());
        holder.tvUpdateAddressPhone.setText(list.get(position).getPuraddressUserPhone());
        holder.tvUpdateAddressAddress.setText(list.get(position).getPuraddressAddress());
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
        holder.tvUpdateAddressUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.updateAddress(position);
                    }
                }
        );
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
    public class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvUpdateAddressName,tvUpdateAddressPhone,tvUpdateAddressAddress,tvUpdateAddressUpdate;

        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            tvUpdateAddressName = itemView.findViewById(R.id.tv_update_address_name);
            tvUpdateAddressPhone = itemView.findViewById(R.id.tv_update_address_phone);
            tvUpdateAddressAddress = itemView.findViewById(R.id.tv_update_address_address);
            tvUpdateAddressUpdate = itemView.findViewById(R.id.tv_update_address_update);
        }
    }
}



