package com.example.xu.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xu.news.R;
import com.example.xu.news.bean.Movie;

import java.util.List;

/**
 * Created by Xu on 2018/4/10
 */

public class MovieAdapter extends BaseMultiItemQuickAdapter<Movie,BaseViewHolder> {
    private String[] bgColors;
    private Context context;
    private List<Movie> movieList;
    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;

    public MovieAdapter(Context c, List<Movie> data) {
        super(data);
        Log.e("MyFragment","MovieAdapter");
        this.context = c;
        bgColors = context.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
        addItemType(TYPE_ONE, R.layout.list_row);
        addItemType(TYPE_TWO, R.layout.list_row);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Movie movie) {
        switch (baseViewHolder.getItemViewType()) {
            case TYPE_ONE:
                baseViewHolder.setText(R.id.serial, "One_"+movie.rank);
                baseViewHolder.setText(R.id.title, "One_"+movie.title);
                String color = bgColors[movie.rank % bgColors.length];
                baseViewHolder.setBackgroundColor(R.id.serial, Color.parseColor(color));
                break;
            case TYPE_TWO:
                baseViewHolder.setText(R.id.serial, String.valueOf(movie.rank));
                baseViewHolder.setText(R.id.title, movie.title);
                String color1 = bgColors[movie.rank % bgColors.length];
                baseViewHolder.setBackgroundColor(R.id.serial, Color.parseColor(color1));
                break;
        }
    }
}
