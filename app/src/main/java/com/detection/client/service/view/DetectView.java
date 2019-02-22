package com.detection.client.service.view;

import java.io.File;

import okhttp3.ResponseBody;

/**
 * Created by 55462 on 2018/6/27.
 */

public interface DetectView extends View {

    File getFile();

    void putFile(ResponseBody body);

}
