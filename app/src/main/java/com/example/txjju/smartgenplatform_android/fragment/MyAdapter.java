package com.example.txjju.smartgenplatform_android.fragment;

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
import com.example.txjju.smartgenplatform_android.adapter.MarketProjectAdapter;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijianchang@yy.com on 2017/4/12.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Creativeproject> datas;
    private Context context;
    private int normalType = 0;
    private int footType = 1;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public MyAdapter(List<Creativeproject> datas, Context context, boolean hasMore) {
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == normalType) {
            return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_project_market, null));
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, null));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder) {
            ((MyAdapter.NormalHolder) holder).tvTitle.setText(datas.get(position).getCreprojectTitle());
            Log.i("MainActivity","title:"+datas.get(position).getCreprojectTitle()+datas.get(position).getCreprojectState());
            ((MyAdapter.NormalHolder) holder).tvAbstract.setText(datas.get(position).getCreprojectContent().substring(0,15));
            //0为未孵化，1为孵化中
            if(datas.get(position).getCreprojectState() == 0){
                ((MyAdapter.NormalHolder) holder).tvState.setText("未孵化");
                Glide.with(context).load(R.mipmap.ic_nono_fh).into( ((MyAdapter.NormalHolder) holder).ivStatePicture);
            }else{
                ((MyAdapter.NormalHolder) holder).tvState.setText("孵化中");
                Glide.with(context).load(R.mipmap.ic_now_fh).into( ((MyAdapter.NormalHolder) holder).ivStatePicture);
            }
            // 图片加载
            Glide.with(context).load(datas.get(position).getCreprojectPicture()).into( ((MyAdapter.NormalHolder) holder).ivPicture);

        } else {
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                fadeTips = false;
                if (datas.size() > 0) {
                    ((FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (datas.size() > 0) {
                    ((FootHolder) holder).tips.setText("没有更多数据了");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((FootHolder) holder).tips.setVisibility(View.GONE);
                            fadeTips = true;
                            hasMore = true;
                        }
                    }, 2000);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    public int getRealLastPosition() {
        return datas.size();
    }


    public void updateList(List<Creativeproject> newDatas, boolean hasMore) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

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

    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    public boolean isFadeTips() {
        return fadeTips;
    }

    public void resetDatas() {
        datas = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }
}
