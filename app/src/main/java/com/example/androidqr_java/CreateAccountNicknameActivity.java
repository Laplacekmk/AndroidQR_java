package com.example.androidqr_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidqr_java.databinding.ActivityCreateAccountNicknameBinding;

public class CreateAccountNicknameActivity extends AppCompatActivity {

    private ActivityCreateAccountNicknameBinding binding;

    private InputMethodManager inputMethodManager;
    private EditText nicknameEdit;
    private TextView nicknameText;
    private ImageView nicknameImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountNicknameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager) getApplication().getSystemService(getApplication().INPUT_METHOD_SERVICE);

        // ボタン要素を取得
        nicknameEdit = binding.nicknameEdit;
        nicknameEdit.addTextChangedListener(ntwNickname);
        nicknameEdit.setOnKeyListener(vokNickname);
        nicknameText = binding.nicknameNextText;
        nicknameImg = binding.nicknameNextImg;

        // ボタンをクリックした時の処理
        nicknameImg.setOnClickListener(nvoNext);
        nicknameText.setOnClickListener(nvoNext);
    }

    View.OnClickListener nvoNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(nicknameEdit.getText().length() > 0) {
                //内部ストレージにニックネームを保存
                SharedPreferences sharedPref = CreateAccountNicknameActivity.this.getSharedPreferences(getString(R.string.sp_account),getApplication().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_ac_nickname), nicknameEdit.getText().toString());
                editor.apply();
                //キーボードを閉じる
                inputMethodManager.hideSoftInputFromWindow(nicknameEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                Log.i("mmmmm",sharedPref.getString(getString(R.string.sp_ac_nickname),"a"));
                finish();
                Intent intent = new Intent(CreateAccountNicknameActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        }
    };


    View.OnKeyListener vokNickname = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
            if((event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)) {
                //キーボードを閉じる
                inputMethodManager.hideSoftInputFromWindow(nicknameEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                return true;
            }
            return false;
        }
    };
    TextWatcher ntwNickname = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("mmmmmm","async");
            if(nicknameEdit.getText().length() > 0){
                Log.i("mmmmmm","async");
                nicknameText.setTextColor(Color.WHITE);
                nicknameImg.setImageResource(R.drawable.caa_button_completed_black);
            }
            else{
                nicknameText.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
                nicknameImg.setImageResource(0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}