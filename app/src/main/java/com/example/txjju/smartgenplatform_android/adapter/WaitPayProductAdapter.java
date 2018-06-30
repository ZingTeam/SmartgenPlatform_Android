package com.example.txjju.smartgenplatform_android.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseitem;
import com.example.txjju.smartgenplatform_android.util.SPUtil;

import java.util.List;

/**
 * 首页产品列表适配器
 */
public class WaitPayProductAdapter extends RecyclerView.Adapter<WaitPayProductAdapter.ViewHolder> {

    private Context context;
    private List<Purchaseitem> list;
    private WaitPayProductAdapter.ModifyCountInterface modifyCountInterface;

    public WaitPayProductAdapter(Context context , List<Purchaseitem> list) {
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
                R.layout.item_order_wait_layout, parent, false);
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
        void payOrder(int position);
        void cancelOrder(int position);
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(WaitPayProductAdapter.ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    //监听item的接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);//重写方法
    }
    private WaitPayProductAdapter.OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(WaitPayProductAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i("Con","进来了几次");
        holder.tvProductTitle.setText(list.get(position).getPurchaseitemName());
        holder.tvProductPrice.setText("￥"+list.get(position).getPurchaseitemSinglePrice()+"");
        holder.tvItemTotalPrice.setText(("￥"+list.get(position).getPurchaseitemPrice()+" (包邮)"));
        holder.tvProductBuyCount.setText("X "+list.get(position).getPurchaseitemCount()+"");
        holder.tvOrderCount.setText("共"+list.get(position).getPurchaseitemCount()+"件商品");
        // 图片加载
        Glide.with(context).load(list.get(position).getPurchaseitemPicture().split(";")[0]).placeholder(R.mipmap.base).into(holder.ivProductPicture);
        //监听item
        View itemView = ((LinearLayout) holder.itemView).getChildAt(1);
        if (mOnItemClickListener != null) {
            Log.i("MainActivity","未付款监听进来了");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        holder.tvCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog alert = new AlertDialog.Builder(context).create();
                        alert.setTitle("提示");
                        alert.setMessage("您确定要取消订单吗？");
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
                                        modifyCountInterface.cancelOrder(position);
                                    }
                                });
                        alert.show();
                    }
                }
        );
        holder.tvSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.payOrder(position);
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductPicture;
        private TextView tvProductTitle, tvProductPrice,tvProductBuyCount,tvItemTotalPrice,tvOrderCount,tvCancel,tvSubmit;
        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            ivProductPicture = itemView.findViewById(R.id.iv_item_order_pic);
            tvProductTitle = itemView.findViewById(R.id.tv_item_order_title);
            tvProductPrice = itemView.findViewById(R.id.tv_order_unit_price);
            tvProductBuyCount = itemView.findViewById(R.id.tv_order_num);
            tvOrderCount = itemView.findViewById(R.id.tv_order_count);
            tvItemTotalPrice = itemView.findViewById(R.id.tv_item_order_total_price);
            tvCancel = itemView.findViewById(R.id.tv_item_order_cancel);
            tvSubmit = itemView.findViewById(R.id.tv_item_order_submit);
        }
    }
}

