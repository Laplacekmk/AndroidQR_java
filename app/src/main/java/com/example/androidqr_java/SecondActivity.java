package com.example.androidqr_java;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.squareup.moshi.Moshi;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
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

    private final String GA_URL = "https://script.googleapis.com/v1/scripts/AKfycbzTTqJ-Tin2b9laNCJNbVVN4r7CLNt4XOPhL52daJ6BIDIx3gPppxi9FAQzu3PXJald:run";
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
        //access token
        {
            try {
                AccessToken token = getScriptService();
                Log.i("mmmmmmmmmm","access ok");
            }catch (IOException e){
                Log.i("mmmmmmmmmm",String.valueOf(e));
            }

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
                        if (jsonstr.length() > 2) {
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
                        else {
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.getInfo.setText("no data");
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
    //Apps Scrips OAuth 認証（残骸）

    private static HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                requestInitializer.initialize(httpRequest);
                httpRequest.setConnectTimeout(3 * 60000);  // 3 minutes connect timeout
                httpRequest.setReadTimeout(3 * 60000);  // 3 minutes read timeout
            }
        };
    }

    public AccessToken getScriptService() throws IOException {
        Log.i("mmmmmmmmmm","access no00");
        File credential_file = new File("C:\\A_school\\team\\AndroidQR_java\\app\\libs\\credentials.json");

        FileInputStream inputStream = openFileInput("libs\\credentials.json");
        Log.i("mmmmmmmmmm", "no12");/*
        GoogleCredentials credential = GoogleCredentials.fromStream(inputStream);
        Log.i("mmmmmmmmmm","access no0");
        credential.refreshIfExpired();
        Log.i("mmmmmmmmmm","access no1");
        AccessToken token = credential.getAccessToken();
        Log.i("mmmmmmmmmm",String.valueOf(token));*/
        return null;
    }


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