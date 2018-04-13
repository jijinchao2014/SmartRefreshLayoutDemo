package com.example.xu.news;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xu.news.Utils.DynamicTimeFormat;
import com.example.xu.news.Utils.JsonArrayRequest;
import com.example.xu.news.adapter.MovieAdapter;
import com.example.xu.news.adapter.MovieMultipleAdapter;
import com.example.xu.news.base.LazyloadFragment;
import com.example.xu.news.bean.Movie;
import com.example.xu.news.view.BbtreeHeader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Xu on 2018/4/4
 */

public class MyFragment extends LazyloadFragment implements OnRefreshListener {
    @BindView(R.id.refreshLayout)
    RefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private static final String TAG = "MyFragment";
    private String title;
    private String URL_TOP_250 = "http://api.androidhive.info/json/imdb_top_250.php?offset=";
    private int offSet = 0;
    private List<Movie> movieList = new ArrayList<>();
    private MovieMultipleAdapter adapter;

    public static MyFragment newInstance(String code) {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setContentView() {
        return R.layout.frg_news_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (null == bundle) return;
        title = bundle.getString("title");
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setRefreshHeader(new BbtreeHeader(mContext).setTimeFormat(new DynamicTimeFormat("更新于 %s")));
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
//            @NonNull
//            @Override
//            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
//                return new BbtreeHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
//            }
//        });
        adapter = new MovieMultipleAdapter(movieList);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //adapter.setLoadMoreView(xxx); 自定义加载更多样式 使用时可自定义
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });

        //倒数第二条开始预加载（非必须）
        adapter.setPreLoadNumber(2);
        recyclerView.setAdapter(adapter);
    }


    private void setData(boolean isRefresh, List data) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            adapter.setNewData(data);
        } else {
            if (size > 0) {
                adapter.addData(data);
            }
        }
        if (size < 6) {
            //第一页如果不够一页就不显示没有更多数据布局
            adapter.loadMoreEnd(isRefresh);
            Toast.makeText(getContext(), "no more data", Toast.LENGTH_SHORT).show();
        } else {
            adapter.loadMoreComplete();
        }
    }

    private void loadMore(){
        String url = URL_TOP_250 + offSet;

        JsonArrayRequest.getMovieData(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e(TAG, json);
                if (TextUtils.isEmpty(json)) return;

                Type listType = new TypeToken<ArrayList<Movie>>() {
                }.getType();
                Gson gson = new Gson();
                final ArrayList<Movie> movieList = gson.fromJson(json, listType);

                int rank = movieList.get(movieList.size() - 1).rank;
                if (rank >= offSet)
                    offSet = rank;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        setData(false,movieList);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.loadMoreFail();
                    }
                });

            }
        });
    }

    private void fetchMovies() {
        Log.e(TAG, "fetchMovies()");
        // showing refresh animation before making http call

        adapter.setEnableLoadMore(false);
        // appending offset to url
        String url = URL_TOP_250 + offSet;

        JsonArrayRequest.getMovieData(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                swipeRefreshLayout.finishRefresh();
                String json = response.body().string();
                Log.e(TAG, json);
                if (TextUtils.isEmpty(json)) return;

                Type listType = new TypeToken<ArrayList<Movie>>() {
                }.getType();
                Gson gson = new Gson();
                movieList = gson.fromJson(json, listType);

                int rank = movieList.get(movieList.size() - 1).rank;
                if (rank >= offSet)
                    offSet = rank;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setNewData(movieList);
                        adapter.setEnableLoadMore(true);
                        // stopping swipe refresh
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Server Error: " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // stopping swipe refresh
                        swipeRefreshLayout.finishRefresh(false);
                    }
                });
            }
        });
    }

    @Override
    protected void lazyLoad() {
        Log.e(TAG, title + " ===========> lazyLoad()");
        Toast.makeText(mContext, "正在加载数据", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.autoRefresh();
    }



    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        fetchMovies();
    }
}
