package com.example.xu.news;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.xu.news.Utils.ImmersionStyleCompat;
import com.example.xu.news.adapter.MyPagerAdapter;

public class MainAct extends AppCompatActivity {
    //数据源
    private String[] titles = {"推荐", "头条", "新闻", "娱乐", "体育", "美女", "科技", "财经", "汽车", "彩票", "国际", "股票", "军事"};

    private TabLayout tablayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        // 设置状态栏样式
        ImmersionStyleCompat.setImmersionStyle(this,
                ContextCompat.getColor(MainAct.this, R.color.color_ff0000),
                ContextCompat.getColor(MainAct.this, R.color.color_ffffff), false);
        // 当前页面的背景色
        getWindow().setBackgroundDrawableResource(R.color.color_f5f5f5);

        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }
}
