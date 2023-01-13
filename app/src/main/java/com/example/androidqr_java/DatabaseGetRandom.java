package com.example.androidqr_java;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabaseGetRandom {

    private static final MediaType MIMEType = MediaType.get("application/json; charset=utf-8");
    //判定
    private int frag = 2;
    String id;

    String GAS_URL;

    private void httpRequest(String url,String json) throws IOException {

        //OkHttpClient生成
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        //request生成
        RequestBody body = RequestBody.create(MIMEType,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                frag = 0;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("mmmmm","kk");
                if(response.isSuccessful()) {
                    Log.i("mmmmmmmmm", "response Successful");

                    final String jsonstr = response.body().string();

                    Log.i("mmmmmmmm", "length:"+jsonstr.length());
                    Log.i("mmmmmmmm", "jsonstr:"+jsonstr);

                    if (jsonstr.length() > 2) {
                        String[] a = jsonstr.split("\"");
                        Log.i("mmmmmmmm", a[1]);
                        frag = 1;
                        id = a[1];
                        Log.i("mmmmm","true");
                    }
                    else {
                        frag = 0;
                    }
                }
                else{
                    String res = String.valueOf(response.isSuccessful());
                    frag = 0;
                }
            }
        });
    }

    DatabaseGetRandom(String url,String random,String id){
        //okhttpを利用するカスタム関数（下記）
        GAS_URL = url;
        String json = "{\"mode\":\"getrandom\", " +
                    "\"random\":\"" + random +"\","+
                    "\"id\":\"" + id +"\"}";
        try {
            httpRequest(GAS_URL, json);
        }catch (IOException e){
            //失敗した時の処理
            frag = 0;
        }
    }

    int getFrag(){
        return frag;
    }
    String getId(){
        return id;
    }
}


