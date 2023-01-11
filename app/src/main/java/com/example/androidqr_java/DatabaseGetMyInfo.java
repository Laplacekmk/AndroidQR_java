package com.example.androidqr_java;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabaseGetMyInfo {

    private static final MediaType MIMEType = MediaType.get("application/json; charset=utf-8");
    //判定
    private int frag = 2;

    private String GAS_URL;
    private String id;
    private String nickname;
    private String info;
    private String gmail;
    private String lineID;

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

                    if (jsonstr.length() > 2) {
                        try {
                            JSONObject db_Json = new JSONObject(jsonstr);
                            final String db_status = db_Json.getString("myInfo");
                            JSONObject Json = new JSONObject(db_status);
                            id = Json.getString("id");
                            nickname = Json.getString("nickname");
                            info = Json.getString("info");
                            frag=1;
                        } catch (Exception e) {
                            Log.i("mmmmmm", "String to Json Failure");
                            frag=0;
                        }

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

    DatabaseGetMyInfo(String url, String id, String gmail, String lineID){
        //okhttpを利用するカスタム関数（下記）
        this.GAS_URL = url;
        this.id = id;
        this.gmail = gmail;
        this.lineID = lineID;
    }

    void SetMyInfo(){
        //okhttpを利用するカスタム関数（下記）
        String json;
        if(id != null){
            json = "{\"mode\":\"getMyInfo\", " +
                    "\"id\":\"" + id + "\"" +
                    "}";
        }
        else if (gmail != null){
            json = "{\"mode\":\"getMyInfo\", " +
                    "\"gmail\":\"" + gmail + "\""+
                    "}";
        }
        else{
            Log.i("mmmmm","jj");
            json = "{\"mode\":\"getMyInfo\", " +
                    "\"lineId\":\"" + lineID + "\"" +
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
    String getId(){
        return  id;
    }
    String getNickname(){
        return  nickname;
    }
    String getInfo(){
        return  info;
    }
}
