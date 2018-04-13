package com.example.xu.news.provider;

import android.graphics.Color;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.annotation.ItemProviderTag;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.example.xu.news.R;
import com.example.xu.news.adapter.MovieMultipleAdapter;
import com.example.xu.news.bean.Movie;

/**
 * https://github.com/chaychan
 * @author ChayChan
 * @description: Text Img ItemProvider
 * @date 2018/3/30  11:39
 */

@ItemProviderTag(
        viewType = MovieMultipleAdapter.TYPE_ONE,
        layout = R.layout.list_row
)
public class TypeOneItemProvider extends BaseItemProvider<Movie,BaseViewHolder> {

    @Override
    public void convert(BaseViewHolder helper, Movie movie, int position) {
        helper.setText(R.id.serial, "One_"+movie.rank);
        helper.setText(R.id.title, "One_"+movie.title);
        String[] bgColors = mContext.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
        String color = bgColors[movie.rank % bgColors.length];
        helper.setBackgroundColor(R.id.serial, Color.parseColor(color));
    }

    @Override
    public void onClick(BaseViewHolder helper, Movie data, int position) {
        Toast.makeText(mContext, data.title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(BaseViewHolder helper, Movie data, int position) {
        return true;
    }
}
