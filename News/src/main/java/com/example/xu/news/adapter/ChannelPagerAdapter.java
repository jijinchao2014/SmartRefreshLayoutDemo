package com.example.xu.news.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.xu.news.base.LazyloadFragment;
import com.example.xu.news.bean.Channel;

import java.util.List;

/**
 * Created by Xu on 2018/4/4
 */

public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private final FragmentManager mFm;
    private List<LazyloadFragment> fragments;
    private List<Channel> mChannels;
    private int mChildCount;
    private boolean[] fragmentsUpdateFlag;

    public ChannelPagerAdapter(FragmentManager fm, List<LazyloadFragment> fragments, List<Channel> channels) {
        super(fm);
        mFm = fm;
        this.fragments = fragments;
        this.mChannels = channels;
    }

    @Override
    public LazyloadFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mChannels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels == null ? "" : mChannels.get(position).Title;
    }

//    @Override
//    public void notifyDataSetChanged() {
//        mChildCount = getCount();
//        super.notifyDataSetChanged();
//    }



    @Override
    public int getItemPosition(Object object) {
//        if (mChildCount > 0) {
//            mChildCount--;
        return POSITION_NONE;
//        }
//        return super.getItemPosition(object);
    }

}
