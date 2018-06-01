package com.example.txjju.smartgenplatform_android.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/6/1.
 */

public class GuideAdapter extends PagerAdapter {


    private List<ImageView> mImageList;

    @Override
    public int getCount() {
        return  mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
       public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageList.get(position));
           return mImageList.get(position);
        }

        @Override
       public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView(mImageList.get(position));
       }
}
