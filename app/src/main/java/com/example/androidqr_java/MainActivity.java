package com.example.androidqr_java;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.BarcodeFormat;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidqr_java.databinding.ActivityMainBinding;
import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    /**
     * C/C++ライブラリを読み込みさせる
     **/
    static {
        System.loadLibrary("ulid");
    }
    /**
     * C/C++のネイティブメソッドを宣言
     **/

    private String account_id;
    private SharedPreferences sharedPref;

    //ナビゲーションのボタン
    private Button goQR;
    private Button goMyList;
    private Button goOthersList;
    private Button goAccountChange;
    private Button goSignOut;
    //ホームのボタン
    private ImageView homeQR;
    private ImageView homeCAMERA;
    //マイリストのボタンok

    //履歴のボタン

    //アカウント切り替えボタンok
    //サインアウトボタンok
    //google sign in
    ImageView buttonGoogle;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount acct;
    final int GS_IN = 1000;
    String gmail;
    //line sign in
    ImageView buttonLINE;
    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();
    final int LS_IN = 1001;
    String lineID;
    private DatabaseGetMyInfo dG;

    //topLay
    private AnimationDrawable animationDrawable;
    //homeのアニメーション右左の切り替え
    boolean ma_home_frag = true;
    private ActivityMainBinding binding;
    //✕ボタン無効状態の切り替え
    boolean top_layout_frag = true;
    private ImageView buttonBatu;
    private AnimationDrawable batu_animation;
    private FlingAnimation batu_dynamicAnimation;

    //url
    private String GAS_URL;
    //randomの暗号
    private String ULID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //url
        GAS_URL = getString(R.string.GAS_URL);

        //google
        //get accountData + googleSignOut
        try {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);
            acct = GoogleSignIn.getLastSignedInAccount(this);
            gsc.signOut()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
        }catch (Exception e){
            Log.i("mmmmmmmm","noaccount");
        }

        //backのグラデーション
        ConstraintLayout constraintLayout = binding.maConstraintLayout;
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //
        sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.sp_account), getApplication().MODE_PRIVATE);
        account_id = sharedPref.getString(getString(R.string.sp_ac_id), null);

        //ニックネーム
        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager) getApplication().getSystemService(getApplication().INPUT_METHOD_SERVICE);
        binding.maNicknamePen.setOnClickListener(nvoMNP);
        binding.maNicknameCheck.setOnClickListener(nvoMNC);
        //ニックネームをセット
        String account_nickname = sharedPref.getString(getString(R.string.sp_ac_nickname), null);
        binding.maNickname.setText(account_nickname);

        //topAnimation
        //横移動
        batu_dynamicAnimation = new FlingAnimation(binding.maTopLayout, DynamicAnimation.TRANSLATION_X);
        batu_dynamicAnimation.setFriction(0.85f);

        //✕ボタン
        buttonBatu = binding.maBatuHam;
        buttonBatu.setOnClickListener(nvoBatu);

        //hoeViewセット
        set_ma_home();

        //ナビゲーションのボタン
        binding.maQrScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("mmmmmm", "qrScreen");
                set_ma_home();
            }
        });

        binding.maMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_ma_myList();
            }
        });
        sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.sp_account), getApplication().MODE_PRIVATE);
        String number = sharedPref.getString(getString(R.string.sp_ac_info_number), null);
        mc = new MapCreate(number);
        titleList =mc.getSelectedTitle();
        itemList = mc.getSelectedItem();
        numberList = mc.getSelectedNumber();
        item_map_name = mc.getItemMapName();
        item_map_frag = mc.getItemMapFrag();

        binding.maOthersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.maGoogle.setOnClickListener(nvoGs);
        binding.maLine.setOnClickListener(nvoLs);

        binding.maSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.sp_ac_id));
                editor.remove(getString(R.string.sp_ac_lineID));
                editor.remove(getString(R.string.sp_ac_gmail));
                editor.remove(getString(R.string.sp_ac_nickname));
                editor.remove(getString(R.string.sp_ac_info_number));
                editor.commit();
                finish();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        });
    }

    //画面遷移関係------------------------------------------------------------
    void set_ma_home () {
        binding.maHome.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_main_home, binding.maHome);
        //QR code生成
        homeQR = findViewById(R.id.ma_qrcode_stroke);
        homeQR.setOnClickListener(nvoQR);

        //cameraへ移動
        homeCAMERA = findViewById(R.id.ma_scan_stroke);
        homeCAMERA.setOnClickListener(nvoCamera);

        //secondActivityへ遷移
        //final ImageView buttonSecond_ = findViewById(R.id.ma_info);
        //buttonSecond_.setOnClickListener(nvoSecond);
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
    //画面遷移animation
    TranslateAnimation translateAnimation;
    private void startTranslate(ConstraintLayout constraintLayout,int type) {
        // 設定を切り替え可能
        if (type == 1) {
            translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
        } else if (type == 2) {
            translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else if (type == 3) {
            translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
        } else if (type == 4) {
            translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
        }

        // animation時間 msec
        translateAnimation.setDuration(700);
        // 繰り返し回数
        translateAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        translateAnimation.setFillAfter(true);
        //アニメーションの開始
        constraintLayout.startAnimation(translateAnimation);
    }
    private View.OnClickListener nvoBatu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(binding.maNicknameEdit.getVisibility() == View.VISIBLE){
                //キーボードを閉じる
                inputMethodManager.hideSoftInputFromWindow(binding.maNicknameEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                binding.maNicknameEdit.setText(null);
                binding.maNicknameEdit.setVisibility(View.INVISIBLE);
                binding.maNicknameCheck.setVisibility(View.INVISIBLE);
                binding.maNickname.setVisibility(View.VISIBLE);
                binding.maNicknamePen.setVisibility(View.VISIBLE);
            }
            if(top_layout_frag) {
                if (ma_home_frag) {
                    buttonBatu.setBackgroundResource(R.drawable.ma_animation_batu);
                    batu_dynamicAnimation.setStartVelocity(-4700).setMinValue(-10000).setMaxValue(10000);
                    //startTranslate(binding.maTopLayout,1);
                    startTranslate(binding.maHome, 2);
                    homeQR.setEnabled(false);
                } else {
                    buttonBatu.setBackgroundResource(R.drawable.ma_animation_batu_re);
                    batu_dynamicAnimation.setStartVelocity(4700).setMinValue(-5000).setMaxValue(7000);
                    //startTranslate(binding.maTopLayout,3);
                    startTranslate(binding.maHome, 4);
                    homeQR.setEnabled(true);
                }
                //batuAnimation();
                batu_animation = (AnimationDrawable) buttonBatu.getBackground();
                batu_animation.start();
                batu_dynamicAnimation.start();
                ma_home_frag = !ma_home_frag;
                //2秒間操作不能に-------------------------------
                top_layout_frag = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        top_layout_frag = true;
                    }
                },1500);
                //-------------------------------
            }
        }
    };
    void batuAnimation(){
        //✕ ⇔ =
        if (top_layout_frag){
            //binding.maConstraintLayout.setForeground(null);
            batu_dynamicAnimation.setStartVelocity(2600).setMinValue(-10000).setMaxValue(7000);
        }
        else {
            //binding.maHome.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(),R.color.clearBlack)));
            batu_dynamicAnimation.setStartVelocity(-2600).setMinValue(-10000).setMaxValue(1000);
        }
        batu_animation = (AnimationDrawable) buttonBatu.getBackground();
        batu_animation.start();
        batu_dynamicAnimation.start();
    }

    //ニックネーム---------------------------------------------------------------
    private InputMethodManager inputMethodManager;
    TextWatcher ntwNickname;
    View.OnTouchListener onTouchListener;
    View.OnKeyListener vokNickname;
    View.OnClickListener nvoMNP;
    View.OnClickListener nvoMNC;
    {
        nvoMNP = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.maNicknameEdit.getVisibility() == View.INVISIBLE) {
                    binding.maNicknameCheck.setVisibility(View.VISIBLE);
                    binding.maNickname.setVisibility(View.INVISIBLE);
                    binding.maNicknamePen.setVisibility(View.INVISIBLE);
                    binding.maNicknameEdit.setVisibility(View.VISIBLE);
                    inputMethodManager.toggleSoftInput(1, 0);
                    binding.maNicknameEdit.requestFocus();
                    binding.maNicknameEdit.setOnKeyListener(vokNickname);
                    binding.getRoot().setOnTouchListener(onTouchListener);
                    binding.maNicknameEdit.addTextChangedListener(ntwNickname);
                }
            }
        };

        nvoMNC = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.maNicknameEdit.getText().length() > 0){
                    String nickname = binding.maNicknameEdit.getText().toString();
                    binding.maNickname.setText(nickname);
                    binding.maNicknameEdit.setText(null);
                    binding.maNicknameEdit.setVisibility(View.INVISIBLE);
                    binding.maNicknameCheck.setVisibility(View.INVISIBLE);
                    binding.maNickname.setVisibility(View.VISIBLE);
                    binding.maNicknamePen.setVisibility(View.VISIBLE);

                    //
                    Log.i("mmmmm",account_id);
                    DatabaseUpdate dP = new DatabaseUpdate(GAS_URL, account_id, nickname,null);
                    dP.update();
                    //---------------------------------------------
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Log.i("mmmmm","run");
                            if(dP.getFrag() == 1){
                                //時間差がある！！
                                Log.i("mmmmm","niOk");
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.sp_ac_nickname), nickname);
                                editor.apply();
                                dP.setFrag(2);
                            }
                            else if(dP.getFrag() == 0){
                                dP.setFrag(2);
                            }
                            else {
                                mainHandler.postDelayed(this, 300);
                            }
                        }
                    };
                    mainHandler.post(r);
                    //--------------------------------------------------
                }
            }
        };

        ntwNickname = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("mmmmmm", "async");
                if (binding.maNicknameEdit.getText().length() > 0) {
                    Log.i("mmmmmm", "async");
                    binding.maNicknameEdit.setTextColor(Color.BLACK);
                    binding.maNicknameCheck.setImageResource(R.drawable.ma_nickname_check);
                } else {
                    binding.maNicknameEdit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    binding.maNicknameCheck.setImageResource(R.drawable.ma_nickname_check_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(binding.maNicknameEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    binding.maNicknameEdit.setText(null);
                    binding.maNicknameEdit.setVisibility(View.INVISIBLE);
                    binding.maNicknameCheck.setVisibility(View.INVISIBLE);
                    binding.maNickname.setVisibility(View.VISIBLE);
                    binding.maNicknamePen.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        };

        vokNickname = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if ((event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(binding.maNicknameEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        };
    }
    //自分の項目-----------------------------------------------------------------
    Map<Integer,Boolean> myList_Map_frag = new HashMap<Integer,Boolean>();
    int changeCount = 0;
    int removeCount;
    MapCreate mc;
    private List<String> titleList;
    private List<String> itemList;
    private List<String> numberList;
    public Map<String,Integer> item_map_frag = new HashMap<String,Integer>();
    public Map<String,String> item_map_name = new HashMap<String,String>();
    public RecyclerView recyclerView_Rear;
    private String ChangedItemNumber;
    void set_ma_myList () {
        binding.maHome.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_main_mylist, binding.maHome);

        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.myList_Recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false)
        );
        recyclerView.setAdapter(new RecyclerViewAdapter_ma_mylist(MainActivity.this, titleList,itemList,numberList,false));

        TextView change = findViewById(R.id.myList_change_text);

        SwitchCompat myListSwitch = findViewById(R.id.myList_switch);
        ImageView pen = findViewById(R.id.mylist_pen);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removeCount > 0) {
                    //map変更
                    for(int i = 0; i < 10; i++){
                        if(myList_Map_frag.get(i)){
                            item_map_frag.put(numberList.get(i),3);
                        }
                        else{
                            item_map_frag.put(numberList.get(i),2);
                        }
                    }

                    ConcatAdapter concatAdapter = new ConcatAdapter(
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[0], mc.outdoor_items, 0),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[1], mc.trip_items, 1),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[2], mc.reading_items, 2),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[3], mc.sns_items, 3),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[4], mc.anime_items, 4),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[5], mc.game_items, 5),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[6], mc.movie_items, 6),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[7], mc.music_items, 7),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[8], mc.gourmet_items, 8),
                            new RecyclerViewAdapter_ma_mylist_edit(MainActivity.this, mc.titles[9], mc.gambling_items, 9)
                    );
                    recyclerView.setAdapter(concatAdapter);

                    recyclerView_Rear = (RecyclerView) findViewById(R.id.myList_recyclerViewRear);
                    recyclerView_Rear.setHasFixedSize(true);
                    recyclerView_Rear.setLayoutManager(
                            new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false)
                    );
                    recyclerView_Rear.setAdapter(new RecyclerViewAdapter_ma_mylist_Rear(MainActivity.this, changeCount,removeCount));

                    change.setText("完了");
                    myList_changeText_update(false);

                    change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(removeCount <= changeCount){
                                //
                                List<String> changedNumber = mc.selectedItemNumber(item_map_frag);
                                ChangedItemNumber = changedNumber.get(0);
                                for(int i = 1; i < 10; i++){
                                    ChangedItemNumber = ChangedItemNumber + "-" + changedNumber.get(i);
                                }

                                binding.maLoad.setImageResource(R.drawable.loadforeground);
                                binding.maProgressBar.setVisibility(View.VISIBLE);
                                myListSwitch.setEnabled(false);
                                buttonBatu.setEnabled(false);

                                //
                                Log.i("mmmmm",account_id);
                                DatabaseUpdate dP = new DatabaseUpdate(GAS_URL, account_id, null,ChangedItemNumber);
                                dP.update();
                                //---------------------------------------------
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("mmmmm","run");
                                        if(dP.getFrag() == 1){
                                            //時間差がある！！
                                            Log.i("mmmmm","niOk");
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString(getString(R.string.sp_ac_info_number), ChangedItemNumber);
                                            editor.apply();

                                            binding.maLoad.setImageResource(0);
                                            binding.maProgressBar.setVisibility(View.INVISIBLE);
                                            myListSwitch.setEnabled(true);
                                            buttonBatu.setEnabled(true);
                                            dP.setFrag(2);

                                            //reset
                                            mc = new MapCreate(ChangedItemNumber);
                                            set_ma_myList();
                                        }
                                        else if(dP.getFrag() == 0){
                                            binding.maLoad.setImageResource(0);
                                            binding.maProgressBar.setVisibility(View.INVISIBLE);
                                            myListSwitch.setEnabled(true);
                                            buttonBatu.setEnabled(true);

                                            dP.setFrag(2);

                                            //reset
                                            set_ma_myList();
                                        }
                                        else {
                                            mainHandler.postDelayed(this, 300);
                                        }
                                    }
                                };
                                mainHandler.post(r);
                                //--------------------------------------------------
                            }
                        }
                    });
                }
            }
        });

        myListSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pen.setImageResource(R.drawable.ma_penb);
                    recyclerView.setAdapter(new RecyclerViewAdapter_ma_mylist(MainActivity.this, titleList,itemList,numberList,true));
                }
                else{
                    //reset
                    set_ma_myList();
                }
            }
        });


        changeCount = 0;
        titleList =mc.getSelectedTitle();
        itemList = mc.getSelectedItem();
        numberList = mc.getSelectedNumber();
        item_map_name = mc.getItemMapName();
        item_map_frag = mc.getItemMapFrag();

        pen.setImageResource(R.drawable.ma_pen_gray);
        myList_Map_frag.clear();
        myList_changeText_update(false);
        change.setText("変更する");
    }
    TextView myList_changeText;
    void myList_changeText_update(boolean frag){
        myList_changeText = findViewById(R.id.myList_change_text);
        if (frag){
            myList_changeText.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.ma_change_text_background,null));
            myList_changeText.setTextColor(getColor(R.color.white));

        }
        else{
            myList_changeText.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.stroke_teal_,null));
            myList_changeText.setTextColor(getColor(R.color.teal_200));
        }
    }
    //qrcode-----------------------------------------------------------------
    public native String getRandom();
    private String[] randString = {"0","1","2","3","4","5","6","7","8","9"
            ,"A","B","C","D","E","F","G","H","I","J","K"
            ,"L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    boolean qrcode_frag = false;
    private View.OnClickListener nvoQR = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //id取得
            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.sp_account), getApplication().MODE_PRIVATE);
            String account_id = sharedPref.getString(getString(R.string.sp_ac_id), null);

            if (qrcode_frag) {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                DatabaseSetRandom dSR = new DatabaseSetRandom(GAS_URL, account_id, "none");
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (dSR.getFrag() != 2) {
                            Log.i("mmmmmm", "ook");
                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                            imageView.setImageResource(R.drawable.aim_trans);
                            qrcode_frag = false;
                        } else {
                            //待ち
                            Log.i("mmmmmm", "ssk");
                            mainHandler.postDelayed(this, 100);
                        }
                    }
                };
                mainHandler.post(r);
                //---------------------------------------
            } else {
                String ulid = getRandom();

                String[] arrays = ulid.split("");

                ULID = "";

                Random rand = new Random();

                for (int i = 5; i < arrays.length; i++) {
                    if (i < 23) {
                        int num = rand.nextInt(36);
                        ULID = ULID + randString[i] + arrays[i];
                    } else {
                        ULID = ULID + arrays[i];
                    }
                }
                Log.i("mmmmm", account_id);
                Log.i("mmmmm", ULID);
                //判定が出るまでループ--------------------------------
                Handler mainHandler = new Handler(Looper.getMainLooper());
                DatabaseSetRandom dSR = new DatabaseSetRandom(GAS_URL, account_id, ULID);
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (dSR.getFrag() != 2) {
                            Log.i("mmmmmm", "ook");

                            // QRCodeの作成
                            Bitmap qrCodeBitmap = createQRCode(ULID);

                            // QRCodeの作成に成功した場合
                            if (qrCodeBitmap != null) {
                                // 結果をImageViewに表示
                                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                imageView.setImageBitmap(qrCodeBitmap);
                            }

                            // 結果をImageViewに表示
                            //binding.imageView.setImageBitmap(qrCodeBitmap);

                            qrcode_frag = true;
                        } else {
                            //待ち
                            Log.i("mmmmmm", "ssk");
                            mainHandler.postDelayed(this, 100);
                        }
                    }
                };
                mainHandler.post(r);
                //---------------------------------------
            }
        }
    };
    private Bitmap createQRCode(String contents){
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
    private int[] createDot(BitMatrix qrBitMatrix){
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

    //アカウント切り替え----------------------------------------------------------
    //googleSignInボタン処理
    private  View.OnClickListener nvoGs = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, GS_IN);
        }
    };

    //lineSignInボタン処理
    private  View.OnClickListener nvoLs = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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

                    dG = new DatabaseGetMyInfo(GAS_URL,null,gmail,null);
                    dG.SetMyInfo();
                    //判定が出るまでループ--------------------------------
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    ProgressBar progressBar = binding.maProgressBar;
                    progressBar.setVisibility(View.VISIBLE);
                    binding.maLoad.setImageResource(R.drawable.loadforeground);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            if(dG.getFrag() != 2){
                                navigateToSecondActivity(dG.getFrag());
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
        else if (requestCode == LS_IN) {
            LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
            switch (result.getResponseCode()) {
                case SUCCESS:
                    // Login successful
                    lineID = result.getLineProfile().getUserId();
                    //database
                    dG= new DatabaseGetMyInfo(GAS_URL,null,null,lineID);
                    //判定が出るまでループ--------------------------------
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    ProgressBar progressBar = binding.maProgressBar;
                    progressBar.setVisibility(View.VISIBLE);
                    binding.maLoad.setImageResource(R.drawable.loadforeground);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            if(dG.getFrag() != 2){
                                navigateToSecondActivity(dG.getFrag());
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
                    Toast.makeText(getApplicationContext(), "LINE Login Canceled", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    // Login canceled due to other error
                    Log.i("mmmmmmmm", "Login FAILED!");
                    Log.i("mmmmmmmmm", result.getErrorData().toString());
                    Toast.makeText(getApplicationContext(), "LINE Login Canceled", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        else{
            Log.i("mmmmmmmmm", "Unsupported Request");
            return;
        }
    }

    //ログイン後画面へ
    void navigateToSecondActivity(int frag){
        if(frag == 1) {
            //自動サインイン用にid保存
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.sp_ac_lineID), lineID);
            editor.putString(getString(R.string.sp_ac_gmail), gmail);
            editor.putString(getString(R.string.sp_ac_id),dG.getId());
            editor.putString(getString(R.string.sp_ac_nickname),dG.getNickname());
            editor.putString(getString(R.string.sp_ac_info_number),dG.getInfo());
            editor.apply();
            finish();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(frag == 3){
            finish();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            gsc.signOut()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            binding.maLoad.setImageResource(0);
            binding.maProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Undefined your account", Toast.LENGTH_SHORT).show();
        }
    }

    //破棄-----------------------------------------------------------------
    @Override
    protected void onStop(){
        super.onStop();
        animationDrawable.stop();
        //id取得

        Handler mainHandler = new Handler(Looper.getMainLooper());
        DatabaseSetRandom dSR = new DatabaseSetRandom(GAS_URL, account_id, "none");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (dSR.getFrag() != 2) {
                    Log.i("mmmmmm","ook");
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageResource(R.drawable.aim_trans);
                    qrcode_frag = false;
                } else {
                    //待ち
                    Log.i("mmmmmm","ssk");
                    mainHandler.postDelayed(this, 100);
                }
            }
        };
        mainHandler.post(r);
        //---------------------------------------
    }
    @Override
    protected void onRestart(){
        super.onRestart();

        animationDrawable.start();
    }
}
