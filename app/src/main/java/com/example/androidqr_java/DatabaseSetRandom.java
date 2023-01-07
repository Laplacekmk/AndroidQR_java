package com.example.androidqr_java;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabaseSetRandom {

    private static final MediaType MIMEType = MediaType.get("application/json; charset=utf-8");
    //判定
    private int frag = 2;

    String GAS_URL;

    private void httpRequest(String url,String json) throws IOException {

        //OkHttpClient生成
        OkHttpClient client = new OkHttpClient();

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

                    Log.i("mmmmmmmm", jsonstr);
                    Log.i("mmmmmmmm", String.valueOf(jsonstr.length()));
                    if (jsonstr == "1") {

                        frag = 1;
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

    DatabaseSetRandom(String url, String id, String random){
        //okhttpを利用するカスタム関数（下記）
        GAS_URL = url;
        Log.i("mmmmm",GAS_URL);
        String json = "{\"mode\":\"setrandom\", " +
                    "\"id\":\"" + id + "\"," +
                    "\"random\":\"" + random + "\"}";
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
}

