package com.example.androidqr_java;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateAcountNicknameFragment extends Fragment {

    private InputMethodManager inputMethodManager;
    private CreateAccountActivity activity;
    private EditText nicknameEdit;
    private TextView nicknameText;
    private ImageView nicknameImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 所属している親アクティビティを取得
        activity = (CreateAccountActivity) getActivity();

        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.frabment_nickname_create_account, container, false);

        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);

        // ボタン要素を取得
        nicknameEdit = view.findViewById(R.id.nickname_edit);
        nicknameEdit.addTextChangedListener(ntwNickname);
        nicknameEdit.setOnKeyListener(vokNickname);
        nicknameText = view.findViewById(R.id.nickname_next_text);
        nicknameImg = view.findViewById(R.id.nickname_next_img);

        // ボタンをクリックした時の処理
        nicknameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nicknameEdit.getText().length() > 0) {
                    activity.Nickname = nicknameEdit.getText().toString();
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(nicknameEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    activity.caaFragment_frag = true;
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });
        nicknameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nicknameEdit.getText().length() > 0) {
                    activity.Nickname = nicknameEdit.getText().toString();
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(nicknameEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    activity.caaFragment_frag = true;
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });
        return view;
    }

    View.OnKeyListener vokNickname = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
            if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
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
                nicknameText.setTextColor(Color.BLACK);
                nicknameImg.setImageResource(R.drawable.caa_button_completeb);
            }
            else{
                nicknameText.setTextColor(ContextCompat.getColor(activity,R.color.gray));
                nicknameImg.setImageResource(0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}

