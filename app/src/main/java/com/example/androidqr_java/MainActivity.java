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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Color;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.zxing.client.android.Intents;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.MultiFormatWriter;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.google.zxing.MultiFormatWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidqr_java.databinding.ActivityMainBinding;
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


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    final int GS_IN = 1000;

    final int LS_IN = 1001;
    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //ボタンを押してQRcode生成
        final Button buttonQR = binding.buttonQR;
        buttonQR.setOnClickListener(nvoQR);

        //ボタンを推してcameraへ移動
        final Button buttonCamera = binding.camera;
        buttonCamera.setOnClickListener(nvoCamera);

        //googleSignIn(公式：https://developers.google.com/identity/sign-in/android/sign-in)
        //アカウントの基本情報とemailを取得するようにサインインを構成
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        //googleSignInAPIと対話するためのオブジェクト
        gsc = GoogleSignIn.getClient(this,gso);

        //すでにログイン済みの場合はそのまま遷移
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) navigateToSecondActivity();

        //ボタンを配置
        final SignInButton buttonsignin = binding.signInButton;
        buttonsignin.setSize(SignInButton.SIZE_WIDE);
        buttonsignin.setColorScheme(SignInButton.COLOR_DARK);
        buttonsignin.setOnClickListener(nvoGs);

        //line signIn
        LoginButton buttonsignin_line = binding.lineLoginBtn;

        buttonsignin_line.setChannelId("1657747561");

        // configure whether login process should be done by Line App, or inside WebView.
        buttonsignin_line.enableLineAppAuthentication(false);

        // set up required scopes and nonce.
        buttonsignin_line.setAuthenticationParams(new LineAuthenticationParams
                .Builder()
                .scopes(Arrays.asList(Scope.PROFILE))
                // .nonce("<a randomly-generated string>") // nonce can be used to improve security
                .build()
        );
        buttonsignin_line.setLoginDelegate(loginDelegate);
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

        buttonsignin_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    // App-to-app login
                    Intent loginIntent = LineLoginApi.getLoginIntent(
                            view.getContext(),
                            "1657747561",
                            new LineAuthenticationParams.Builder()
                                    .scopes(Arrays.asList(Scope.PROFILE))
                                    // .nonce("<a randomly-generated string>") // nonce can be used to improve security
                                    .build());
                    startActivityForResult(loginIntent, LS_IN);
                }catch(Exception e) {
                    Log.e("mmmmmmmmmmm", e.toString());
                }
            }
        });
    }

    //googleSignInボタン処理
    private  View.OnClickListener nvoGs = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent,GS_IN);
        }
    };

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
                String token_id = account.getIdToken();
                Log.i("mmmmmm",token_id);

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

                    Log.i("mmmmmmmm",accessToken);
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
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    //ボタン処理の変数化
    private View.OnClickListener nvoQR = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //文字列の取得
            String text = binding.editText.getText().toString();

            // QRCodeの作成
            Bitmap qrCodeBitmap = createQRCode(text);

            // QRCodeの作成に成功した場合
            if (qrCodeBitmap != null)
            {
                // 結果をImageViewに表示
                binding.imageView.setImageBitmap(qrCodeBitmap);
            }
        }
    };

    private Bitmap createQRCode(String contents)
    {
        Bitmap qrBitmap = null;
        try {
            // QRコードの生成
            QRCodeWriter qrcodewriter = new QRCodeWriter();
            BitMatrix qrBitMatrix = qrcodewriter.encode(contents,
                    BarcodeFormat.QR_CODE,
                    300,
                    300);

            qrBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            qrBitmap.setPixels(this.createDot(qrBitMatrix), 0, 300, 0, 0, 300, 300);
        }
        catch(Exception ex)
        {
            // エンコード失敗
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            return qrBitmap;
        }
    }

    // ドット単位の判定
    private int[] createDot(BitMatrix qrBitMatrix)
    {
        // 縦幅・横幅の取得
        int width = qrBitMatrix.getWidth();
        int height = qrBitMatrix.getHeight();
        // 枠の生成
        int[] pixels = new int[width * height];

        // データが存在するところを黒にする
        for (int y = 0; y < height; y++)
        {
            // ループ回数盤目のOffsetの取得
            int offset = y * width;
            for (int x = 0; x < width; x++)
            {
                // データが存在する場合
                if (qrBitMatrix.get(x, y))
                {
                    pixels[offset + x] = Color.BLACK;
                }
                else
                {
                    pixels[offset + x] = Color.WHITE;
                }
            }
        }
        // 結果を返す
        return pixels;
    }

    private View.OnClickListener nvoCamera = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplication(), CameraView.class);
            startActivity(intent);
        }
    };
}