package com.example.xu.news;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xu.news.Utils.CommonUtil;
import com.example.xu.news.Utils.ConstanceValue;
import com.example.xu.news.Utils.ImmersionStyleCompat;
import com.example.xu.news.Utils.SharedPreferencesMgr;
import com.example.xu.news.Utils.ToastUtils;
import com.example.xu.news.adapter.ChannelPagerAdapter;
import com.example.xu.news.base.LazyloadFragment;
import com.example.xu.news.bean.Channel;
import com.example.xu.news.listener.OnChannelListener;
import com.example.xu.news.view.ChannelDialogFragment;
import com.example.xu.news.view.NoAnimViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.library.colortrackview.ColorTrackTabLayout;

/**
 * Created by Xu on 2018/4/4
 */

public class MainAct2 extends AppCompatActivity implements OnChannelListener, DialogInterface.OnDismissListener {
    @BindView(R.id.tab)
    ColorTrackTabLayout tab;
    @BindView(R.id.vp)
    NoAnimViewPager mVp;
    @BindView(R.id.icon_category)
    ImageView icon_category;

    private ChannelPagerAdapter mTitlePagerAdapter;
    public List<Channel> mSelectedDatas = new ArrayList<>();
    public List<Channel> mUnSelectedDatas = new ArrayList<>();
    private List<LazyloadFragment> mFragments;
    private Gson mGson = new Gson();
    String TITLE_SELECTED = "explore_title_selected";
    String TITLE_UNSELECTED = "explore_title_unselected";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("xu", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main2);
        // 设置状态栏样式
        ImmersionStyleCompat.setImmersionStyle(this,
                ContextCompat.getColor(MainAct2.this, R.color.color_ff0000),
                ContextCompat.getColor(MainAct2.this, R.color.color_ffffff), false);
        // 当前页面的背景色
        getWindow().setBackgroundDrawableResource(R.color.color_f5f5f5);

        ButterKnife.bind(this);

        getTitleData();
        mFragments = new ArrayList<>();
        for (int i = 0; i < mSelectedDatas.size(); i++) {
            MyFragment fragment = MyFragment.newInstance(mSelectedDatas.get(i).TitleCode);
            mFragments.add(fragment);
        }
        mTitlePagerAdapter = new ChannelPagerAdapter(getSupportFragmentManager(), mFragments, mSelectedDatas);
        mVp.setAdapter(mTitlePagerAdapter);
        mVp.setOffscreenPageLimit(mSelectedDatas.size());

        tab.setTabPaddingLeftAndRight(CommonUtil.dip2px(10), CommonUtil.dip2px(10));
        tab.setupWithViewPager(mVp);
        tab.post(new Runnable() {
            @Override
            public void run() {
                //设置最小宽度，使其可以在滑动一部分距离
                ViewGroup slidingTabStrip = (ViewGroup) tab.getChildAt(0);
                slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + icon_category.getMeasuredWidth());
//                setIndicator(tab, 10, 10);
            }
        });

        reflex(tab);
        //隐藏指示器
         tab.setSelectedTabIndicatorHeight(2);

    }

    private void getTitleData() {
        String selectTitle = SharedPreferencesMgr.getString(TITLE_SELECTED, "");
        String unselectTitle = SharedPreferencesMgr.getString(TITLE_UNSELECTED, "");
        if (TextUtils.isEmpty(selectTitle) || TextUtils.isEmpty(unselectTitle)) {
            //本地没有title
            String[] titleStr = getResources().getStringArray(R.array.home_title);
            String[] titlesCode = getResources().getStringArray(R.array.home_title_code);
            //默认添加了全部频道
            for (int i = 0; i < titlesCode.length; i++) {
                String t = titleStr[i];
                String code = titlesCode[i];
                mSelectedDatas.add(new Channel(t, code));
            }

            String selectedStr = mGson.toJson(mSelectedDatas);
            SharedPreferencesMgr.setString(TITLE_SELECTED, selectedStr);
        } else {
            //之前添加过
            List<Channel> selecteData = mGson.fromJson(selectTitle, new TypeToken<List<Channel>>() {
            }.getType());
            List<Channel> unselecteData = mGson.fromJson(unselectTitle, new TypeToken<List<Channel>>() {
            }.getType());
            mSelectedDatas.addAll(selecteData);
            mUnSelectedDatas.addAll(unselecteData);
        }
    }

    @OnClick(R.id.icon_category)
    public void onclick(View view) {
        ToastUtils.showToast(MainAct2.this, "频道管理页面");
        ChannelDialogFragment.newInstance(mSelectedDatas, mUnSelectedDatas, this, this).show(getSupportFragmentManager(), "channel");
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mTitlePagerAdapter.notifyDataSetChanged();
        mVp.setOffscreenPageLimit(mSelectedDatas.size());
        tab.setCurrentItem(tab.getSelectedTabPosition());
        ViewGroup slidingTabStrip = (ViewGroup) tab.getChildAt(0);
        //注意：因为最开始设置了最小宽度，所以重新测量宽度的时候一定要先将最小宽度设置为0
        slidingTabStrip.setMinimumWidth(0);
        slidingTabStrip.measure(0, 0);
        slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + icon_category.getMeasuredWidth());

        //保存选中和未选中的channel
        SharedPreferencesMgr.setString(ConstanceValue.TITLE_SELECTED, mGson.toJson(mSelectedDatas));
        SharedPreferencesMgr.setString(ConstanceValue.TITLE_UNSELECTED, mGson.toJson(mUnSelectedDatas));
    }

    @Override
    public void onItemMove(int starPos, int endPos) {
        listMove(mSelectedDatas, starPos, endPos);
        listMove(mFragments, starPos, endPos);
    }

    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        //移动到我的频道
        Channel channel = mUnSelectedDatas.remove(starPos);
        mSelectedDatas.add(endPos, channel);
        mFragments.add(MyFragment.newInstance(channel.TitleCode));
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        //移动到推荐频道
        mUnSelectedDatas.add(endPos, mSelectedDatas.remove(starPos));
        mFragments.remove(starPos);
    }

    private void listMove(List datas, int starPos, int endPos) {
        Object o = datas.get(starPos);
        //先删除之前的位置
        datas.remove(starPos);
        //添加到现在的位置
        datas.add(endPos, o);
    }

    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = CommonUtil.dip2px(10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
