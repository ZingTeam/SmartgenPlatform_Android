package com.example.txjju.smartgenplatform_android.adapter;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private List<Creativeproject> datas;// 数据源
    private Context context;    // 上下文Context

    private int normalType = 0;     // 第一种ViewType，正常的item
    private int footType = 1;       // 第二种ViewType，底部的提示View

    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示

    private Handler mHandler = new Handler(Looper.getMainLooper()); //获取主线程的Handler

    public MarketProjectAdapter(List<Creativeproject> datas, Context context, boolean hasMore) {
        // 初始化变量
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 根据返回的ViewType，绑定不同的布局文件，这里只有两种
        if (viewType == normalType) {
            return new MarketProjectAdapter.NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_project_market, null));
        } else {
            return new MarketProjectAdapter.FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, null));
        }
    }
    //监听item的接口
    public interface OnItemClickListener{
        void onItemClick(View view , int position);//重写方法
    }
    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // 如果是正常的imte，直接设置各个控件的值
        if (holder instanceof MarketProjectAdapter.NormalHolder) {
            setViewValue(holder,position);
            //监听item
            View itemView = ((LinearLayout) holder.itemView).getChildAt(1);
            if (mOnItemClickListener != null) {
                Log.i("MainActivity","市场项目监听进来了");
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }
            else {
            // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个footView
            ((MarketProjectAdapter.FootHolder) holder).tips.setVisibility(View.VISIBLE);
            // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
            if (hasMore == true) {
                // 不隐藏footView提示
                fadeTips = false;
                if (datas.size() > 0) {
                    // 如果查询数据发现增加之后，就显示正在加载更多
                    ((MarketProjectAdapter.FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (datas.size() > 0) {
                    // 如果查询数据发现并没有增加时，就显示没有更多数据了
                    ((MarketProjectAdapter.FootHolder) holder).tips.setText("没有更多数据了");
                    // 然后通过延时加载模拟网络请求的时间，在1000ms后执行
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 隐藏提示条
                            ((MarketProjectAdapter.FootHolder) holder).tips.setVisibility(View.GONE);
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

    private void setViewValue(final RecyclerView.ViewHolder holder, int position) {
        ((MarketProjectAdapter.NormalHolder) holder).tvTitle.setText(datas.get(position).getCreprojectTitle());
        //项目简言取项目内容里的前15个字符
        ((MarketProjectAdapter.NormalHolder) holder).tvAbstract.setText(datas.get(position).getCreprojectContent().split("。")[0]);
        //0为生活手工 1为家具家居 2为科技数码 3为艺术娱乐 4为医疗健康 5为户外运动 6为其他
        switch(datas.get(position).getCreprojectClassify()){
            case 0:
                ((MarketProjectAdapter.NormalHolder) holder).tvClassify.setText("生活手工");
                break;
            case 1:
                ((MarketProjectAdapter.NormalHolder) holder).tvClassify.setText("家具家居");
                break;
            case 2:
                ((MarketProjectAdapter.NormalHolder) holder).tvClassify.setText("科技数码");
                break;
            case 3:
                ((MarketProjectAdapter.NormalHolder) holder).tvClassify.setText("艺术娱乐");
                break;
            case 4:
                ((MarketProjectAdapter.NormalHolder) holder).tvClassify.setText("医疗健康");
                break;
            case 5:
                ((MarketProjectAdapter.NormalHolder) holder).tvClassify.setText("户外运动");
                break;
            case 6:
                ((MarketProjectAdapter.NormalHolder) holder).tvClassify.setText("其他");
                break;
        }
        if(datas.get(position).getCreprojectPraise() == 0){
            ((MarketProjectAdapter.NormalHolder) holder).tvPraise.setText("暂无");
        }else{
            ((MarketProjectAdapter.NormalHolder) holder).tvPraise.setText(datas.get(position).getCreprojectPraise()+"");
        }
        //0为未孵化，1为孵化中
        if(datas.get(position).getCreprojectState() == 0){
            ((MarketProjectAdapter.NormalHolder) holder).tvState.setText("未孵化");
            Glide.with(context).load(R.mipmap.ic_nono_fh).into( ((MarketProjectAdapter.NormalHolder) holder).ivStatePicture);
        }else{
            ((MarketProjectAdapter.NormalHolder) holder).tvState.setText("孵化中");
            Glide.with(context).load(R.mipmap.ic_now_fh).into( ((MarketProjectAdapter.NormalHolder) holder).ivStatePicture);
        }
        // 图片加载，placeholder里的为默认图片
        Glide.with(context).load(datas.get(position).getCreprojectPicture()).placeholder(R.mipmap.base).into( ((MarketProjectAdapter.NormalHolder) holder).ivPicture);
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
    public void updateList(List<Creativeproject> newDatas, boolean hasMore) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    // 正常item的ViewHolder，用以缓存findView操作
    class NormalHolder extends RecyclerView.ViewHolder {
        private ImageView ivPicture,ivStatePicture,ivDetails;
        private TextView tvTitle,tvAbstract,tvState,tvPraise,tvClassify;

        public NormalHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            ivPicture = itemView.findViewById(R.id.iv_project_picture_market);
            ivStatePicture = itemView.findViewById(R.id.iv_project_statepicture_market);
            ivDetails = itemView.findViewById(R.id.iv_project_details_market);
            tvTitle = itemView.findViewById(R.id.tv_project_title_market);
            tvAbstract = itemView.findViewById(R.id.tv_project_abstract_market);
            tvState = itemView.findViewById(R.id.tv_project_state_market);
            tvPraise = itemView.findViewById(R.id.tv_project_praise_market);
            tvClassify = itemView.findViewById(R.id.tv_project_classify_market);
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
}