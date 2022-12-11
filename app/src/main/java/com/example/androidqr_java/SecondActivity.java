package com.example.androidqr_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.URL;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.example.androidqr_java.databinding.ActivitySecondBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding binding;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private TextView name,email;
    private Button signOutBtn,addBtn,searchBtn;
    private List<String> db_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolBar();

        name = binding.name;
        email = binding.email;
        signOutBtn = binding.signOut;
        addBtn = binding.buttonAdd;
        searchBtn = binding.buttonSearch;

        //get accountData + googleSignOut
        {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                name.setText(personName);
                email.setText(personEmail);
            }

            signOutBtn.setOnClickListener(nvoSo);
        }
        //dbから取得する情報を入力
        addBtn.setOnClickListener(nvoAdd);
        //httpとdbから指定した情報を取得
        {


        }
    }

    void httpRequest(URL url) throws IOException{

        //OkHttpClinet生成
        OkHttpClient client = new OkHttpClient();

        //request生成
        Request request = new Request.Builder()
                .url(url)
                .get()
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

                        try {
                            JSONObject db_Json = new JSONObject(jsonstr);
                            final String db_status = db_Json.getString("1");
                            JSONObject Json = new JSONObject(db_status);
                            final String status = Json.getString("NAME");
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("mmmmmmm", status);
                                }
                            });
                        } catch (Exception e) {
                            Log.i("mmmmmm", "String to Json Failure");

                        }
                    }
                    else{
                        Log.i("mmmmmmmmm", "response Failure");
                    }
                }
            });
    }


    private View.OnClickListener nvoSo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                    startActivity(new Intent(SecondActivity.this, MainActivity.class));
                }
            });
        }
    };

    private  View.OnClickListener nvoAdd = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String editId = binding.editId.getText().toString();
            String editName = binding.editName.getText().toString();
            String editClass = binding.editClass.getText().toString();
            if(editId != null) {
                try {
                    //okhttpを利用するカスタム関数（下記）
                    final URL GA_URL = new URL(
                            "https://script.google.com/macros/s/AKfycbxCTr04tOOEQs3C6CVZmEgthgSbhXVPZX6LUk5N0PezgPTyqBlCJxLCzw3OponhDrg7/exec"
                                    + "?mode=add"
                                    + "&id=" + editId
                                    + "&name=" + editName
                                    + "&class=" + editClass );
                    httpRequest(GA_URL);
                } catch (Exception e) {
                    Log.e("mmmmmmmmmmmm", e.getMessage());
                }
            }
        }
    };

    private void toolBar()
    {
        //toolbar名前の変更
        binding.myToolbar.setTitle("Account");

        //toolbarの表示
        setSupportActionBar(binding.myToolbar);
    }
}