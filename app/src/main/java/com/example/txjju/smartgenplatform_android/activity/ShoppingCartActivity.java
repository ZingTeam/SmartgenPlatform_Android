package com.example.txjju.smartgenplatform_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.ShoppingCartAdapter;
import com.example.txjju.smartgenplatform_android.pojo.ShoppingCartBean;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener
        ,ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface {
    public TextView tv_title, tv_settlement, tv_show_price;
    private TextView tv_all_check;
    private CheckBox ck_all;
    private ListView list_shopping_cart;
    private ShoppingCartAdapter shoppingCartAdapter;
    private TextView tv_edit;
    private boolean flag = false;
    private List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
    private boolean mSelect;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initView();
        initData();
    }
    /**
     * 批量模式下，用来记录当前选中状态
     */
    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();


    protected int getLayout() {
        return R.layout.activity_shopping_cart;
    }


    protected void initView() {
        list_shopping_cart = findViewById(R.id.list_shopping_cart);
        ck_all = findViewById(R.id.ck_all);
        ck_all.setOnClickListener(this);
        tv_show_price = findViewById(R.id.tv_show_price);
        tv_settlement = findViewById(R.id.tv_settlement);
        tv_settlement.setOnClickListener(this);
        tv_edit = findViewById(R.id.tv_shoppingCart_edit);
        tv_edit.setOnClickListener(this);
        shoppingCartAdapter = new ShoppingCartAdapter(this);
        shoppingCartAdapter.setCheckInterface(this);
        shoppingCartAdapter.setModifyCountInterface(this);
        list_shopping_cart.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);

    }

    protected void initData() {

        for (int i = 0; i < 6; i++) {
            ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
            shoppingCartBean.setShoppingName("树叶回收垃圾桶——方便实惠，你值得拥有");
            shoppingCartBean.setPrice(25.0);
            shoppingCartBean.setCount(1);
            shoppingCartBean.getPicture();
            shoppingCartBeanList.add(shoppingCartBean);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选按钮
            case R.id.ck_all:
                if (shoppingCartBeanList.size() != 0) {
                    if (ck_all.isChecked()) {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(true);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(false);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.tv_shoppingCart_edit:
                flag = !flag;
                if (flag) {
                    tv_edit.setText("完成");
                    shoppingCartAdapter.isShow(false);
                } else {
                    tv_edit.setText("编辑");
                    shoppingCartAdapter.isShow(true);
                }
                break;
        }
    }

    /**
     * 单选
     *
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {

        shoppingCartBeanList.get(position).setChoosed(isChecked);

        if (isAllCheck())
            ck_all.setChecked(true);
        else
            ck_all.setChecked(false);

        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }


    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {

        for (ShoppingCartBean group : shoppingCartBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                totalPrice += shoppingCartBean.getPrice() * shoppingCartBean.getCount();
            }
        }
        tv_show_price.setText("合计:" + totalPrice);
        tv_settlement.setText("结算(" + totalCount + ")");
    }

    /**
     * 增加
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
        currentCount++;
        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();

    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void childDelete(int position) {
        shoppingCartBeanList.remove(position);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();

    }


}