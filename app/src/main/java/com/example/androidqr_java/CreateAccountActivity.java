package com.example.androidqr_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.androidqr_java.databinding.ActivityCreateAccountBinding;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.opencensus.resource.Resource;

public class CreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    //---------------------------------------
    public int i;
    private boolean caa_frag = false;
    public boolean caaFragment_frag = true;

    public Map<String,Integer> item_map_frag = new HashMap<String,Integer>();
    public Map<String,String> item_map_name = new HashMap<String,String>();
    private enum numTitles {
        outdoor,
        trip,
        reading,
        sns,
        anime,
        game,
        movie,
        music,
        gourmet,
        gambling
    }
    private static final String[] titles = {
            "アウトドア",
            "旅行",
            "読書",
            "SNS",
            "アニメ/漫画",
            "ゲーム",
            "映画鑑賞",
            "音楽鑑賞",
            "グルメ",
            "ギャンブル"
    };
    private List<String> outdoor_items = new ArrayList<>(){
        {
            add("ランニング/ウォーキング");
            add("ジム");
            add("球技");
            add("釣り");
            add("キャンプ");
            add("ウィンタースポーツ");
        }
    };
    private List<String> trip_items = new ArrayList<>(){
        {
            add("国内旅行");
            add("海外旅行");
        }
    };
    private List<String> reading_items = new ArrayList<>(){
        {
            add("日本文学");
            add("海外文学");
            add("洋書");
            add("純文学");
            add("ミステリ");
            add("SF小説");
        }
    };
    private List<String> sns_items = new ArrayList<>(){
        {
            add("twitter");
            add("instagram");
            add("facebook");
            add("tiktok");
            add("youtube");
        }
    };
    private List<String> anime_items = new ArrayList<>(){
        {
            add("バトル");
            add("ギャグ");
            add("ラブコメ");
            add("日常");
            add("スポーツ");
            add("サスペンス/ホラー");
        }
    };
    private List<String> game_items = new ArrayList<>(){
        {
            add("シューティングゲーム");
            add("ホラーゲーム");
            add("スポーツゲーム");
            add("RPG");
        }
    };
    private List<String> movie_items = new ArrayList<>(){
        {
            add("アクション");
            add("SF");
            add("サスペンス/ホラー");
            add("戦争");
            add("恋愛");
            add("コメディ");
            add("ミュージカル");
        }
    };
    private List<String> music_items = new ArrayList<>(){
        {
            add("j-pop");
            add("k-pop");
            add("洋楽");
            add("アイドル");
            add("バンド");
        }
    };
    private List<String> gourmet_items = new ArrayList<>(){
        {
            add("和食");
            add("洋食");
            add("フレンチ");
            add("中華");
        }
    };
    private List<String> gambling_items = new ArrayList<>(){
        {
            add("競馬");
            add("ボートレース");
            add("パチンコ");
            add("宝くじ");
        }
    };

    private RecyclerView recyclerView;
    public RecyclerView recyclerView_Rear;

    public List<String> selected_itemList = new ArrayList<String>();
    public List<String> selected_itemNumber = new ArrayList<String>();
    //---------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //mapを0で初期化
        Map_setFrag();
        Map_setName();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ConcatAdapter concatAdapter = new ConcatAdapter(
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.outdoor.ordinal()],  outdoor_items,   numTitles.outdoor.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.trip.ordinal()],     trip_items,      numTitles.trip.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.reading.ordinal()],  reading_items,   numTitles.reading.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.sns.ordinal()],      sns_items,       numTitles.sns.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.anime.ordinal()],    anime_items,     numTitles.anime.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.game.ordinal()],     game_items,      numTitles.game.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.movie.ordinal()],    movie_items,     numTitles.movie.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.music.ordinal()],    music_items,     numTitles.music.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.gourmet.ordinal()],  gourmet_items,   numTitles.gourmet.ordinal()),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,  titles[numTitles.gambling.ordinal()], gambling_items,  numTitles.gambling.ordinal())
                );
        recyclerView.setAdapter(concatAdapter);

        recyclerView_Rear = (RecyclerView) findViewById(R.id.recyclerViewRear);
        recyclerView_Rear.setHasFixedSize(true);
        recyclerView_Rear.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false)
        );
        recyclerView_Rear.setAdapter(new RecyclerViewAdapter_caaRear(CreateAccountActivity.this,0));

        binding.caaNextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caaFragment_frag = false;
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.caa_main,new CreateAccountCheckFragment());
                fragmentTransaction.commit();
            }
        });
    }

    public void setText_rear(boolean frag){
        if(frag && caa_frag) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(binding.caaNextImage, "translationX", -120f);
            animation.setDuration(500);
            animation.start();
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(binding.caaNextText, "translationX", -120f);
            animation1.setDuration(500);
            animation1.start();
            selected_itemList = selectedItemList();
            selected_itemNumber = selectedItemNumber();
            Log.i("mmmmm",selected_itemList + "");
            Log.i("mmmmm",selected_itemNumber + "");
            caa_frag = false;
        }
        else if (!frag && !caa_frag){
            ObjectAnimator animation = ObjectAnimator.ofFloat(binding.caaNextImage, "translationX", 120f);
            animation.setDuration(500);
            animation.start();
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(binding.caaNextText, "translationX", 120f);
            animation1.setDuration(500);
            animation1.start();
            selected_itemList = null;
            selected_itemNumber = null;
            Log.i("mmmmm","gray_gray");
            caa_frag = true;
        }
    }

    List<String> selectedItemList(){
        List<String> list = new ArrayList<String>();
        String title;
        //outdoor
        title = String.valueOf(numTitles.outdoor.ordinal())+",";
        for (int i = 0; i < outdoor_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //trip
        title = String.valueOf(numTitles.trip.ordinal())+",";
        for (int i = 0; i < trip_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //reading
        title = String.valueOf(numTitles.reading.ordinal()+",");
        for (int i = 0; i < reading_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //sns
        title = String.valueOf(numTitles.sns.ordinal())+",";
        for (int i = 0; i < sns_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //anime
        title = String.valueOf(numTitles.anime.ordinal())+",";
        for (int i = 0; i < anime_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //game
        title = String.valueOf(numTitles.game.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //movie
        title = String.valueOf(numTitles.movie.ordinal())+",";
        for (int i = 0; i < movie_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //music
        title = String.valueOf(numTitles.music.ordinal())+",";
        for (int i = 0; i < music_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //gourmet
        title = String.valueOf(numTitles.gourmet.ordinal())+",";
        for (int i = 0; i < gourmet_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        //gambling
        title = String.valueOf(numTitles.gambling.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(item_map_name.get(title + i));
            }
        }
        return list;
    }
    List<String> selectedItemNumber(){
        List<String> list = new ArrayList<String>();
        String title;
        //outdoor
        title = String.valueOf(numTitles.outdoor.ordinal())+",";
        for (int i = 0; i < outdoor_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //trip
        title = String.valueOf(numTitles.trip.ordinal())+",";
        for (int i = 0; i < trip_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //reading
        title = String.valueOf(numTitles.reading.ordinal())+",";
        for (int i = 0; i < reading_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //sns
        title = String.valueOf(numTitles.sns.ordinal())+",";
        for (int i = 0; i < sns_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //anime
        title = String.valueOf(numTitles.anime.ordinal())+",";
        for (int i = 0; i < anime_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //game
        title = String.valueOf(numTitles.game.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //movie
        title = String.valueOf(numTitles.movie.ordinal())+",";
        for (int i = 0; i < movie_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //music
        title = String.valueOf(numTitles.music.ordinal())+",";
        for (int i = 0; i < music_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //gourmet
        title = String.valueOf(numTitles.gourmet.ordinal())+",";
        for (int i = 0; i < gourmet_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //gambling
        title = String.valueOf(numTitles.gambling.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        return list;
    }

    private void Map_setFrag () {
        //outdoor
        for (int i = 0; i < outdoor_items.size(); i++) {
            item_map_frag.put(numTitles.outdoor.ordinal()+","+i,    0);
        }
        //trip
        for (int i = 0; i < trip_items.size(); i++) {
            item_map_frag.put(numTitles.trip.ordinal()+","+i,       0);
        }
        //reading
        for (int i = 0; i < reading_items.size(); i++) {
            item_map_frag.put(numTitles.reading.ordinal()+","+i,    0);
        }
        //sns
        for (int i = 0; i < sns_items.size(); i++) {
            item_map_frag.put(numTitles.sns.ordinal()+","+i,        0);
        }
        //anime
        for (int i = 0; i < anime_items.size(); i++) {
            item_map_frag.put(numTitles.anime.ordinal()+","+i,      0);
        }
        //game
        for (int i = 0; i < game_items.size(); i++) {
            item_map_frag.put(numTitles.game.ordinal()+","+i,       0);
        }
        //movie
        for (int i = 0; i < movie_items.size(); i++) {
            item_map_frag.put(numTitles.movie.ordinal()+","+i,      0);
        }
        //music
        for (int i = 0; i < music_items.size(); i++) {
            item_map_frag.put(numTitles.music.ordinal()+","+i,      0);
        }
        //gourmet
        for (int i = 0; i < gourmet_items.size(); i++) {
            item_map_frag.put(numTitles.gourmet.ordinal()+","+i,    0);
        }
        //gambling
        for (int i = 0; i < gambling_items.size(); i++) {
            item_map_frag.put(numTitles.gambling.ordinal()+","+i,   0);
        }
    }
    private void Map_setName () {
        //outdoor
        for (int i = 0; i < outdoor_items.size(); i++) {
            item_map_name.put(numTitles.outdoor.ordinal()+","+i,    outdoor_items.get(i));
        }
        //trip
        for (int i = 0; i < trip_items.size(); i++) {
            item_map_name.put(numTitles.trip.ordinal()+","+i,       trip_items.get(i));
        }
        //reading
        for (int i = 0; i < reading_items.size(); i++) {
            item_map_name.put(numTitles.reading.ordinal()+","+i,    reading_items.get(i));
        }
        //sns
        for (int i = 0; i < sns_items.size(); i++) {
            item_map_name.put(numTitles.sns.ordinal()+","+i,        sns_items.get(i));
        }
        //anime
        for (int i = 0; i < anime_items.size(); i++) {
            item_map_name.put(numTitles.anime.ordinal()+","+i,      anime_items.get(i));
        }
        //game
        for (int i = 0; i < game_items.size(); i++) {
            item_map_name.put(numTitles.game.ordinal()+","+i,       game_items.get(i));
        }
        //movie
        for (int i = 0; i < movie_items.size(); i++) {
            item_map_name.put(numTitles.movie.ordinal()+","+i,      movie_items.get(i));
        }
        //music
        for (int i = 0; i < music_items.size(); i++) {
            item_map_name.put(numTitles.music.ordinal()+","+i,      music_items.get(i));
        }
        //gourmet
        for (int i = 0; i < gourmet_items.size(); i++) {
            item_map_name.put(numTitles.gourmet.ordinal()+","+i,    gourmet_items.get(i));
        }
        //gambling
        for (int i = 0; i < gambling_items.size(); i++) {
            item_map_name.put(numTitles.gambling.ordinal()+","+i,   gambling_items.get(i));
        }
    }
}