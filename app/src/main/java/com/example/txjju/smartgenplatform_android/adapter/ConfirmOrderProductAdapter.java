package com.example.txjju.smartgenplatform_android.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.pojo.Product;

import java.util.List;

/**
 * 首页产品列表适配器
 */
public class ConfirmOrderProductAdapter extends RecyclerView.Adapter<ConfirmOrderProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> list;

    public ConfirmOrderProductAdapter(Context context , List<Product> list) {
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
                R.layout.item_confirm_order_layout, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i("Con","进来了几次");
        holder.tvProductTitle.setText(list.get(position).getProductName());
        holder.tvProductPrice.setText(list.get(position).getProductPrice()+"");
        holder.tvItemTotal.setText((list.get(position).getProductPrice()*Integer.parseInt(list.get(position).getProductBuyCount()))+"");
        holder.tvProductBuyCount.setText(list.get(position).getProductBuyCount());
        // 图片加载
        Glide.with(context).load(list.get(position).getProductPicture().split(";")[0]).placeholder(R.mipmap.base).into(holder.ivProductPicture);
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
        private TextView tvProductTitle, tvProductPrice,tvProductBuyCount,tvItemTotal;
        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            ivProductPicture = itemView.findViewById(R.id.iv_product_picture_confirm_order);
            tvProductTitle = itemView.findViewById(R.id.tv_product_title_confirm_order);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price_confirm_order);
            tvProductBuyCount = itemView.findViewById(R.id.tv_product_buy_count_confirm_order);
            tvItemTotal = itemView.findViewById(R.id.tv_item_total);
        }
    }
}

