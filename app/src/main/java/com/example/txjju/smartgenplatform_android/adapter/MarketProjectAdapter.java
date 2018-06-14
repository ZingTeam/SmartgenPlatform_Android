package com.example.txjju.smartgenplatform_android.adapter;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创意市场--项目列表适配器
 */
public class MarketProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Creativeproject> datas; // 数据源
    private Context context;    // 上下文Context

    private int normalType = 0;     // 第一种ViewType，正常的item
    private int footType = 1;       // 第二种ViewType，底部的提示View

    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示
    private Handler mHandler = new Handler(Looper.getMainLooper());



    public MarketProjectAdapter(List<Creativeproject> datas, Context context, boolean hasMore) {
        // 初始化变量
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
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


    // 根据条目位置返回ViewType，以供onCreateViewHolder方法内获取不同的Holder
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    // 正常item的ViewHolder，用以缓存findView操作
    class NormalHolder extends RecyclerView.ViewHolder {
        private ImageView ivPicture,ivStatePicture,ivDetails;
        private TextView tvTitle,tvAbstract,tvState;

        public NormalHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            ivPicture = itemView.findViewById(R.id.iv_project_picture_market);
            ivStatePicture = itemView.findViewById(R.id.iv_project_statepicture_market);
            ivDetails = itemView.findViewById(R.id.iv_project_details_market);
            tvTitle = itemView.findViewById(R.id.tv_project_title_market);
            tvAbstract = itemView.findViewById(R.id.tv_project_abstract_market);
            tvState = itemView.findViewById(R.id.tv_project_state_market);
        }
    }

    // // 底部footView的ViewHolder，用以缓存findView操作
    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 根据返回的ViewType，绑定不同的布局文件，这里只有两种
        if (viewType == normalType) {
            return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_project_market, null));
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, null));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // 如果是正常的imte，直接设置imte控件中的值
        if (holder instanceof NormalHolder) {
            ((NormalHolder) holder).tvTitle.setText(datas.get(position).getCreprojectTitle());
            Log.i("MainActivity","title:"+datas.get(position).getCreprojectTitle()+datas.get(position).getCreprojectState());
            ((NormalHolder) holder).tvAbstract.setText(datas.get(position).getCreprojectContent().substring(0,15));
            //0为未孵化，1为孵化中
            if(datas.get(position).getCreprojectState() == 0){
                ((NormalHolder) holder).tvState.setText("未孵化");
                Glide.with(context).load(R.mipmap.ic_nono_fh).into( ((NormalHolder) holder).ivStatePicture);
            }else{
                ((NormalHolder) holder).tvState.setText("孵化中");
                Glide.with(context).load(R.mipmap.ic_now_fh).into( ((NormalHolder) holder).ivStatePicture);
            }
            // 图片加载
            Glide.with(context).load(datas.get(position).getCreprojectPicture()).into( ((NormalHolder) holder).ivPicture);
        } else {
            // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个footView
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
            if (hasMore == true) {
                // 不隐藏footView提示
                fadeTips = false;
                if (datas.size() > 0) {
                    // 如果查询数据发现增加之后，就显示正在加载更多
                    Log.i("MainActivity","正在加载更多...");
                    ((FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (datas.size() > 0) {
                    // 如果查询数据发现并没有增加时，就显示没有更多数据了
                    ((FootHolder) holder).tips.setText("没有更多数据了");
                    /*// 隐藏提示条
                    ((FootHolder) holder).tips.setVisibility(View.GONE);
                    // 将fadeTips设置true
                    fadeTips = true;
                    // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                    hasMore = true;*/

                    // 然后通过延时加载模拟网络请求的时间，在500ms后执行
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 隐藏提示条
                            ((FootHolder) holder).tips.setVisibility(View.GONE);
                            // 将fadeTips设置true
                            fadeTips = true;
                            // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                            hasMore = true;
                        }
                    }, 500);
                }
            }
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

    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<Creativeproject> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

}

    /*private Context context;
    private List<Creativeproject> list;

    public MarketProjectAdapter(Context context , List<Creativeproject> list) {
        this.context = context;
        this.list = list;
    }

    *//**
     * 创建视图，并绑定ViewHolder
     * @param parent   :
     * @param viewType :
     *//*
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建行视图
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_project_market, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
    }

    *//**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     *//*
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getCreprojectTitle());
        Log.i("MainActivity","title:"+list.get(position).getCreprojectTitle()+list.get(position).getCreprojectState());
        holder.tvAbstract.setText(list.get(position).getCreprojectContent().substring(0,15));
        //0为未孵化，1为孵化中
        if(list.get(position).getCreprojectState() == 0){
            holder.tvState.setText("未孵化");
            Glide.with(context).load(R.mipmap.ic_nono_fh).into(holder.ivStatePicture);
        }else{
            holder.tvState.setText("孵化中");
            Glide.with(context).load(R.mipmap.ic_now_fh).into(holder.ivStatePicture);
        }
        // 图片加载
        Glide.with(context).load(list.get(position).getCreprojectPicture()).into(holder.ivPicture);
    }

    *//**
     * 配置列表行数
     *//*
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    *//**
     * 定义行布局中的控件，并获取控件
     *//*
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPicture,ivStatePicture,ivDetails;
        private TextView tvTitle,tvAbstract,tvState;
        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            ivPicture = itemView.findViewById(R.id.iv_project_picture_market);
            ivStatePicture = itemView.findViewById(R.id.iv_project_statepicture_market);
            ivDetails = itemView.findViewById(R.id.iv_project_details_market);
            tvTitle = itemView.findViewById(R.id.tv_project_title_market);
            tvAbstract = itemView.findViewById(R.id.tv_project_abstract_market);
            tvState = itemView.findViewById(R.id.tv_project_state_market);
        }
    }
}*/

