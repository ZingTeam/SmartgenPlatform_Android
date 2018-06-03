package com.example.txjju.smartgenplatform_android.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.fragment.HomeFragment;
import com.example.txjju.smartgenplatform_android.fragment.MarketFragment;
import com.example.txjju.smartgenplatform_android.fragment.MineFragment;
import com.example.txjju.smartgenplatform_android.fragment.StoreFragment;
import com.example.txjju.smartgenplatform_android.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.ashokvarma.bottomnavigation.ShapeBadgeItem.SHAPE_OVAL;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "MainActivity";
    private ArrayList<Fragment> fragments;
    private List<Fragment> fgList;//装载Fragment的集合
    private FragmentManager fm;//fragment管理器
    private int currPosition = 0;//默认是首页
    private String[] tags = new String[]{"HomeFragment","MarketFragment","StoreFragment","MineFragment"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE
                );

        ShapeBadgeItem shapeBadgeItem = new ShapeBadgeItem();
        shapeBadgeItem.setShape(SHAPE_OVAL) //形状
                .setShapeColor(Color.BLUE) //颜色
                .setShapeColorResource(R.color.colorPrimaryDark) //颜色，资源文件获取
                .setEdgeMarginInDp(this,0) //距离Item的margin，dp
                .setEdgeMarginInPixels(20) //距离Item的margin，px
                .setSizeInDp(this,10,10) //宽高，dp
                .setSizeInPixels(5,5) //宽高，px
                .setAnimationDuration(200) //隐藏和展示的动画速度，单位毫秒,和setHideOnSelect一起使用
                .setGravity(Gravity.LEFT) //位置，默认右上角
                .setHideOnSelect(true); //true：当选中状态时消失，非选中状态显示,moren false

        TextBadgeItem numberBadgeItem = new TextBadgeItem()
                .setBackgroundColor("#FF0000") //背景色
                .setTextColor("#FFFFFF") //文本颜色
                .setBorderColor("#000000") //border颜色
                .setBorderWidth(5) //border宽度px
                .setText("s")
                .setBackgroundColorResource(R.color.colorPrimaryDark) //背景色，资源文件获取
                .setBorderColorResource(R.color.colorPrimary) //border颜色，资源文件获取
                .setTextColorResource(R.color.colorAccent) //文本颜色，资源文件获取
                .setAnimationDuration(30) //隐藏和展示的动画速度，单位毫秒,和setHideOnSelect一起使用
                .setGravity(Gravity.RIGHT|Gravity.TOP) //位置，默认右上角
                .setHideOnSelect(true); //true：当选中状态时消失，非选中状态显示,moren false
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.wxbhome, "首页").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.wxb, "创意市场").setActiveColorResource(R.color.teal))
                .addItem(new BottomNavigationItem(R.mipmap.go, "众智商城").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.account, "个人中心").setActiveColorResource(R.color.brown).setBadgeItem(numberBadgeItem))
                .setFirstSelectedPosition(0)
                .initialise();
        setBottomNavigationItem(bottomNavigationBar, 5, 21, 12);

        initFragment();

        bottomNavigationBar.setTabSelectedListener(this);
    }

    private void initFragment() {
        fgList = new ArrayList<>();
        fgList.add(new HomeFragment());
        fgList.add(new MarketFragment());
        fgList.add(new StoreFragment());
        fgList.add(new MineFragment());
        //调用管理器,管理Fragment，一个事物管理器管理一个Fragment
        fm = getSupportFragmentManager();//实例化Fragment对象
        FragmentTransaction ft = fm.beginTransaction();//实例化Fragment事务管理器
        for(int i = 0; i < fgList.size();i++){//将四个Fragment装在容器中，交给ft管理
            ft.add(R.id.fl_container,fgList.get(i),tags[i]);
        }
        ft.commit();//提交保存
        showFragment(0);//默认显示首页Fragment

    }

    private void showFragment(int i) {
        FragmentTransaction ft = fm.beginTransaction();//实例化Fragment事务管理器
        hiddenFragment();//先隐藏所有Fragment
        ft.show(fgList.get(i));//显示Fragment
        ft.commit();
    }

    private void hiddenFragment() {
        FragmentTransaction ft = fm.beginTransaction();//实例化Fragment事务管理器
        for(Fragment fg:fgList){
            ft.hide(fg);
        }
        ft.commit();
    }

    /**
     * 设置默认的

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, HomeFragment.newInstance("Home"));
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("Home"));
        fragments.add(MarketFragment.newInstance("Market"));
        fragments.add(StoreFragment.newInstance("Store"));
        fragments.add(MineFragment.newInstance("Mine"));
        return fragments;
    }*/

    /***
     *
     * 切换 TabBar item 同时设置 切换fragment 改变activity界面内容
     */
    @Override
    public void onTabSelected(int position) {
        switch (position){
            case 0://选择首页
                Log.d(TAG,"选择首页");
                showFragment(0);
                clickAgain(0);
                break;
            case 1://选择创意市场
                Log.d(TAG,"选择创意市场");
                showFragment(1);
                clickAgain(1);
                break;
            case 2://选择众智商城
                Log.d(TAG,"选择众智商城");
                showFragment(2);
                clickAgain(2);
                break;
            case 3://选择个人中心
                Log.d(TAG,"选择个人中心");
                showFragment(3);
                clickAgain(3);
                break;
        }
    }

    private void clickAgain(int i) {
        if(currPosition == i){      // 当前显示为某页面，并再次点击了此页面的导航按钮
            EventBus.getDefault().post(new MessageEvent(tags[i]));  // 通知Fragment刷新
        }
        currPosition = i;
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {

    }
    /**
     @param bottomNavigationBar，需要修改的 BottomNavigationBar
     @param space 图片与文字之间的间距
     @param imgLen 单位：dp，图片大小，应 <= 36dp
     @param textSize 单位：dp，文字大小，应 <= 20dp

     使用方法：直接调用setBottomNavigationItem(bottomNavigationBar, 6, 26, 10);
     代表将bottomNavigationBar的文字大小设置为10dp，图片大小为26dp，二者间间距为6dp
     **/

    private void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize){
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            field.setAccessible(true);
            if(field.getName().equals("mTabContainer")){
                try{
                    //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for(int j = 0; j < mTabContainer.getChildCount(); j++){
                        //获取到容器内的各个Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));

                        //获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0,0,0,dip2px(20-textSize - space/2));

                        //获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0,0,0,space/2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
