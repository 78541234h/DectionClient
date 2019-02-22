package com.detection.client.service.presenter;

import android.content.Context;
import android.widget.Toast;

import com.detection.client.service.view.DetectView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by 55462 on 2018/6/27.
 */

public class DetectPresenter extends BasePresenter<DetectView> {

    private DetectView detectView;
    public DetectPresenter(Context context,DetectView view) {
        super(context);
        attachView(view);
        this.detectView=view;
    }

    public void detectView(String host, String port){
        File file= detectView.getFile();

        RequestBody requestFile=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body=MultipartBody.Part.createFormData("file",file.getName(),requestFile);
        String descriptionString="hello,这是文件描述";

        detectView.showDialog();
        ApiCallBack<ResponseBody> subscriber=new ApiCallBack<ResponseBody>() {

            @Override
            public void onSuccess(ResponseBody model) {
               if (model==null){
                   Toast.makeText(context,"model=null",Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(context,model.toString(),Toast.LENGTH_SHORT).show();
               }
               detectView.putFile(model);
                //Toast.makeText(context, (int) model.contentLength(),Toast.LENGTH_SHORT).show();
               /* Bitmap bitmap=null;

                try {
                    bitmap= BitmapFactory.decodeByteArray(model.bytes(),0,model.bytes().length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap==null){
                    Toast.makeText(context,"bitmap=null",Toast.LENGTH_SHORT).show();
                }else {
                    detectView.putFile(bitmap);
                }*/
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(context,"失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                detectView.cancelDialog();
            }
        };

        addSubscription(manager.detectImg(body, host, port),subscriber);
    }

    public void destroy(){
        detachView();
    }
}
