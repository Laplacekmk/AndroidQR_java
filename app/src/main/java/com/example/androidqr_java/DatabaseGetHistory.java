package com.example.androidqr_java;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabaseGetHistory {

    private static final MediaType MIMEType = MediaType.get("application/json; charset=utf-8");
    //判定
    private int frag = 2;

    private String GAS_URL;
    private String othersID;
    private List<String> id = new ArrayList<String>();
    private List<String> nickname = new ArrayList<String>();;
    private List<String> info = new ArrayList<String>();;

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
                            final String countS = db_Json.getString("count");
                            int countI = Integer.valueOf(countS);
                            String json_history;
                            for(int i = 0; i < countI; i++){
                                json_history = db_Json.getString(String.valueOf(i));
                                JSONObject json = new JSONObject(json_history);
                                id.add(json.getString("id"));
                                nickname.add(json.getString("nickname"));
                                info.add(json.getString("info"));
                            }
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

    DatabaseGetHistory(String url, String id){
        //okhttpを利用するカスタム関数（下記）
        this.GAS_URL = url;
        this.othersID = id;
    }

    void SetHistory(){
        //okhttpを利用するカスタム関数（下記）
        String json;
        json = "{\"mode\":\"getHistory\", " +
               "\"othersId\":\"" + othersID + "\"" +
               "}";
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
    List<String> getOthersId(){
        return  id;
    }
    List<String> getOthersNickname(){
        return  nickname;
    }
    List<String> getOthersInfo(){
        return  info;
    }
}
