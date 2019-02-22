package com.detection.client.service.presenter;

import android.content.Intent;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 55462 on 2018/6/27.
 */

public interface Presenter<V> {

    void onCreate();

    void onStart();

    void pause();

    void attachView(V view);

    void detachView();

    void onUnsubscribe();

    void attachIncomingIntent(Intent intent);

    void addSubscription(Observable observable, Subscriber subscriber);
}
