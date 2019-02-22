package com.detection.client.service;



import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by 55462 on 2018/6/27.
 */

public interface RetrofitService {

    @Multipart
    @POST("/api/getDetectedImage")
    Observable<ResponseBody> detectImg(@Part MultipartBody.Part file);

}
