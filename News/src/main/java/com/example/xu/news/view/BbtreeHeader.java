package com.example.xu.news.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xu.news.R;
import com.example.xu.news.Utils.CommonUtil;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jjc on 2018/4/12.
 */

public class BbtreeHeader extends LinearLayout implements RefreshHeader {
    protected String KEY_LAST_UPDATE_TIME = "LAST_UPDATE_TIME";
    private TextView mTitleTextView,tvStretch;
    private ImageView ivBaby;
    private TextView mLastUpdateTextView;
    protected Date mLastTime;
    protected SharedPreferences mShared;
    protected DateFormat mLastUpdateFormat;
    protected boolean mEnableLastTime = true;
    public BbtreeHeader(@NonNull Context context) {
        this(context,null);
    }

    public BbtreeHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BbtreeHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_bbtree, this);
        tvStretch = (TextView) header.findViewById(R.id.tv_stretch);
        ivBaby = (ImageView) header.findViewById(R.id.iv_heade_baby);
        //帧动画
        ivBaby.setBackgroundResource(R.drawable.baby_anim);
        AnimationDrawable anim = (AnimationDrawable) ivBaby.getBackground();
        anim.start();
        mTitleTextView = (TextView) header.findViewById(R.id.ptr_classic_header_rotate_view_header_title);
        mLastUpdateTextView = (TextView) header.findViewById(R.id.ptr_classic_header_rotate_view_header_last_update);

//        anim_left = AnimationUtils.loadAnimation(context, R.anim.set_left_bg);
//        anim_right = AnimationUtils.loadAnimation(context, R.anim.set_right_bg);

        try {//try 不能删除-否则会出现兼容性问题
            if (context instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                if (manager != null) {
                    @SuppressLint("RestrictedApi")
                    List<Fragment> fragments = manager.getFragments();
                    if (fragments != null && fragments.size() > 0) {
                        setLastUpdateTime(new Date());
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

//        setPrimaryColors(getResources().getColor(R.color.colorAccent),getResources().getColor(android.R.color.white));
        mLastUpdateFormat = new SimpleDateFormat("上次更新 M-d HH:mm", Locale.getDefault());
        KEY_LAST_UPDATE_TIME += context.getClass().getName();
        mShared = context.getSharedPreferences("BbtreeHeader", Context.MODE_PRIVATE);
        setLastUpdateTime(new Date(mShared.getLong(KEY_LAST_UPDATE_TIME, System.currentTimeMillis())));
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        if (isDragging){
            if (percent <= 1){
                int moveX = (int)(CommonUtil.dip2px(60)*percent);
//                Log.i("jijc","----moveX:"+moveX+"----per:"+percent+"---offset:"+offset);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tvStretch
                        .getLayoutParams();
                tvParams.width = moveX;
                tvStretch.setLayoutParams(tvParams);
            }
        }else {
            int moveX = (int)(CommonUtil.dip2px(60));
            RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tvStretch
                    .getLayoutParams();
            tvParams.width = moveX;
            tvStretch.setLayoutParams(tvParams);
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        if (success) {
            mTitleTextView.setText("刷新完成");
            if (mLastTime != null) {
                setLastUpdateTime(new Date());
            }
        } else {
            mTitleTextView.setText("刷新失败");
        }
        return 100;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }


    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View updateView = mLastUpdateTextView;
        switch (newState) {
            case None:
                updateView.setVisibility(mEnableLastTime ? VISIBLE : GONE);
            case PullDownToRefresh:
//                mTitleTextView.setText("下拉开始刷新");
                mTitleTextView.setVisibility(GONE);
                mLastUpdateTextView.setVisibility(GONE);
                break;
            case Refreshing:
                mTitleTextView.setText("努力加载中");
                mTitleTextView.setVisibility(VISIBLE);
                mLastUpdateTextView.setVisibility(VISIBLE);
                break;
            case ReleaseToRefresh:
                mTitleTextView.setText("努力加载中");
                mTitleTextView.setVisibility(VISIBLE);
                mLastUpdateTextView.setVisibility(VISIBLE);
                break;
        }
    }

    public BbtreeHeader setLastUpdateTime(Date time) {
        final View thisView = this;
        mLastTime = time;
        mLastUpdateTextView.setText(mLastUpdateFormat.format(time));
        if (mShared != null && !thisView.isInEditMode()) {
            mShared.edit().putLong(KEY_LAST_UPDATE_TIME, time.getTime()).apply();
        }
        return this;
    }

    public BbtreeHeader setTimeFormat(DateFormat format) {
        mLastUpdateFormat = format;
        if (mLastTime != null) {
            mLastUpdateTextView.setText(mLastUpdateFormat.format(mLastTime));
        }
        return this;
    }


}
