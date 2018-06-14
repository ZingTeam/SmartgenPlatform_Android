package com.example.txjju.smartgenplatform_android.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.activity.AboutMeActivity;
import com.example.txjju.smartgenplatform_android.activity.MainActivity;
import com.example.txjju.smartgenplatform_android.activity.OrderActivity;
import com.example.txjju.smartgenplatform_android.activity.SettingActivity;
import com.example.txjju.smartgenplatform_android.activity.ShoppingCartActivity;
import com.example.txjju.smartgenplatform_android.activity.SystemMessagesActivity;
import com.example.txjju.smartgenplatform_android.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 个人中心的Fragment
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvName,tvCollectProject,tvCollectProduct,tvEdit,tvCollectProductLine,tvCollectProjectLine;
    private ImageView ivImgUser,ivSet,ivNews;
    private RadioButton rbOrder,rbPublic,rbCart,rbHistory;
    private RecyclerView rvCreativeProject;

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
        //获取新建视图View中布局文件的控件
        initViews(view);
        initFragment();
        return view;
    }

    private void initFragment() {
        fgList = new ArrayList<>();
        fgList.add(new CollectProductFragment());
        fgList.add(new CollectCreProjectFragment());
        fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction(); // 实例化Fragment事物管理器
        for (int i = 0; i < fgList.size(); i++) {     // 将两个Fragment装在容器中，交给ft管理
            ft.add(R.id.fl_container, fgList.get(i), tags[i]);
        }
        ft.commit();    // 提交保存
        showFragment(0);    // 默认显示首页Fragment
    }

    private void showFragment(int i) {
        hideFragments();    // 先隐藏所有Fragment
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.show(fgList.get(i));     // 显示Fragment
        ft.commit();
    }

    private void hideFragments() {
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fg : fgList) {
            ft.hide(fg);        // 隐藏所有Fragment
        }
        ft.commit();
    }

    private void clickAgain(int i) {
        if(currPosition == i){      // 当前显示为某页面，并再次点击了此页面的导航按钮
            EventBus.getDefault().post(new MessageEvent(tags[i]));  // 通知Fragment刷新
        }
        currPosition = i;
    }

    private void initViews(View view) {
        ivImgUser = view.findViewById(R.id.img_mine_user);//获取用户头像控件
        ivSet = view.findViewById(R.id.iv_mine_set);//获取“设置”控件
        ivNews = view.findViewById(R.id.iv_mine_news);//获取“消息”控件
        tvEdit = view.findViewById(R.id.tv_mine_edit);//获取“编辑”控件
        tvCollectProduct = view.findViewById(R.id.tv_mine_collect_product);//获取“收藏的商品”控件
        tvCollectProject = view.findViewById(R.id.tv_mine_collect_project);//获取“收藏的项目”控件
        tvCollectProductLine = view.findViewById(R.id.tv_collect_product_line_mine);//获取“收藏的商品下划线”控件
        tvCollectProjectLine = view.findViewById(R.id.tv_collect_project_line_mine);//获取“发布的项目下划线”控件
        rbOrder=view.findViewById(R.id.rb_mine_myOrder);//获取“我的订单”控件
        rbPublic=view.findViewById(R.id.rb_mine_myPublic);//获取“我的发布”控件
        rbCart=view.findViewById(R.id.rb_mine_shoppingCart);//获取“购物车”控件
        rbHistory=view.findViewById(R.id.rb_mine_history);//获取“浏览历史”控件
        ivImgUser.setOnClickListener(this);//控件的监听事件
        tvEdit.setOnClickListener(this);
        ivSet.setOnClickListener(this);
        ivNews.setOnClickListener(this);
        tvCollectProduct.setOnClickListener(this);
        tvCollectProject.setOnClickListener(this);
        rbOrder.setOnClickListener(this);
        rbPublic.setOnClickListener(this);
        rbCart.setOnClickListener(this);
        rbHistory.setOnClickListener(this);
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
            case R.id.rb_mine_myOrder:
              Intent intent3 =new Intent(getActivity(),OrderActivity.class);
                startActivity(intent3);
                break;
            case R.id.rb_mine_myPublic:
//                Intent intent4 =new Intent(getActivity(),OrderActivity.class);
//                startActivity(intent4);
                break;
            case R.id.rb_mine_shoppingCart:
                Intent intent5 =new Intent(getActivity(),ShoppingCartActivity.class);
                startActivity(intent5);
                break;
            case R.id.rb_mine_history:
//                Intent intent6 =new Intent(getActivity(),OrderActivity.class);
//                startActivity(intent6);
                break;
            case R.id.tv_mine_collect_product:  // 选择“收藏的商品”
                tvCollectProduct.setEnabled(false);
                tvCollectProject.setEnabled(true);
                tvCollectProductLine.setBackgroundColor(Color.parseColor("#3197ff"));
                tvCollectProjectLine.setBackgroundColor(Color.parseColor("#bfbfbf"));
                showFragment(0);
                clickAgain(0);
                break;
            case R.id.tv_mine_collect_project:// 选择“收藏的项目”
                tvCollectProject.setEnabled(false);
                tvCollectProduct.setEnabled(true);
                tvCollectProjectLine.setBackgroundColor(Color.parseColor("#3197ff"));
                tvCollectProductLine.setBackgroundColor(Color.parseColor("#bfbfbf"));
                showFragment(1);
                clickAgain(1);
                break;
        }
    }
}
