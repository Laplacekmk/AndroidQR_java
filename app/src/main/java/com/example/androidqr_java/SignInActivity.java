package com.example.androidqr_java;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.zxing.BarcodeFormat;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidqr_java.databinding.ActivitySignInBinding;
import com.linecorp.linesdk.Constants;
import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.LoginListener;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.linecorp.linesdk.widget.LoginButton;

import java.net.URL;
import java.util.Arrays;


public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    //google sign in
    ImageView buttonGoogle;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    final int GS_IN = 1000;
    String gmail;

    //line sign in
    ImageView buttonLINE;
    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();
    final int LS_IN = 1001;
    String lineID;

    ScaleAnimation btnEffect = new ScaleAnimation(
            1.0f, 0.9f, 1.0f, 0.9f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    String GAS_URL;

    private DatabaseExistence dE;

    boolean sia_frag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //url
        GAS_URL = getString(R.string.GAS_URL);

        //progressbar
        ProgressBar progressBar = binding.progressBar;

        //googleSignIn(公式：https://developers.google.com/identity/sign-in/android/sign-in)
        //アカウントの基本情報とemailを取得するようにサインインを構成
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        //googleSignInAPIと対話するためのオブジェクト
        gsc = GoogleSignIn.getClient(this,gso);
        //すでにログイン済みの場合はそのまま遷移
        //google
        SharedPreferences sharedPref_google = SignInActivity.this.getSharedPreferences(getString(R.string.sp_account),getApplication().MODE_PRIVATE);
        String account_google = sharedPref_google.getString(getString(R.string.sp_ac_gmail), null);
        //line
        SharedPreferences sharedPref = SignInActivity.this.getSharedPreferences(getString(R.string.sp_account),getApplication().MODE_PRIVATE);
        String account_line = sharedPref.getString(getString(R.string.sp_ac_lineID), null);
        //database確認
        if(account_google != null || account_line != null) {
            navigateToSecondActivity(1);
        }
        //ボタンのレイアウトと配置
        buttonGoogle = binding.googleLogin;
        buttonGoogle.setOnClickListener(nvoGs);

        //line signIn(公式：https://developers.line.biz/ja/docs/android-sdk/integrate-line-login/)
        //LineLogin処理
        buttonLINE = binding.lineLogin;
        buttonLINE.setOnClickListener(nvoLs);

        //createAccount処理
        TextView caaText = binding.createAccount;
        caaText.setOnClickListener(nvoCaa);
    }

    //googleSignInボタン処理
    private  View.OnClickListener nvoGs = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btnEffect.setDuration(400);
            btnEffect.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.i("mmmmmmmmm", String.valueOf(animation));
                    Intent signInIntent = gsc.getSignInIntent();
                    startActivityForResult(signInIntent, GS_IN);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            buttonGoogle.startAnimation(btnEffect);
        }
    };

    //lineSignInボタン処理
    private  View.OnClickListener nvoLs = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btnEffect.setDuration(400);
            btnEffect.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.i("mmmmmmmmm", String.valueOf(animation));
                    try {
                        // App-to-app login
                        Intent signInIntent = LineLoginApi.getLoginIntent(
                                view.getContext(),
                                getString(R.string.chanelID),
                                new LineAuthenticationParams.Builder()
                                        .scopes(Arrays.asList(Scope.PROFILE))
                                        .build());
                        startActivityForResult(signInIntent, LS_IN);
                    } catch (Exception e) {
                        Log.e("mmmmmmmmmmm", e.toString());
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            buttonLINE.startAnimation(btnEffect);
        }
    };

    //ログイン処理後の処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //googleSignInからの戻りの場合
        if(requestCode == GS_IN){
            //GoogleSignInAccountオブジェクトにはアカウント情報が含まれている
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //SignInが成功しているか確認後、画面遷移
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    gmail = account.getEmail();

                    dE = new DatabaseExistence(GAS_URL,null,gmail,null);
                    //判定が出るまでループ--------------------------------
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    ProgressBar progressBar = binding.progressBar;
                    progressBar.setVisibility(View.VISIBLE);
                    binding.asiLoad.setImageResource(R.drawable.loadforeground);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            if(dE.getFrag() != 2){
                                navigateToSecondActivity(dE.getFrag());
                            }
                            else {
                                mainHandler.postDelayed(this, 100);
                            }
                        }
                    };
                    mainHandler.post(r);
                    //-------------------------------------------------
                }
                return;
            }catch (ApiException e){
                Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //lineからの戻り
        if (requestCode == LS_IN) {
            LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
            switch (result.getResponseCode()) {
                case SUCCESS:
                    // Login successful
                    lineID = result.getLineProfile().getUserId();
                    //database
                    dE = new DatabaseExistence(GAS_URL,null,null,lineID);
                    //判定が出るまでループ--------------------------------
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    ProgressBar progressBar = binding.progressBar;
                    progressBar.setVisibility(View.VISIBLE);
                    binding.asiLoad.setImageResource(R.drawable.loadforeground);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            if(dE.getFrag() != 2){
                                navigateToSecondActivity(dE.getFrag());
                            }
                            else {
                                mainHandler.postDelayed(this, 100);
                            }
                        }
                    };
                    mainHandler.post(r);
                    //-------------------------------------------------
                    break;
                case CANCEL:
                    // Login canceled by user
                    Log.i("mmmmmmmmm", "LINE Login Canceled by user.");
                    break;
                default:
                    // Login canceled due to other error
                    Log.i("mmmmmmmm", "Login FAILED!");
                    Log.i("mmmmmmmmm", result.getErrorData().toString());
            }
            return;
        }
        else if (requestCode != LS_IN){
            Log.i("mmmmmmmmm", "Unsupported Request");
            return;
        }
    }

    //ログイン後画面へ
    void navigateToSecondActivity(int frag){
        if(frag == 1) {
            //自動サインイン用にid保存
            SharedPreferences sharedPref = SignInActivity.this.getSharedPreferences(getString(R.string.sp_account),getApplication().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.sp_ac_lineID), lineID);
            editor.putString(getString(R.string.sp_ac_gmail), gmail);
            editor.apply();
            finish();
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            binding.asiLoad.setImageResource(0);
            binding.progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Undefined your account", Toast.LENGTH_SHORT).show();
        }
    }

    //アカウント作成画面へ遷移
    private View.OnClickListener nvoCaa = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            Intent intent = new Intent(SignInActivity.this,CreateAccountGorLActivity.class);
            startActivity(intent);
        }
    };
}