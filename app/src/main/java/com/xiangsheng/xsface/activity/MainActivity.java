package com.xiangsheng.xsface.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xiangsheng.xsface.R;
import com.xiangsheng.xsface.network.UploadUtil;

import java.io.IOException;


public class MainActivity extends Activity{
    private EditText name ;
    private ImageButton img ;
    private int REQUEST_CODE_PHOTO_PICKED = 1;
    private Bitmap bitmap = null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_test);
        name = (EditText)findViewById(R.id.et);
        img = (ImageButton) findViewById(R.id.iv);
        img.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_launcher));
    }

    public void BtnEnd(View view){
        if (bitmap != null && !name.getText().toString().equals("")){
            UploadUtil.mBitmap = bitmap;
            UploadUtil.mName(name.getText().toString());
        }
    }

    //提取图像原数据
    public void Img(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PHOTO_PICKED);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            ContentResolver resolver = getContentResolver();
            Uri originalUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri );
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
