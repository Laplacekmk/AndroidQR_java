package com.example.androidqr_java;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateAccountCheckFragment extends Fragment {

    CreateAccountActivity activity;

    /**
     * C/C++ライブラリを読み込みさせる
     **/
    static {
        System.loadLibrary("ulid");
    }
    String ulid;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount acct;
    String Gmail;
    String LineId;
    String nickname;

    private String GAS_URL;

    private static final MediaType MIMEType = MediaType.get("application/json; charset=utf-8");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 所属している親アクティビティを取得
        activity = (CreateAccountActivity) getActivity();

        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_check_create_account, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCheck_caa);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        );
        recyclerView.setAdapter(new RecyclerViewAdapter_caaCheck(activity));

        // ボタン要素を取得
        ImageView completeImg = view.findViewById(R.id.complete_image);
        TextView completeText = view.findViewById(R.id.complete_text);
        ImageView backImg = view.findViewById(R.id.caa_back_image);

        //id
        ulid = getULID();

        //url
        GAS_URL = getString(R.string.GAS_URL);

        //get accountData + googleSignOut
        SharedPreferences sharedPref = activity.getSharedPreferences(getString(R.string.pre_googleSignInCheck),activity.MODE_PRIVATE);
        Gmail = sharedPref.getString(getString(R.string.pre_googleSignInCheck), null);
        //get LineId
        sharedPref = activity.getSharedPreferences(getString(R.string.pre_lineSignInCheck),activity.MODE_PRIVATE);
        LineId = sharedPref.getString(getString(R.string.pre_lineSignInCheck), null);

        //ニックネーム
        sharedPref = activity.getSharedPreferences(getString(R.string.sp_account),activity.MODE_PRIVATE);
        nickname = sharedPref.getString(getString(R.string.sp_ac_nickname),null);

        // ボタンをクリックした時の処理
        //ホームへ
        completeImg.setOnClickListener(nvoComplete);
        completeText.setOnClickListener(nvoComplete);
        //戻り
        backImg.setOnClickListener(nvoBack);
        return view;
    }

    View.OnClickListener nvoComplete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //内部ストレージに保存
            //databaseに保存する情報を作成
            String itemName = activity.selected_itemList.get(0);
            String itemNumber = activity.selected_itemNumber.get(0);
            for(int i = 1; i < 10; i++) {
               itemName = itemName +"-"+ activity.selected_itemList.get(i);
               itemNumber = itemNumber +"-"+ activity.selected_itemNumber.get(i);
            }
            //保存
            SharedPreferences sharedPref = activity.getSharedPreferences(getString(R.string.sp_account),activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.sp_ac_info_name), itemName);
            editor.putString(getString(R.string.sp_ac_info_number), itemNumber);
            editor.putString(getString(R.string.sp_ac_id), ulid);
            editor.putString(getString(R.string.sp_ac_gmail), Gmail);
            editor.putString(getString(R.string.sp_ac_lineID), LineId);
            editor.putString(getString(R.string.sp_ac_nickname), nickname);
            editor.apply();
            //仮idを消去
            sharedPref = activity.getSharedPreferences(getString(R.string.pre_googleSignInCheck),activity.MODE_PRIVATE);
            editor = sharedPref.edit();
            editor.remove(getString(R.string.pre_googleSignInCheck));
            editor.commit();
            sharedPref = activity.getSharedPreferences(getString(R.string.pre_lineSignInCheck),activity.MODE_PRIVATE);
            editor = sharedPref.edit();
            editor.remove(getString(R.string.pre_lineSignInCheck));
            editor.commit();

            //databaseに追加
            //okhttpを利用するカスタム関数（下記）
            try {
                String json = "{\"mode\":\"" + getString(R.string.add) + "\", " +
                        "\"id\":\"" + ulid + "\"," +
                        "\"nickname\":\"" + nickname + "\"," +
                        "\"info\":\"" + itemNumber + "\"," +
                        "\"gmail\":\"" + Gmail + "\"," +
                        "\"lineId\":\"" + LineId + "\"" +
                        "}";
                httpRequest(GAS_URL, json);
            }catch (IOException e){
                //失敗した時の処理
                Log.i("mmmmm","通信失敗");
            }
            //fragment終了
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack();
            //CreateAccountActivity終了
            activity.finish();
            //homeへ
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener nvoBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            activity.caaFragment_frag = true;
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack();
        }
    };

    void httpRequest(String url,String json) throws IOException {

        //OkHttpClinet生成
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
                Log.i("mmmmmmmmmm","onFailure");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    Log.i("mmmmmmmmm", "response Successful");

                    final String jsonstr = response.body().string();

                    Log.i("mmmmmmmm", jsonstr);
                    if (jsonstr.length() > 2) {
                        try {
                            //返ってきた文字列をjsonにパース
                            JSONObject db_Json = new JSONObject(jsonstr);
                            final String db_status = db_Json.getString("ここにidとかを入れる");
                            JSONObject Json = new JSONObject(db_status);
                            final String status = Json.getString("NAME");
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //パースした文字列をもとにしょり
                                }
                            });
                        } catch (Exception e) {
                            Log.i("mmmmmm", "String to Json Failure");

                        }
                    }
                    else {
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //何も返ってこなかったとき
                            }
                        });
                    }
                }
                else{
                    String res = String.valueOf(response.isSuccessful());
                    Log.i("mmmmmmmmm", "response is " + res);
                }
            }
        });
    }

    /**
     * C/C++のネイティブメソッドを宣言
     **/
    public native String getULID();
}
