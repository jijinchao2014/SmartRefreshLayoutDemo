package com.example.xu.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by jjc on 2018/4/13.
 */

public class NoAnimViewPager extends ViewPager {
    public NoAnimViewPager(Context context) {
        super(context);
    }

    public NoAnimViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }
}
