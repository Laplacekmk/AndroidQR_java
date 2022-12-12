package com.example.androidqr_java;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Script;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
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
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.gson.GsonFactory;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding binding;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount acct;

    private TextView name,email;
    private Button signOutBtn,addBtn,searchBtn;
    private List<String> db_List;

    private final String GA_URL = "https://script.google.com/macros/s/AKfycbwQj776-jZgfMEvd825-u6_jX4sNKvUpJSzIfJ3_vunMvhQmcy4aj9BS9FNp1TXaJxS/exec";
    private static final MediaType MIMEType = MediaType.get("application/json; charset=utf-8");

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
            acct = GoogleSignIn.getLastSignedInAccount(this);

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
        searchBtn.setOnClickListener(nvoSearch);
        //httpとdbから指定した情報を取得
        {


        }
    }

    void httpRequest(String url,String json) throws IOException{

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

                        try {
                            JSONObject db_Json = new JSONObject(jsonstr);
                            final String db_status = db_Json.getString(binding.searchID.getText().toString());
                            JSONObject Json = new JSONObject(db_status);
                            final String status = Json.getString("NAME");
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.getInfo.setText(status);
                                    Log.i("mmmmmmm", status);
                                }
                            });
                        } catch (Exception e) {
                            Log.i("mmmmmm", "String to Json Failure");

                        }
                    }
                    else{
                        String res = String.valueOf(response.isSuccessful());
                        Log.i("mmmmmmmmm", "response is " + res);
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
                    String json = "{\"mode\":\"add\", " +
                                    "\"id\":\""+ editId +"\"," +
                                    "\"name\":\""+ editName +"\"," +
                                    "\"class\":\""+ editClass +"\"" +
                                    "}";
                    httpRequest(GA_URL,json);
                } catch (Exception e) {
                    Log.e("mmmmmmmmmmmm", e.getMessage());
                }
            }
        }
    };
    //Apps Scrips OAuth 認証（未）
    /*
    private static HttpRequestInitializer setHttpTimeout() {
        return new HttpRequestInitializer() {
            final HttpRequestInitializer requestInitializer = new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {

                }
            }
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                requestInitializer.initialize(httpRequest);
                // This allows the API to call (and avoid timing out on)
                // functions that take up to 6 minutes to complete (the maximum
                // allowed script run time), plus a little overhead.
                httpRequest.setReadTimeout(380000);
            }
        };
    }
    public static Script getScriptService() throws IOException {

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory JSON_FACTORY = new GsonFactory();
        return new Script.Builder(
                httpTransport, JSON_FACTORY, setHttpTimeout())
                .setApplicationName("OAuth Laplacekmk")
                .build();
    }*/
    private  View.OnClickListener nvoSearch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                URL url = new URL("https://script.google.com/macros/s/AKfycbxMuuduu-CU0SSAk8rBJ49EGY7cKcgGoQkjGO_0k4Br6yu2KOpvHtZMaS2KDD1ExISF/exec");

                Log.i("mmmmmmm","openconnection ok");
            }catch (Exception e){
                Log.i("mmmmmmm","openconnection bad");
            }

            {
                String editId = binding.searchID.getText().toString();
                if (editId.length() != 0) {
                    try {
                        //okhttpを利用するカスタム関数（下記）
                        String json = "{\"mode\":\"search\", \"id\":\"" + editId + "\"}";
                        httpRequest(GA_URL, json);
                    } catch (Exception e) {
                        Log.e("mmmmmmmmmmmm", e.getMessage());
                    }
                }else{
                    Log.i("mmmmmmmmmmm","no id");
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