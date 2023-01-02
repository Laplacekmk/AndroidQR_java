package com.example.androidqr_java;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;

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

    //topLay
    private ActivityMainBinding binding;
    boolean top_layout_frag = false;
    int translationTop;
    ImageView buttonBatu;
    AnimationDrawable batu_animation;
    FlingAnimation batu_dynamicAnimation;

    //qr
    boolean qrcode_frag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConstraintLayout constraintLayout = binding.maConstraintLayout;
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //topAnimation
        //横移動
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point realSize = new Point();
        disp.getRealSize(realSize);
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();

        translationTop = realSize.x - (int) (60 * metrics.density);
        binding.maTopLayout.setTranslationX(-translationTop);

        batu_dynamicAnimation = new FlingAnimation(binding.maTopLayout, DynamicAnimation.TRANSLATION_X);

        //QR code生成
        final ImageView buttonQR = binding.maQrcodeStroke;
        buttonQR.setOnClickListener(nvoQR);

        //cameraへ移動
        final ImageView buttonCamera = binding.maScanStroke;
        buttonCamera.setOnClickListener(nvoCamera);

        //secondActivityへ遷移
        final ImageView buttonSecond = binding.maInfo;
        buttonSecond.setOnClickListener(nvoSecond);

        //✕ボタン
        buttonBatu = binding.maBatuHam;
        buttonBatu.setOnClickListener(nvoBatu);
    }

    //ボタン処理の変数化
    private View.OnClickListener nvoQR = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(qrcode_frag){
                binding.imageView.setImageResource(R.drawable.aim_trans);
            }
            else {
                //文字列の取得
                //tring text = binding.editText.getText().toString();

                // QRCodeの作成
                Bitmap qrCodeBitmap = createQRCode("a");

                // QRCodeの作成に成功した場合
                if (qrCodeBitmap != null) {
                    // 結果をImageViewに表示
                    binding.imageView.setImageBitmap(qrCodeBitmap);
                }
            }
            qrcode_frag = !qrcode_frag;
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
                    pixels[offset + x] = getColor(R.color.clear);
                }
            }
        }
        // 結果を返す
        return pixels;
    }

    private View.OnClickListener nvoCamera = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, CameraView.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener nvoSecond = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener nvoBatu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            batuAnimation();
            top_layout_frag = !top_layout_frag;
        }
    };

    void batuAnimation(){
        //✕ ⇔ =
        if (top_layout_frag){
            buttonBatu.setBackgroundResource(R.drawable.ma_animation_batu_re);
            batu_dynamicAnimation.setStartVelocity(-3500).setMinValue(-3000).setMaxValue(5000);
        }
        else {
            buttonBatu.setBackgroundResource(R.drawable.ma_animation_batu);
            batu_dynamicAnimation.setStartVelocity(3500).setMinValue(-3000).setMaxValue(5000);
        }
        batu_animation = (AnimationDrawable) buttonBatu.getBackground();
        batu_animation.start();
        batu_dynamicAnimation.start();
    }
}
