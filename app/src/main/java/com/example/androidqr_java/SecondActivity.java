package com.example.androidqr_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.example.androidqr_java.databinding.ActivitySecondBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

    private String GAS_URL;
    private TextView name,email;
    private Button signOutBtn,addBtn,searchBtn;

    private static final MediaType MIMEType = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GAS_URL = getString(R.string.GAS_URL);
        name = binding.name;
        email = binding.email;
        signOutBtn = binding.signOut;
        addBtn = binding.buttonAdd;
        searchBtn = binding.buttonSearch;


        //get accountData + googleSignOut
        try {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);
            acct = GoogleSignIn.getLastSignedInAccount(this);

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                name.setText(personName);
                email.setText(personEmail);
            }
        }catch (Exception e){
            Log.i("mmmmmmmm","noaccount");
        }
        signOutBtn.setOnClickListener(nvoSo);

        //dbから取得する情報を入力
        addBtn.setOnClickListener(nvoAdd);
        searchBtn.setOnClickListener(nvoSearch);
    }


    private View.OnClickListener nvoSo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences sharedPref = SecondActivity.this.getSharedPreferences(getString(R.string.sp_account),getApplication().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(getString(R.string.sp_ac_lineID));
            editor.remove(getString(R.string.sp_ac_gmail));
            editor.commit();
            finish();
            startActivity(new Intent(SecondActivity.this, SignInActivity.class));
        }
    };

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
                    httpRequest(GAS_URL,json);
                } catch (Exception e) {
                    Log.e("mmmmmmmmmmmm", e.getMessage());
                }
            }
        }
    };

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
                        httpRequest(GAS_URL, json);
                    } catch (Exception e) {
                        Log.e("mmmmmmmmmmmm", e.getMessage());
                    }
                }else{
                    Log.i("mmmmmmmmmmm","no id");
                }
            }
        }
    };
}