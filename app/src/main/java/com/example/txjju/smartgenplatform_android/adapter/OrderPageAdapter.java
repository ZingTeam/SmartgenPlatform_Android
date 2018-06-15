package com.example.txjju.smartgenplatform_android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/6/14.
 */

public class OrderPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments=null;
    private List<String> mTitles=null;

    public OrderPageAdapter(FragmentManager fm, List<Fragment> mFragments, List<String> mTitles) {
        super(fm);
        this.mFragments =mFragments;
        this.mTitles=mTitles;
    }

    public OrderPageAdapter(FragmentManager fm, Fragment... fragments) {
        super(fm);
        this.mFragments = Arrays.asList(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
