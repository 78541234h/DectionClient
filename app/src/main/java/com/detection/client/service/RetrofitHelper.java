package com.detection.client.service;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 55462 on 2018/6/27.
 */

public class RetrofitHelper {


        public static final String TAG="OkHttp";

        //短缓存1分钟
        public static final int CACHE_AGE_SHORT=60;
        //长缓存有效期1天
        public static final int CACHE_STALE_LONG=60*60*24;
        //base_url
        public static final String BASE_URL="http://192.168.0.4:8084";


        private Context mContext;

        OkHttpClient client=new OkHttpClient();

        GsonConverterFactory factory;

        private static RetrofitHelper instance=null;


        private Retrofit mRetrofit=null;


        public static RetrofitHelper getInstance(Context context, String host, String port){
            if (instance==null){
                instance=new RetrofitHelper(context, host, port);
                if (context==null){
                    Log.e("TOPLYH","context======null");
                }
            }
            return instance;
        }

        private RetrofitHelper(Context context, String host, String port){
            mContext=context;
            init(host, port);
        }

        private void init(String host, String port){
            initOkHttpClient();
            resetApp(host, port);
        }

        private void resetApp(String host, String port){
            Gson gson=new GsonBuilder().create();
            factory=GsonConverterFactory.create(gson);

            //host = "192.168.0.4";
            //port = "8084";
            mRetrofit=new Retrofit.Builder()
                    //.baseUrl(BASE_URL)
                    .baseUrl("http://"+host+":"+port)
                    .client(client)
                    .addConverterFactory(factory)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();


        }

        public RetrofitService getServer(){
            return mRetrofit.create(RetrofitService.class);
        }

        private void initOkHttpClient(){
            File cacheDirectory=new File(mContext.getCacheDir(),"HttpCache");
            Cache cache=new Cache(cacheDirectory,1024*1024*100);

            client=new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30,TimeUnit.SECONDS)
                    .readTimeout(30,TimeUnit.SECONDS)
                    .cache(cache)
                    .build();
        }
}
