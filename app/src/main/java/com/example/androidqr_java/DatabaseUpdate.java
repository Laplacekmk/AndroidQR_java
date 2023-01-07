package com.example.androidqr_java;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabaseUpdate {

        private static final MediaType MIMEType = MediaType.get("application/json; charset=utf-8");
        //判定
        private int frag = 2;

        private String GAS_URL;
        private String id;
        private String nickname;
        private String info;

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
                    if(response.isSuccessful()) {
                        Log.i("mmmmmmmmm", "response Successful");

                        final String jsonstr = response.body().string();
                        Log.i("mmmmm",jsonstr);

                        if (jsonstr.equals("1")) {
                            Log.i("mmmmm","ok");
                            frag=1;
                        }
                        else {
                            Log.i("mmmmm","no");
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

        DatabaseUpdate(String url, String id, String nickname, String info){
            //okhttpを利用するカスタム関数（下記）
            this.GAS_URL = url;
            this.id = id;
            this.nickname = nickname;
            this.info = info;
        }

        void update(){
            //okhttpを利用するカスタム関数（下記）
            Log.i("mmmmm",GAS_URL);
            String json;
            if(nickname != null) {
                json = "{\"mode\":\"update\", " +
                        "\"id\":\"" + id + "\"," +
                        "\"nickname\":\"" + nickname + "\"" +
                        "}";
            }
            else {
                json = "{\"mode\":\"update\", " +
                        "\"id\":\"" + id + "\"," +
                        "\"info\":\"" + info + "\"" +
                        "}";
            }
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
        void setFrag(int i){
            frag = i;
        }
    }
