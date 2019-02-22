package com.detection.client.service;

import android.content.Context;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by 55462 on 2018/6/27.
 */

public class Manager {
    private RetrofitService retrofitService;
    private Context context;
    private String host = "10.0.2.2";
    private String port="8080";
    public Manager(Context context){
        this.context = context;
        //retrofitService=RetrofitHelper.getInstance(context, host, port).getServer();
    }

    public Observable<ResponseBody> detectImg(MultipartBody.Part file, String host, String port){
        //retrofitService
        return RetrofitHelper.getInstance(this.context, host, port).getServer().detectImg(file);
        //return retrofitService.detectImg(file);
    }
}
