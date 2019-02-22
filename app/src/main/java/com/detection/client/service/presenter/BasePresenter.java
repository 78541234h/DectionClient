package com.detection.client.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.detection.client.service.Manager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 55462 on 2018/6/27.
 */

public class BasePresenter<V> implements Presenter<V> {

    public V mvpView;
    protected Manager manager;
    private CompositeSubscription compositeSubscription;
    protected Context context;

    public BasePresenter(Context context){
        this.context=context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(V view) {
        this.mvpView=view;
        manager=new Manager(context);
    }

    @Override
    public void detachView() {
        this.mvpView=null;
        onUnsubscribe();
    }

    @Override
    public void onUnsubscribe() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    @Override
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (compositeSubscription==null){
            compositeSubscription=new CompositeSubscription();
        }

        compositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
