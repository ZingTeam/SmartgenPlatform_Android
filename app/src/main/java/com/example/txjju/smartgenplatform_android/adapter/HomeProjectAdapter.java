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

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getCreprojectTitle());
        // 图片加载
        Glide.with(context).load(list.get(position).getCreprojectPicture()).into(holder.iv);
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

