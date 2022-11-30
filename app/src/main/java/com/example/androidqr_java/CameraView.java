package com.example.androidqr_java;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.androidqr_java.databinding.ViewCameraBinding;

public class CameraView extends AppCompatActivity {

    private ViewCameraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolBar();
    }

    private void toolBar()
    {
        //toolbarの表示
        setSupportActionBar(binding.myToolbar);

        //ツールバーに対応するActionBarを取得
        ActionBar ab = getSupportActionBar();

        //名前の変更
        ab.setTitle(R.string.camera);

        //アップボタンの実装
        ab.setDisplayHomeAsUpEnabled(true);
    }
}