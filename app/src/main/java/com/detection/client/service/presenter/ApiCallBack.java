package com.detection.client.service.presenter;



import android.util.Log;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by 55462 on 2018/6/27.
 */

public abstract class ApiCallBack<M> extends Subscriber<M> {
    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException){
            HttpException httpException=(HttpException) e;
            int code=httpException.code();
            String msg=httpException.getMessage();
            Log.d("dandy","code="+code);
            if (code==504){
                msg="网络不给力";
            }
            if (code==502||code==404){
                msg="服务器异常,请稍后再试";
            }
            onFailure(msg);
        }else {
            onFailure(e.getMessage());
        }
        Log.e("dandy","请求异常了 "+e.toString());
        onFinish();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(M m) {
        onSuccess(m);
        onFinish();
    }
}