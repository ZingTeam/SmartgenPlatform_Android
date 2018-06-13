package com.example.txjju.smartgenplatform_android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.adapter.ProductDetailPagerAdapter;
import com.example.txjju.smartgenplatform_android.fragment.ProductDetailFragment;
import com.example.txjju.smartgenplatform_android.fragment.ProductFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDetailsActivity extends BaseActivity implements ProductFragment.OnScrollTabChangeListener, View.OnClickListener {
    private TabLayout tabLayout;//顶部导航栏
    private ViewPager viewPager;
    private ImageView toolbarMore,ivBack,ivToolbarMore;
    private ProductDetailPagerAdapter productPagerAdapter = null;
    //private MorePopupWindow popupWindow = null;

    private List<Fragment> mFragments;
    private String[] titles = new String[]{"商品", "详情"};
    public final static String TAG = "ProductDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
    }

    private void init() {
        ivBack = findViewById(R.id.back);
        ivToolbarMore = findViewById(R.id.toolbar_more);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        ivBack.setOnClickListener(this);
        mFragments = new ArrayList<>();
        mFragments.add(new ProductFragment());
        mFragments.add(new ProductDetailFragment());

        productPagerAdapter = new ProductDetailPagerAdapter(getSupportFragmentManager(), mFragments, Arrays.asList(titles));
        Log.i(TAG,"结果："+productPagerAdapter.getCount());
        viewPager.setOffscreenPageLimit(productPagerAdapter.getCount());
        viewPager.setAdapter(productPagerAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
           // popupWindow.dismiss();
        }
    };


    public static void open(Context context) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void currentTab(int tabId) {
        Log.i(TAG,"当前："+tabId);
        viewPager.setCurrentItem(tabId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.toolbar_more:
                List list = new ArrayList<String>();
                list.add("消息");
                list.add("服务社");
                list.add("购物车");
                //popupWindow = new MorePopupWindow(this, onItemClickListener);
               // popupWindow.setList(list);

               // popupWindow.show(toolbarMore, 20);
                break;
        }

    }
}
