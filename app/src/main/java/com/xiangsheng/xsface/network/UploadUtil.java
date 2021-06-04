package com.xiangsheng.xsface.network;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.xiangsheng.xsface.url.AppConfig.url;


/**
 * Created by Administrator on 2018/7/12.
 */

public class UploadUtil {
    private static Context mContext;
    private static ImageView mImageView;
    public static Bitmap mBitmap;
//    public static AFR_FSDKFace face;
    private static final String TAG = "upload";

    private static RequestBody requestBody;
    private static InputStream is;
    private static ByteArrayOutputStream outputStream;
    public static String s = null;

    public UploadUtil(Context context, ImageView imageview) {
        this.mContext = context;
        this.mImageView = imageview;
    }

    public static void mName(String name) {
        new NetworkTask().execute(name);
    }

    private int x;

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    static class NetworkTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doPost(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!"error".equals(result)) {
//                Log.i(TAG, "图片地址 " + AppConfig.url + result);
                Glide.with(mContext).load(url + result).into(mImageView);
            }
        }
    }

    private static String doPost(String name) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //问题在于api里面处理的时间比较久，而我所创建的 OkHttpClient 客户端并没有给足够的时间取处理，因此需要设置更长的连接和读取时间，延长至50秒，可顺利通过。代码如下所示：
//        OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder().connectTimeout(50000, TimeUnit.MILLISECONDS)
//                .readTimeout(50000, TimeUnit.MILLISECONDS)
//                .build();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
//        builder.addFormDataPart("userId", "20160519142605");
//        builder.addFormDataPart("name",imagePath);
//        if (name.equals("C#")) {
//            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                    .addFormDataPart("name", name).build();
//        }
//        else
//        {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (mBitmap != null)
        {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            RequestBody image_data = RequestBody.create(MediaType.parse("application/octet-stream"), data);
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("image", System.currentTimeMillis() + ".jpg", image_data).build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response;
//        HttpServerConnection response;
            try {
                response = mOkHttpClient.newCall(request).execute();
                if (response.message().toString().equals("OK")){
                    System.out.print("发送成功");
                }else {
                    System.out.print("发送失败");
                }
//            onResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void onResponse(Response response) throws IOException {


        String root = Environment.getExternalStorageDirectory().toString();

//        byte[] photo = response.body().bytes();
        Log.d("log", "数据response:");
        String json = response.body().string();
        Log.d("log", "数据res:" + json.toString());
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        String path = root + "/" + dateFormat.format(date) + ".png";
//        String path = root + "/" + i + ".png";
//        byte2image(photo, path);

    }

    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals(""))
            return;
        try {
            FileOutputStream imageOutput = new FileOutputStream(path, true);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }

}
