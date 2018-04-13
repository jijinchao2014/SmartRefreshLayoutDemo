package com.example.xu.news.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class LazyloadFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    private boolean isInitView = false; // 是否初始化view
    private boolean isVisible = false; // 是否可见
    private Unbinder unbinder;// 注解
    protected Context mContext;

    /**
     * 加载页面布局文件
     * @return
     */
    protected abstract int setContentView();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 加载要显示的数据
     */
    protected abstract void lazyLoad();

    /**
     * 查找组件
     * @param resID
     * @return
     */
    public final View findViewById(int resID) {
        return this.rootView != null ? this.rootView.findViewById(resID) : null;
    }

    /**
     * 判断所有view初始化完成对用户可见时，加载数据
     */
    private void isCanLoadData() {
        if (isInitView && isVisible) {
            lazyLoad();
            isInitView = false;
            isVisible = false;
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(setContentView(), container, false);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        unbinder = ButterKnife.bind(this, rootView);
        initView(savedInstanceState);
        isInitView = true;
        isCanLoadData();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            isCanLoadData();
        } else {
            isVisible = false;
        }
    }
}
