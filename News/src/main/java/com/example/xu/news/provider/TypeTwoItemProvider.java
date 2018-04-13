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
        viewType = MovieMultipleAdapter.TYPE_TWO,
        layout = R.layout.list_row_2
)
public class TypeTwoItemProvider extends BaseItemProvider<Movie,BaseViewHolder> {

    @Override
    public void convert(BaseViewHolder helper, Movie movie, int position) {
        helper.setText(R.id.serial, "Two_"+movie.rank);
        helper.setText(R.id.title, "Two_"+movie.title);
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
