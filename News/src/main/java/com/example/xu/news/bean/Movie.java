package com.example.xu.news.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Xu on 2018/4/10
 */

public class Movie implements Serializable, MultiItemEntity {
    public int rank;
    public String title;
    public int itemType;


    public Movie() {}
    public Movie(int rank, String title) {
        this.title = title;
        this.rank = rank;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
