package com.example.txjju.smartgenplatform_android.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.pojo.Product;

import java.util.List;

/**
 * 众智商城--商品列表适配器
 */
public class StoreProductAdapter extends RecyclerView.Adapter<StoreProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> list;
    private int productGrade;

    public StoreProductAdapter(Context context , List<Product> list) {
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
                R.layout.item_product_store, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //产品名和产品简介
        holder.tvTitle.setText(list.get(position).getProductName()+"--"+list.get(position).getProductMsg());
        //产品价格
        holder.tvPrice.setText(list.get(position).getProductPrice()+"");
        // 图片加载
        Glide.with(context).load(list.get(position).getProductPicture()).into(holder.ivPicture);
        //根据产品好评数，来计算出产品等级
        if(Integer.parseInt(list.get(position).getProductBestCount())<200){
            productGrade = 0;
        }
        if(Integer.parseInt(list.get(position).getProductBestCount())>=200&&Integer.parseInt(list.get(position).getProductBestCount())<400){
            productGrade = 1;
        }
        if(Integer.parseInt(list.get(position).getProductBestCount())>=400&&Integer.parseInt(list.get(position).getProductBestCount())<600){
            productGrade = 2;
        }
        if(Integer.parseInt(list.get(position).getProductBestCount())>=600&&Integer.parseInt(list.get(position).getProductBestCount())<800){
            productGrade = 3;
        }
        if(Integer.parseInt(list.get(position).getProductBestCount())>=800&&Integer.parseInt(list.get(position).getProductBestCount())<1000){
            productGrade = 4;
        }
        if(Integer.parseInt(list.get(position).getProductBestCount())>=1000){
            productGrade = 5;
        }

        //通过好评分数来判断：显示星星状态和分数
        switch (productGrade){
            case 0:
                holder.tvGrade.setText("尚无评论");
                break;
            case 1:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar1);
                holder.tvGrade.setText(productGrade+".0分");
                break;
            case 2:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar1);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar2);
                holder.tvGrade.setText(productGrade+".0分");
                break;
            case 3:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar1);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar2);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar3);
                holder.tvGrade.setText(productGrade+".0分");
                break;
            case 4:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar1);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar2);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar3);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar4);
                holder.tvGrade.setText(productGrade+".0分");
                break;
            case 5:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar1);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar2);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar3);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar4);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(holder.ivClassStar5);
                holder.tvGrade.setText(productGrade+".0分");
                break;
        }
        //通过产品购买状态，来显示
        if(list.get(position).getProductStatus() ==0){
            holder.tvSellState.setText("预售");
        }else if(list.get(position).getProductStatus() ==1){
            holder.tvSellState.setText("现购");
            holder.tvSellState.setTextColor(Color.parseColor("#0fe9c3"));
        }else{
            holder.tvSellState.setText("下架商品");
            holder.tvTitle.setTextColor(Color.parseColor("#B3B3B3"));
            holder.tvPrice.setTextColor(Color.parseColor("#B3B3B3"));
            holder.tvYuan.setTextColor(Color.parseColor("#B3B3B3"));
            holder.tvSellState.setBackgroundColor(Color.parseColor("#f8f8f8"));
            holder.tvSellState.setTextColor(Color.parseColor("#B3B3B3"));
            holder.llAll.setBackgroundResource(R.color.c1);
            //通过好评分数来判断：显示星星状态和分数
            switch (productGrade){
                case 0:
                    holder.tvGrade.setText("尚无评论");
                    break;
                case 1:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar1);
                    holder.tvGrade.setText(productGrade+".0分");
                    break;
                case 2:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar1);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar2);
                    holder.tvGrade.setText(productGrade+".0分");
                    break;
                case 3:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar1);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar2);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar3);
                    holder.tvGrade.setText(productGrade+".0分");
                    break;
                case 4:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar1);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar2);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar3);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar4);
                    holder.tvGrade.setText(productGrade+".0分");
                    break;
                case 5:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar1);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar2);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar3);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar4);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(holder.ivClassStar5);
                    holder.tvGrade.setText(productGrade+".0分");
                    break;
            }
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llAll;
        private ImageView ivPicture,ivClassStar1,ivClassStar2,ivClassStar3,ivClassStar4,ivClassStar5;
        private TextView tvSellState,tvTitle,tvPrice,tvYuan,tvGrade;
        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            llAll = itemView.findViewById(R.id.ll_all_store);
            ivPicture = itemView.findViewById(R.id.iv_product_picture_store);
            ivClassStar1 = itemView.findViewById(R.id.iv_class_star1_store);
            ivClassStar2 = itemView.findViewById(R.id.iv_class_star2_store);
            ivClassStar3 = itemView.findViewById(R.id.iv_class_star3_store);
            ivClassStar4 = itemView.findViewById(R.id.iv_class_star4_store);
            ivClassStar5 = itemView.findViewById(R.id.iv_class_star5_store);
            tvSellState = itemView.findViewById(R.id.tv_sell_state_store);
            tvTitle = itemView.findViewById(R.id.tv_product_title_store);
            tvPrice = itemView.findViewById(R.id.tv_product_price_store);
            tvYuan = itemView.findViewById(R.id.tv_yuan);
            tvGrade = itemView.findViewById(R.id.tv_product_grade_store);
        }
    }
}

