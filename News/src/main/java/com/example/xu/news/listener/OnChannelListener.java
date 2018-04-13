package com.example.xu.news.listener;

/**
 * Created by Xu on 2018/4/8
 */

public interface OnChannelListener {
    void onItemMove(int starPos, int endPos);

    void onMoveToMyChannel(int starPos, int endPos);

    void onMoveToOtherChannel(int starPos, int endPos);
}