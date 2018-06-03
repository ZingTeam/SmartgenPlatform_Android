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
import com.example.txjju.smartgenplatform_android.pojo.CreativeProject;

import java.util.List;

/**
 * Created by Administrator on 2018/6/3.
 */

public class CreativeProjectAdapter extends RecyclerView.Adapter<CreativeProjectAdapter.ViewHolder> {

    private Context context;
    private List<CreativeProject> list;

    public CreativeProjectAdapter(Context context , List<CreativeProject> list) {
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
                R.layout.rv_item_creativeproject_mine, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
    }
/**
 * 配置行布局中控件的数据
 * @param holder
 * @param position
 */
        @Override
        public void onBindViewHolder (CreativeProjectAdapter.ViewHolder holder,int position){
            holder.tvCreprojectTitle.setText(list.get(position).getCreproject_title());
            holder.tvCreprojectState.setText(list.get(position).getCreproject_state());
            holder.tvCreprojectPraise.setText(list.get(position).getCreproject_praise());
            // 图片加载
            Glide.with(context).load(list.get(position).getCreproject_picture()).into(holder.ivCreprojectPicture);
        }
    /**
     * 配置列表行数
     */
        @Override
        public int getItemCount () {
            return list == null ? 0 : list.size();
        }

        /**
         * 定义行布局中的控件，并获取控件
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivCreprojectPicture,ivFhIcon,ivDetails,ivDianzan;
            private TextView tvCreprojectTitle, tvCreprojectState, tvCreprojectPraise;
            public int iv;

            public ViewHolder(View itemView) {
                super(itemView);
                // 获取行视图中的控件
               ivCreprojectPicture=itemView.findViewById(R.id.iv_mine_Creproject_picture);
               ivFhIcon=itemView.findViewById(R.id.fh_icon);
               ivDetails=itemView.findViewById(R.id.iv_mine_details);
               ivDianzan=itemView.findViewById(R.id.iv_mine_details);
               tvCreprojectTitle = itemView.findViewById(R.id.tv_mine_Creproject_title);
               tvCreprojectState = itemView.findViewById(R.id.tv_mine_Creproject_state);
               tvCreprojectPraise = itemView.findViewById(R.id.tv_mine_Creproject_praise);
            }
        }
}
