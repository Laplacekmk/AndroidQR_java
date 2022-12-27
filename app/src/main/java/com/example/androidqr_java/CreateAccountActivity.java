package com.example.androidqr_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.androidqr_java.databinding.ActivityCreateAccountBinding;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

    public Map<String,Integer> item_map= new HashMap<String,Integer>();
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
            add("SF");
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

    private Animation next_go;
    private Animation next_back;
    //---------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //mapに値を入れる
        {
            //outdoor 6
            item_map.put(outdoor_items.get(0), 0);
            item_map.put(outdoor_items.get(1), 0);
            item_map.put(outdoor_items.get(2), 0);
            item_map.put(outdoor_items.get(3), 0);
            item_map.put(outdoor_items.get(4), 0);
            item_map.put(outdoor_items.get(5), 0);
            //trip 2
            item_map.put(trip_items.get(0), 0);
            item_map.put(trip_items.get(1), 0);
            //reading 6
            item_map.put(reading_items.get(0), 0);
            item_map.put(reading_items.get(1), 0);
            item_map.put(reading_items.get(2), 0);
            item_map.put(reading_items.get(3), 0);
            item_map.put(reading_items.get(4), 0);
            item_map.put(reading_items.get(5), 0);
            //sns 5
            item_map.put(sns_items.get(0), 0);
            item_map.put(sns_items.get(1), 0);
            item_map.put(sns_items.get(2), 0);
            item_map.put(sns_items.get(3), 0);
            item_map.put(sns_items.get(4), 0);
            //anime 7
            item_map.put(anime_items.get(0), 0);
            item_map.put(anime_items.get(1), 0);
            item_map.put(anime_items.get(2), 0);
            item_map.put(anime_items.get(3), 0);
            item_map.put(anime_items.get(4), 0);
            item_map.put(anime_items.get(5), 0);
            item_map.put(anime_items.get(6), 0);
            //game 4
            item_map.put(game_items.get(0), 0);
            item_map.put(game_items.get(1), 0);
            item_map.put(game_items.get(2), 0);
            item_map.put(game_items.get(3), 0);
            //movie 7
            item_map.put(movie_items.get(0), 0);
            item_map.put(movie_items.get(1), 0);
            item_map.put(movie_items.get(2), 0);
            item_map.put(movie_items.get(3), 0);
            item_map.put(movie_items.get(4), 0);
            item_map.put(movie_items.get(5), 0);
            item_map.put(movie_items.get(6), 0);
            //music 5
            item_map.put(music_items.get(0), 0);
            item_map.put(music_items.get(1), 0);
            item_map.put(music_items.get(2), 0);
            item_map.put(music_items.get(3), 0);
            item_map.put(music_items.get(4), 0);
            //gourmet 4
            item_map.put(gourmet_items.get(0), 0);
            item_map.put(gourmet_items.get(1), 0);
            item_map.put(gourmet_items.get(2), 0);
            item_map.put(gourmet_items.get(3), 0);
            //gambling 4
            item_map.put(gambling_items.get(0), 0);
            item_map.put(gambling_items.get(1), 0);
            item_map.put(gambling_items.get(2), 0);
            item_map.put(gambling_items.get(3), 0);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ConcatAdapter concatAdapter = new ConcatAdapter(
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.outdoor.ordinal()],outdoor_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.trip.ordinal()],trip_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.reading.ordinal()],reading_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.sns.ordinal()],sns_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.anime.ordinal()],anime_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.game.ordinal()],game_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.movie.ordinal()],movie_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.music.ordinal()],music_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.gourmet.ordinal()],gourmet_items),
                new RecyclerViewAdapter_caa(CreateAccountActivity.this,titles[numTitles.gambling.ordinal()],gambling_items)
                );
        recyclerView.setAdapter(concatAdapter);

        recyclerView_Rear = (RecyclerView) findViewById(R.id.recyclerViewRear);
        recyclerView_Rear.setHasFixedSize(true);
        recyclerView_Rear.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false)
        );
        recyclerView_Rear.setAdapter(new RecyclerViewAdapter_caaRear(CreateAccountActivity.this,0));

        next_go = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.caa_button_animation_go);
        next_back = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.caa_button_animation_back);
    }

    public void setText_rear(boolean frag){
        if(frag && caa_frag) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(binding.caaNextImage, "translationX", -120f);
            animation.setDuration(500);
            animation.start();
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(binding.caaNextText, "translationX", -120f);
            animation1.setDuration(500);
            animation1.start();
            Log.i("mmmmm","black_black");
            caa_frag = false;
        }
        else if (!frag && !caa_frag){
            ObjectAnimator animation = ObjectAnimator.ofFloat(binding.caaNextImage, "translationX", 120f);
            animation.setDuration(500);
            animation.start();
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(binding.caaNextText, "translationX", 120f);
            animation1.setDuration(500);
            animation1.start();
            Log.i("mmmmm","gray_gray");
            caa_frag = true;
        }
    }
}