package com.example.xu.news.Utils;


import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Xu on 2018/4/10
 */

public class JsonArrayRequest {
    public static void getMovieData(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        Request request = new Request.Builder().url(url).post(formBody.build()).build();
        client.newCall(request).enqueue(callback);
    }
}
