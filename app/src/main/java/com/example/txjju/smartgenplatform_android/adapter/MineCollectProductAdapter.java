package com.example.txjju.smartgenplatform_android.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.pojo.Product;

import java.util.List;

/**
 * 创意市场--产品列表适配器
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
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
        private TextView tvTitle,tvClassify,tvPrice;
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

