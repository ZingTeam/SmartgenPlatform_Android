package com.example.txjju.smartgenplatform_android.adapter;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;
import com.example.txjju.smartgenplatform_android.pojo.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * 众智商城--商品列表适配器
 */
public class StoreProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Product> datas;// 数据源
    private Context context;    // 上下文Context

    private int normalType = 0;     // 第一种ViewType，正常的item
    private int footType = 1;       // 第二种ViewType，底部的提示View

    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示

    private int productGrade = 0;//产品星级评定

    private Handler mHandler = new Handler(Looper.getMainLooper()); //获取主线程的Handler

    public StoreProductAdapter(List<Product> datas, Context context, boolean hasMore) {
        // 初始化变量
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 根据返回的ViewType，绑定不同的布局文件，这里只有两种
        if (viewType == normalType) {
            return new StoreProductAdapter.NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_product_store, null));
        } else {
            return new StoreProductAdapter.FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, null));
        }
    }
    //监听item的接口
    public interface OnItemClickListener{
        void onItemClick(View view , int position);//重写方法
    }
    private StoreProductAdapter.OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(StoreProductAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // 如果是正常的imte，直接设置各个控件的值
        if (holder instanceof StoreProductAdapter.NormalHolder) {
            setViewValue(holder,position);
            //监听item
            View itemView = ((LinearLayout) holder.itemView).getChildAt(1);
            if (mOnItemClickListener != null) {
                Log.i("MainActivity","商场产品监听进来了");
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        } else {
            // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个footView
            ((StoreProductAdapter.FootHolder) holder).tips.setVisibility(View.VISIBLE);
            // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
            if (hasMore == true) {
                // 不隐藏footView提示
                fadeTips = false;
                if (datas.size() > 0) {
                    // 如果查询数据发现增加之后，就显示正在加载更多
                    ((StoreProductAdapter.FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (datas.size() > 0) {
                    // 如果查询数据发现并没有增加时，就显示没有更多数据了
                    ((StoreProductAdapter.FootHolder) holder).tips.setText("没有更多数据了");
                    // 然后通过延时加载模拟网络请求的时间，在1000ms后执行
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 隐藏提示条
                            ((StoreProductAdapter.FootHolder) holder).tips.setVisibility(View.GONE);
                            // 将fadeTips设置true
                            fadeTips = true;
                            // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                            hasMore = true;
                        }
                    }, 1000);
                }
            }
        }
    }

    //设置viewType为正常即normal时，对控件的赋值
    private void setViewValue(final RecyclerView.ViewHolder holder, int position) {

        Glide.with(context).load(R.mipmap.ic_star).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
        Glide.with(context).load(R.mipmap.ic_star).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
        Glide.with(context).load(R.mipmap.ic_star).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar3);
        Glide.with(context).load(R.mipmap.ic_star).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar4);
        Glide.with(context).load(R.mipmap.ic_star).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar5);
        //产品名和产品简介
        ((StoreProductAdapter.NormalHolder) holder).tvTitle.setText(datas.get(position).getProductName());
        ((StoreProductAdapter.NormalHolder) holder).tvOneMsg.setText(datas.get(position).getProductOneMsg());
        //产品价格
        ((StoreProductAdapter.NormalHolder) holder).tvPrice.setText(datas.get(position).getProductPrice()+"");
        // 图片加载
        Glide.with(context).load(datas.get(position).getProductPicture().split(";")[0]).placeholder(R.mipmap.base).into(((StoreProductAdapter.NormalHolder) holder).ivPicture);
        //根据产品好评数，来计算出产品等级
        if(Integer.parseInt(datas.get(position).getProductBestCount())<100){
            productGrade = 0;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=100&&Integer.parseInt(datas.get(position).getProductBestCount())<200){
            productGrade = 1;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=200&&Integer.parseInt(datas.get(position).getProductBestCount())<300){
            productGrade = 2;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=300&&Integer.parseInt(datas.get(position).getProductBestCount())<400){
            productGrade = 3;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=400&&Integer.parseInt(datas.get(position).getProductBestCount())<500){
            productGrade = 4;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=500){
            productGrade = 5;
        }

        //通过好评分数来判断：显示星星状态和分数
        switch (productGrade){
            case 0:
                ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText("尚无评论");
                break;
            case 1:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                break;
            case 2:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
                ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                break;
            case 3:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar3);
                ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                break;
            case 4:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar3);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar4);
                ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                break;
            case 5:
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar3);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar4);
                Glide.with(context).load(R.mipmap.ic_star_checke).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar5);
                ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                break;
        }
        //通过产品购买状态，来显示
        if(datas.get(position).getProductStatus() ==0){
            ((StoreProductAdapter.NormalHolder) holder).tvSellState.setText("预售");
        }else if(datas.get(position).getProductStatus() ==1){
            ((StoreProductAdapter.NormalHolder) holder).tvSellState.setText("现购");
            //((StoreProductAdapter.NormalHolder) holder).tvSellState.setTextColor(Color.parseColor("#0fe9c3"));
        }else{
            ((StoreProductAdapter.NormalHolder) holder).tvSellState.setText("下架商品");
            ((StoreProductAdapter.NormalHolder) holder).tvTitle.setTextColor(Color.parseColor("#B3B3B3"));
            ((StoreProductAdapter.NormalHolder) holder).tvPrice.setTextColor(Color.parseColor("#B3B3B3"));
            ((StoreProductAdapter.NormalHolder) holder).tvYuan.setTextColor(Color.parseColor("#B3B3B3"));
            ((StoreProductAdapter.NormalHolder) holder).tvSellState.setBackgroundColor(Color.parseColor("#f8f8f8"));
            ((StoreProductAdapter.NormalHolder) holder).tvSellState.setTextColor(Color.parseColor("#B3B3B3"));
            ((StoreProductAdapter.NormalHolder) holder).llAll.setBackgroundResource(R.color.c1);
            //通过好评分数来判断：显示星星状态和分数
            switch (productGrade){
                case 0:
                    ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText("尚无评论");
                    break;
                case 1:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                    ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                    break;
                case 2:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
                    ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                    break;
                case 3:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar3);
                    ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                    break;
                case 4:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar3);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar4);
                    ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                    break;
                case 5:
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar1);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar2);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar3);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar4);
                    Glide.with(context).load(R.mipmap.ic_star_gray).into(((StoreProductAdapter.NormalHolder) holder).ivClassStar5);
                    ((StoreProductAdapter.NormalHolder) holder).tvGrade.setText(productGrade+".0分");
                    break;
            }
        }
    }

    // 获取条目数量，之所以要加1是因为增加了一条footView
    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    // 自定义方法，获取列表中数据源的最后一个位置，比getItemCount少1，因为不计上footView
    public int getRealLastPosition() {
        return datas.size();
    }

    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<Product> newDatas, boolean hasMore) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    // 正常item的ViewHolder，用以缓存findView操作
    class NormalHolder extends RecyclerView.ViewHolder {
        private LinearLayout llAll;
        private ImageView ivPicture,ivClassStar1,ivClassStar2,ivClassStar3,ivClassStar4,ivClassStar5;
        private TextView tvSellState,tvTitle,tvPrice,tvYuan,tvGrade,tvOneMsg;

        public NormalHolder(View itemView) {
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
            tvOneMsg = itemView.findViewById(R.id.tv_product_oneMsg_store);
            tvPrice = itemView.findViewById(R.id.tv_product_price_store);
            tvYuan = itemView.findViewById(R.id.tv_yuan);
            tvGrade = itemView.findViewById(R.id.tv_product_grade_store);
        }
    }

    // 底部footView的ViewHolder，用以缓存findView操作
    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    // 暴露接口，改变fadeTips的方法
    public boolean isFadeTips() {
        return fadeTips;
    }

    // 暴露接口，下拉刷新时，通过暴露方法将数据源置为空
    public void resetDatas() {
        datas = new ArrayList<>();
    }

    // 根据条目位置返回ViewType，以供onCreateViewHolder方法内获取不同的Holder
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    /**
     * 创建视图，并绑定ViewHolder
     * @param parent   :
     * @param viewType :

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建行视图
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_product_store, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
    }*/

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    /*@Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //产品名和产品简介
        holder.tvTitle.setText(datas.get(position).getProductName()+"--"+list.get(position).getProductMsg());
        //产品价格
        holder.tvPrice.setText(datas.get(position).getProductPrice()+"");
        // 图片加载
        Glide.with(context).load(datas.get(position).getProductPicture()).into(holder.ivPicture);
        //根据产品好评数，来计算出产品等级
        if(Integer.parseInt(datas.get(position).getProductBestCount())<200){
            productGrade = 0;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=200&&Integer.parseInt(list.get(position).getProductBestCount())<400){
            productGrade = 1;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=400&&Integer.parseInt(list.get(position).getProductBestCount())<600){
            productGrade = 2;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=600&&Integer.parseInt(list.get(position).getProductBestCount())<800){
            productGrade = 3;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=800&&Integer.parseInt(list.get(position).getProductBestCount())<1000){
            productGrade = 4;
        }
        if(Integer.parseInt(datas.get(position).getProductBestCount())>=1000){
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
        if(datas.get(position).getProductStatus() ==0){
            holder.tvSellState.setText("预售");
        }else if(datas.get(position).getProductStatus() ==1){
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
    }*/
    /**
     * 定义行布局中的控件，并获取控件

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
    }*/
}

