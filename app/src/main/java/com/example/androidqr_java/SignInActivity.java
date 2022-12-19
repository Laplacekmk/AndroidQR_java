package com.example.androidqr_java;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
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
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    final int GS_IN = 1000;

    //line sign in
    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();
    final int LS_IN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //googleSignIn(公式：https://developers.google.com/identity/sign-in/android/sign-in)
        //アカウントの基本情報とemailを取得するようにサインインを構成
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        //googleSignInAPIと対話するためのオブジェクト
        gsc = GoogleSignIn.getClient(this,gso);
        //すでにログイン済みの場合はそのまま遷移
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) navigateToSecondActivity();
        //ボタンのレイアウトと配置
        final SignInButton buttonsignin = binding.signInButton;
        buttonsignin.setSize(SignInButton.SIZE_WIDE);
        buttonsignin.setColorScheme(SignInButton.COLOR_DARK);
        buttonsignin.setOnClickListener(nvoGs);

        //line signIn(公式：https://developers.line.biz/ja/docs/android-sdk/integrate-line-login/)
        LoginButton buttonsignin_line = binding.lineLoginBtn;
        buttonsignin_line.setChannelId(String.valueOf(R.string.chanelID));
        //ログイン処理をLINEアプリで行うか、WebView内で行うかを設定します。
        buttonsignin_line.enableLineAppAuthentication(false);
        // 必要なスコープ(情報)を設定。今回の場合は、基本的なプロフィールのみ
        buttonsignin_line.setAuthenticationParams(new LineAuthenticationParams
                .Builder()
                .scopes(Arrays.asList(Scope.PROFILE))
                .build()
        );
        //ログインの委任を設定
        buttonsignin_line.setLoginDelegate(loginDelegate);
        //ログイン結果を取得するように設定
        buttonsignin_line.addLoginListener(new LoginListener() {
            @Override
            public void onLoginSuccess(@NonNull LineLoginResult result) {
                Log.i("mmmmmmmmm","line ok");
            }

            @Override
            public void onLoginFailure(@Nullable LineLoginResult result) {
                Log.i("mmmmmmmmm","line no");
            }
        });
        //ボタン処理
        buttonsignin_line.setOnClickListener(nvoLs);
    }

    //googleSignInボタン処理
    private  View.OnClickListener nvoGs = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent,GS_IN);
        }
    };

    //lineSignInボタン処理
    private  View.OnClickListener nvoLs = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try{
                // App-to-app login
                Intent loginIntent = LineLoginApi.getLoginIntent(
                        view.getContext(),
                        String.valueOf(R.string.chanelID),
                        new LineAuthenticationParams.Builder()
                                .scopes(Arrays.asList(Scope.PROFILE))
                                .build());
                startActivityForResult(loginIntent, LS_IN);
            }catch(Exception e) {
                Log.e("mmmmmmmmmmm", e.toString());
            }
        }
    };

    //ログイン処理後の処理
    @Override
    protected void onActivityResult(int requestcode, int resultCode, Intent data){
        super.onActivityResult(requestcode, resultCode, data);

        //googleSignInからの戻りの場合
        if(requestcode == GS_IN){
            //GoogleSignInAccountオブジェクトにはアカウント情報が含まれている
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //SignInが成功しているか確認後、画面遷移
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null)navigateToSecondActivity();
            }catch (ApiException e){
                Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            }
        }

        //lineからの戻り
        if (requestcode == LS_IN) {
            LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
            Log.i("mmmmmmmm","nonoise.zero@gmail.com");
            switch (result.getResponseCode()) {
                case SUCCESS:
                    // Login successful
                    String accessToken = result.getLineCredential().getAccessToken().getTokenString();
                    navigateToSecondActivity();
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
        }
        else if (requestcode != LS_IN){
            Log.i("mmmmmmmmm", "Unsupported Request");
            return;
        }
    }

    //ログイン後画面へ
    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(SignInActivity.this, SecondActivity.class);
        startActivity(intent);
    }
}