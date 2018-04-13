package com.example.xu.news.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.example.xu.news.bean.Movie;
import com.example.xu.news.provider.TypeOneItemProvider;
import com.example.xu.news.provider.TypeTwoItemProvider;

import java.util.List;

/**
 * Created by jjc on 2018/4/12.
 */

public class MovieMultipleAdapter extends MultipleItemRvAdapter<Movie,BaseViewHolder> {
    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;
    public MovieMultipleAdapter(@Nullable List<Movie> data) {
        super(data);

        //构造函数若有传其他参数可以在调用finishInitialize()之前进行赋值，赋值给全局变量
        //这样getViewType()和registerItemProvider()方法中可以获取到传过来的值
        //getViewType()中可能因为某些业务逻辑，需要将某个值传递过来进行判断，返回对应的viewType
        //registerItemProvider()中可以将值传递给ItemProvider

        finishInitialize();
    }

    @Override
    protected int getViewType(Movie movie) {
        //根据实体类判断并返回对应的viewType，具体判断逻辑因业务不同，这里这是简单通过判断type属性
        if (movie.rank % 2 == 0){
            return TYPE_TWO;
        }else {
            return TYPE_ONE;
        }

    }

    @Override
    public void registerItemProvider() {
        //注册相关的条目provider
        mProviderDelegate.registerProvider(new TypeOneItemProvider());
        mProviderDelegate.registerProvider(new TypeTwoItemProvider());
    }
}
