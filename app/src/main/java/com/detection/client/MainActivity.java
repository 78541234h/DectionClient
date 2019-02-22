package com.detection.client;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.detection.client.service.presenter.DetectPresenter;
import com.detection.client.service.view.DetectView;

import java.io.File;

import java.io.IOException;


import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements DetectView{

    private static final int REQUEST_CODE_IMG=1;
    private Button button;
    private EditText host; //= findViewById(R.id.server_host);
    private EditText port; //= findViewById(R.id.server_port);
    private ImageView imageView;
    private DetectPresenter presenter;
    private String path;
    private ProgressDialog progressDialog;

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;


    //动态获取内存存储权限
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity,new String[] {
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },MY_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        Window window=getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_main);
        host = findViewById(R.id.server_host);
        port = findViewById(R.id.server_port);
        button=findViewById(R.id.select_img);
        imageView=findViewById(R.id.show_img);
        presenter=new DetectPresenter(this,this);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");

                startActivityForResult(intent,REQUEST_CODE_IMG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode==REQUEST_CODE_IMG){
                Uri uri=data.getData();
                String img_url=uri.getPath();
                ContentResolver cr=this.getContentResolver();


                String[] imgPath = {MediaStore.Images.Media.DATA};

                //好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(data.getData(), imgPath, null, null, null);

                //按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();

                //最后根据索引值获取图片路径
                path = cursor.getString(column_index);
                Toast.makeText(MainActivity.this,path,Toast.LENGTH_SHORT).show();
                String serverHost = this.host.getText().toString();
                String serverPort = this.port.getText().toString();
                presenter.detectView(serverHost, serverPort);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void showDialog() {
        progressDialog=ProgressDialog.show(MainActivity.this,
                "",
                "detecting...");
    }

    @Override
    public void cancelDialog() {
        progressDialog.cancel();
    }

    @Override
    public void toastMessage(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public File getFile() {
        return new File(path);
    }

    @Override
    public void putFile(ResponseBody body) {
        byte[] bytes=null;
        Bitmap bitmap=null;
        try {
            bytes=body.bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bytes==null){
            Toast.makeText(MainActivity.this,"bytes=null",Toast.LENGTH_SHORT);
        }else {
            bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            imageView.setImageBitmap(bitmap);
        }
    }
}
