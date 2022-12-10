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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding binding;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private TextView name,email;
    private Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = binding.name;
        email = binding.email;
        signOutBtn = binding.signOut;

        //googleSignOut
        {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getId();
                String personEmail = acct.getIdToken();
                name.setText(personName);
                email.setText(personEmail);
            }

            signOutBtn.setOnClickListener(nvoSo);
        }
        //httpリクエスト
        try{
            //okhttpを利用するカスタム関数（下記）
            final URL GA_URL = new URL("https://script.google.com/macros/s/AKfycbyYQsYyqOxk6_J2fvUHEnnSJqA_Y-71AVEJ-LGvIL4/dev");
            httpRequest(GA_URL);
        }catch(Exception e){
            Log.e("mmmmmmmmmmmm",e.getMessage());
        }
/*
        //cloud database
        // Set up URL parameters
        Log.i("mmmmmmmm","ok6");
        Properties connProps = new Properties();
        connProps.setProperty("user", "laplacekmk"); // iam-user@gmail.com
        connProps.setProperty("sslmode", "disable");
        connProps.setProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
        connProps.setProperty("cloudSqlInstance", "laplacekmk:asia-northeast2:laplace-kmk2022");
        connProps.setProperty("enableIamAuth", "true");
        Log.i("mmmmmmmm","ok5");

        new DownloadFilesTask().execute();


        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Log.i("mmmmmmmm","ok");

                Connection conn = connectionPool.getConnection();

                PreparedStatement ps = conn.prepareStatement("select * from user");

                ResultSet rs = ps.executeQuery();
                Log.i("mmmmmmmm","ok2");

                int i = rs.getInt("id");

                String s = String.valueOf(i);
                Log.i("mmmmmmm",s);

            }catch (Exception e){

            }
        }
*/
    }

    void httpRequest(URL url) throws IOException{

        //OkHttpClinet生成
        OkHttpClient client = new OkHttpClient();

        //request生成
        Request request = new Request.Builder()
                .url(url)
                .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("mmmmmmmmmm","no1");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()) Log.i("mmmmmmmmm", "ok");

                    final String jsonstr = response.body().string();

                    Log.i("mmmmmmmm",jsonstr);

                    try {
                        Log.i("mmmmmmm","0");
                        JSONObject json = new JSONObject(jsonstr);
                        Log.i("mmmmmmm","1");
                        final String status = json.getString("status");
                        Log.i("mmmmmmm","2");
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        Log.i("mmmmmmm","3");
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("mmmmmmm", status);
                            }
                        });
                    } catch (Exception e) {
                        Log.i("mmmmmm","no2");

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
}