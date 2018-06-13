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

import java.util.List;

/**
 * 创意市场--项目列表适配器
 */
public class MineCollectProjectAdapter extends RecyclerView.Adapter<MineCollectProjectAdapter.ViewHolder> {

    private Context context;
    private List<Creativeproject> list;

    public MineCollectProjectAdapter(Context context , List<Creativeproject> list) {
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
                R.layout.item_collect_project_mine, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getCreprojectTitle());
        holder.tvTime.setText(list.get(position).getCreprojectReleaseTime()+"");
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
        private ImageView ivPicture,ivStatePicture,ivDetails;
        private TextView tvTitle,tvTime,tvState;
        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            ivPicture = itemView.findViewById(R.id.iv_collect_project_mine);
            ivStatePicture = itemView.findViewById(R.id.iv_collect_project_stateimg_mine);
            ivDetails = itemView.findViewById(R.id.iv_collect_project_details_mine);
            tvTitle = itemView.findViewById(R.id.tv_collect_project_title_mine);
            tvTime = itemView.findViewById(R.id.tv_collect_project_time_mine);
            tvState = itemView.findViewById(R.id.tv_collect_project_state_mine);
        }
    }
}

