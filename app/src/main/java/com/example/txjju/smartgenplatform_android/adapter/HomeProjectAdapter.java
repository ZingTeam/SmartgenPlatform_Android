package com.example.txjju.smartgenplatform_android.adapter;


import android.content.Context;
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

import java.util.List;

/**
 * 首页项目列表适配器
 */
public class HomeProjectAdapter extends RecyclerView.Adapter<HomeProjectAdapter.ViewHolder> {

    private Context context;
    private List<Creativeproject> list;

    public HomeProjectAdapter(Context context , List<Creativeproject> list) {
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
                R.layout.item_project_home_layout, parent, false);
        return new ViewHolder(view);    // 返回ViewHolder对象，并将行视图通过构造传入
    }

    //监听item的接口
    public interface OnItemClickListener{
        void onItemClick(View view , int position);//重写方法
    }
    private HomeProjectAdapter.OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(HomeProjectAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getCreprojectTitle());
        // 图片加载
        Glide.with(context).load(list.get(position).getCreprojectPicture()).placeholder(R.mipmap.login_bg).into(holder.iv);
        //监听item
        View itemView = ((LinearLayout) holder.itemView).getChildAt(0);
        if (mOnItemClickListener != null) {
            Log.i("MainActivity","首页项目监听进来了");
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            // 获取行视图中的控件
            iv = itemView.findViewById(R.id.project_img);
            tvName = itemView.findViewById(R.id.project_name);
        }
    }
}

