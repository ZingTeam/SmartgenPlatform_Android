package com.example.txjju.smartgenplatform_android.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.pojo.Product;

import java.util.List;

/**
 * 创意市场--项目列表适配器
 */
public class MineCollectProductAdapter extends RecyclerView.Adapter<MineCollectProductAdapter.ViewHolder> {

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

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getProductName()+"--"+list.get(position).getProductMsg());
        holder.tvPrice.setText(list.get(position).getProductPrice()+"");
        // 图片加载
        Glide.with(context).load(list.get(position).getProductPicture()).into(holder.ivPicture);
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
        private ImageView ivPicture;
        private TextView tvTitle,tvPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            ivPicture = itemView.findViewById(R.id.iv_collect_product_picture_mine);
            tvTitle = itemView.findViewById(R.id.tv_collect_product_title_mine);
            tvPrice = itemView.findViewById(R.id.tv_collect_product_price_mine);
        }
    }
}

