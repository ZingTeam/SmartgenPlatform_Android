package com.example.txjju.smartgenplatform_android.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.AboutMeActivity;
import com.example.txjju.smartgenplatform_android.activity.CollectProductActivity;
import com.example.txjju.smartgenplatform_android.activity.CollectProjectActivity;
import com.example.txjju.smartgenplatform_android.activity.MainActivity;
import com.example.txjju.smartgenplatform_android.activity.OrderActivity;
import com.example.txjju.smartgenplatform_android.activity.ProductDetailsActivity;
import com.example.txjju.smartgenplatform_android.activity.PublicActivity;
import com.example.txjju.smartgenplatform_android.activity.SettingActivity;
import com.example.txjju.smartgenplatform_android.activity.ShoppingCartActivity;
import com.example.txjju.smartgenplatform_android.activity.SystemMessagesActivity;
import com.example.txjju.smartgenplatform_android.activity.UpdateAdressActivity;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.MessageEvent;
import com.example.txjju.smartgenplatform_android.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 个人中心的Fragment
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvName,tvAttention,tvFans,tvOneMsg,tvCollectProject,tvCollectProduct,tvEdit,tvCollectProductLine,tvCollectProjectLine;
    private ImageView ivImgUser,ivSet,ivNews;
    private RelativeLayout rlOrder,rlpublic,rlCart,rlAddress,rlHistory,rlCollectProduct,rlCollectProject;
    private RadioButton rbOrder,rbPublic,rbCart,rbHistory;
    private RecyclerView rvCreativeProject;
    private Dialog dialog;
    private int userId;//保存用户ID
    private User user;

    //fragment
    private List<Fragment> fgList;  // 装载Fragment的集合
    private android.support.v4.app.FragmentManager fm; // fragment管理器
    private int currPosition = 0;
    private String[] tags = new String[]{"CollectProductFragment", "CollectCreProjectFragment"};

    //fragment中布局以及控件的获取都写在此方法中
    //onCreateView方法是在onCreate方法之后执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        //获取用户信息
        user = SPUtil.getUser(getActivity());
        //获取新建视图View中布局文件的控件
        initViews(view);
        if(user == null){
            return view;
        }
        init();//初始化用户信息
        return view;
    }

    private void init() {
        // 用户头像图片加载
        Glide.with(getActivity()).load(user.getUserHeadPortrait()).placeholder(R.mipmap.qx).into(ivImgUser);
        tvName.setText(user.getUserName());
        tvOneMsg.setText("改变世界，创意你我");
        tvAttention.setText("50");
        tvFans.setText("85");
    }

    private void initViews(View view) {
        tvName = view.findViewById(R.id.tv_mine_name);
        tvAttention = view.findViewById(R.id.tv_attention_mine);
        tvFans = view.findViewById(R.id.tv_fans_mine);
        tvOneMsg = view.findViewById(R.id.tv_one_msg_mine);
        ivImgUser = view.findViewById(R.id.img_mine_user);//获取用户头像控件
        ivSet = view.findViewById(R.id.iv_mine_set);//获取“设置”控件
        ivNews = view.findViewById(R.id.iv_mine_news);//获取“消息”控件
        tvEdit = view.findViewById(R.id.tv_mine_edit);//获取“编辑”控件
        rlOrder=view.findViewById(R.id.rl_order);//获取“我的订单”控件
        rlpublic=view.findViewById(R.id.rl_public);//获取“我的发布”控件
        rlCart=view.findViewById(R.id.rl_cart);//获取“我的购物车”控件
        rlAddress=view.findViewById(R.id.rl_address);//获取“我的收货地址”控件
        rlHistory=view.findViewById(R.id.rl_history);//获取“我的历史记录”控件
        rlCollectProduct=view.findViewById(R.id.rl_collect_product);//获取“我的产品收藏”控件
        rlCollectProject=view.findViewById(R.id.rl_collect_project);//获取“我的项目收藏”控件

        ivImgUser.setOnClickListener(this);//控件的监听事件
        tvEdit.setOnClickListener(this);
        ivSet.setOnClickListener(this);
        ivNews.setOnClickListener(this);
        rlOrder.setOnClickListener(this);
        rlpublic.setOnClickListener(this);
        rlCart.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
        rlHistory.setOnClickListener(this);
        rlCollectProduct.setOnClickListener(this);
        rlCollectProject.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.img_mine_user:
            case R.id.tv_mine_edit:
                Intent intent = new Intent();
                //AboutMeActivity.class为想要跳转的Activity
                intent.setClass(getActivity(), AboutMeActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_mine_news:
                Intent intent1 = new Intent();
                // SystemMessagesActivity.class为想要跳转的Activity
                intent1.setClass(getActivity(), SystemMessagesActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_mine_set:
                Intent intent2 = new Intent();
                // SystemMessagesActivity.class为想要跳转的Activity
                intent2.setClass(getActivity(), SettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_order://选择我的订单
                Intent intent3 =new Intent(getActivity(),OrderActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_public://选择我的发布
                Intent intent4 =new Intent(getActivity(),PublicActivity.class);
                startActivity(intent4);
                break;
            case R.id.rl_cart://选择我的购物车
                Intent intent5 =new Intent(getActivity(),ShoppingCartActivity.class);
                startActivity(intent5);
                break;
            case R.id.rl_address://选择我的收货地址
                Intent intent6 =new Intent(getActivity(),UpdateAdressActivity.class);
                startActivity(intent6);
                break;
            case R.id.rl_history://选择历史记录
//                Intent intent6 =new Intent(getActivity(),OrderActivity.class);
//                startActivity(intent6);
                break;
            case R.id.rl_collect_product:  // 选择“收藏的商品”
                Intent intent8 =new Intent(getActivity(),CollectProductActivity.class);
                startActivity(intent8);
                break;
            case R.id.rl_collect_project:// 选择“收藏的项目”
                Intent intent9 =new Intent(getActivity(),CollectProjectActivity.class);
                startActivity(intent9);
               // tvCollectProductLine.setBackgroundColor(Color.parseColor("#bfbfbf"));
                break;
        }
    }
}
