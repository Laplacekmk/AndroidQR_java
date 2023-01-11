package com.example.androidqr_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.example.androidqr_java.databinding.ActivityCreateAccountGorlBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateAccountGorLActivity extends AppCompatActivity {

    private ActivityCreateAccountGorlBinding binding;

    //google sign in
    ImageView googleImg;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    final int GS_IN = 1000;
    String Gmail;
    String LineId;
    private String GAS_URL;


    private DatabaseExistence dE;

    //line sign in
    ImageView lineImg;
    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();
    final int LS_IN = 1001;

    ScaleAnimation btnEffect = new ScaleAnimation(
            1.0f, 0.9f, 1.0f, 0.9f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    private TextView nicknameBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountGorlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ボタン要素を取得
        googleImg = binding.golGoogle;
        lineImg = binding.golLine;

        //googleLogIn処理
        //googleSignIn(公式：https://developers.google.com/identity/sign-in/android/sign-in)
        //アカウントの基本情報とemailを取得するようにサインインを構成
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        //googleSignInAPIと対話するためのオブジェクト
        gsc = GoogleSignIn.getClient(getApplicationContext(),gso);

        //url
        GAS_URL = getString(R.string.GAS_URL);

        // ボタンをクリックした時の処理
        googleImg.setOnClickListener(nvoGs);
        lineImg.setOnClickListener(nvoLs);

        //サインインへ戻り
        nicknameBack = binding.caaGroLBackSi;
        nicknameBack.setOnClickListener(nvoBsi);
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
                    Log.i("mmmmmmmmm",String.valueOf(animation));
                    Intent signInIntent = gsc.getSignInIntent();
                    startActivityForResult(signInIntent,GS_IN);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            googleImg.startAnimation(btnEffect);
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
                    Log.i("mmmmmmmmm",String.valueOf(animation));
                    try{
                        // App-to-app login
                        Intent signInIntent = LineLoginApi.getLoginIntent(
                                view.getContext(),
                                getString(R.string.chanelID),
                                new LineAuthenticationParams.Builder()
                                        .scopes(Arrays.asList(Scope.PROFILE))
                                        .build());
                        startActivityForResult(signInIntent, LS_IN);
                    }catch(Exception e) {
                        Log.e("mmmmmmmmmmm", e.toString());
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            lineImg.startAnimation(btnEffect);
        }
    };

    //ログイン処理後の処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //仮idを消去
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.pre_googleSignInCheck),getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getString(R.string.pre_googleSignInCheck));
        editor.commit();
        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.pre_lineSignInCheck),getApplicationContext().MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.remove(getString(R.string.pre_lineSignInCheck));
        editor.commit();

        //googleSignInからの戻りの場合
        if(requestCode == GS_IN){
            //GoogleSignInAccountオブジェクトにはアカウント情報が含まれている
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //SignInが成功しているか確認後、画面遷移
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Gmail = account.getEmail();
                if(account != null) {
                    sharedPref = CreateAccountGorLActivity.this.getSharedPreferences(getString(R.string.pre_googleSignInCheck), getApplication().MODE_PRIVATE);
                    editor = sharedPref.edit();
                    editor.putString(getString(R.string.pre_googleSignInCheck), Gmail);
                    editor.apply();
                    //databaseにすでにアカウントがあればSignIn画面へ戻る
                    dE = new DatabaseExistence(GAS_URL,null,Gmail,null);
                    //判定が出るまでループ--------------------------------
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    ProgressBar progressBar = binding.progressBar;
                    progressBar.setVisibility(View.VISIBLE);
                    binding.caaGorLLoad.setImageResource(R.drawable.loadforeground);
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
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                navigateToSignInActivity();
                return;
            }
        }
        //lineからの戻り
        else if (requestCode == LS_IN) {
            LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
            switch (result.getResponseCode()) {
                case SUCCESS:
                    LineId = result.getLineProfile().getUserId();
                    // Login successful
                    //自動サインイン用にid保存
                    sharedPref = CreateAccountGorLActivity.this.getSharedPreferences(getString(R.string.pre_lineSignInCheck), getApplication().MODE_PRIVATE);
                    editor = sharedPref.edit();
                    editor.putString(getString(R.string.pre_lineSignInCheck), LineId);
                    editor.apply();
                    //databaseにすでにアカウントがあればSignIn画面へ戻る
                    dE = new DatabaseExistence(GAS_URL,null,null,LineId);
                    //判定が出るまでループ--------------------------------
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    ProgressBar progressBar = binding.progressBar;
                    progressBar.setVisibility(View.VISIBLE);
                    binding.caaGorLLoad.setImageResource(R.drawable.loadforeground);
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
                    Toast.makeText(getApplicationContext(), "Login canceled", Toast.LENGTH_SHORT).show();
                    navigateToSignInActivity();
                    break;
                default:
                    // Login canceled due to other error
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    navigateToSignInActivity();
            }
            return;
        }
        else{
            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            navigateToSignInActivity();
            return;
        }
    }

    View.OnClickListener nvoBsi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigateToSignInActivity();
        }
    };

    //ニックネーム画面
    void navigateToSecondActivity(int frag){
        if (frag == 0) {
            finish();
            Intent intent = new Intent(CreateAccountGorLActivity.this,CreateAccountNicknameActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Account already exists", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.pre_lineSignInCheck),getApplication().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(getString(R.string.pre_lineSignInCheck));
            editor.commit();
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            });
            finish();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
    }
    //ログイン画面
    void navigateToSignInActivity(){
        //仮lineIDを消去
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.pre_lineSignInCheck),getApplication().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getString(R.string.pre_lineSignInCheck));
        editor.commit();
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
    }
}