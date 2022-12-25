package com.example.androidqr_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.androidqr_java.databinding.ActivityCreateAccountBinding;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.opencensus.resource.Resource;

public class CreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    ImageView button;

    private int i;

    //---------------------------------------
    private static final String[] names = {
            "PlayStation",
            "Switch",
            "パソコン"
    };
    Drawable drawable;
    private List<String> name_list = new ArrayList<>(){
        {
            add("ゲーム");
            add("スポーツ");
            add("読書");
            add("旅行");
            add("旅行");
        }
    };
    private List<String> list_game = new ArrayList<>(){
        {
            add("aaaaaa");
            add("bbbbbb");
            add("cccccc");
        }
    };
    private List<String> list_sport = new ArrayList<>(){
        {
            add("dddddd");
            add("eeeeee");
            add("ffffff");
        }
    };
    private List<String> list_reading = new ArrayList<>(){
        {
            add("gggggg");
            add("hhhhhh");
            add("iiiiii");
        }
    };
    private List<String> list_trip = new ArrayList<>(){
        {
            add("jjjjjj");
            add("kkkkkk");
            add("llllll");
        }
    };
    private List<List<String>> list_all = new ArrayList<>(){
        {
            add(list_game);
            add(list_sport);
            add(list_reading);
            add(list_trip);
            add(list_trip);
        }
    };

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterR1;
    private RecyclerView.Adapter adapterR2;
    private RecyclerView.Adapter adapterR3;
    private RecyclerView.LayoutManager layoutManager;
    private List<Drawable> drawables_list;

    BaseAdapter adapter;
    //---------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapterR1 = new recycleAdapter(getApplication(),names[0],name_list);
        adapterR2 = new recycleAdapter(getApplication(),names[1],name_list);
        adapterR3 = new recycleAdapter(getApplication(),names[2],name_list);
        ConcatAdapter concatAdapter = new ConcatAdapter(adapterR1,adapterR2,adapterR3);
        recyclerView.setAdapter(adapterR1);

/*
        ExpandableListView exListView = findViewById(R.id.exListView);
        ListAdapters adapters = new ListAdapters(CreateAccountActivity.this,name_list,list_all);
        exListView.setAdapter(adapters);

        exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    ListAdapters adapters1 = (ListAdapters) parent.getExpandableListAdapter();
                    String name = (String) adapters1.getGroup(groupPosition);
                    String name_child = (String) adapters1.getChild(groupPosition, childPosition);
                    Toast.makeText(getApplicationContext(), name + ":" + name_child, Toast.LENGTH_LONG).show();

                return true;
            }
        });*/

        drawable = getResources().getDrawable(R.drawable.caa_button_grayback);
        drawables_list = new ArrayList<>(){
            {
                add(drawable);
                add(drawable);
                add(drawable);
                add(drawable);
                add(drawable);
                add(drawable);
            }
        };
        /*

        ListView listView = binding.listGame;
        ListView listView1 = binding.listSport;
        ListView listView2 = binding.listReading;
        ListView listView3 = binding.listTrip;
        adapter = new ListvewAdapter(this.getApplicationContext(),R.layout.list_items,name_list,drawables_list);
        listView.setAdapter(adapter);
        listView1.setAdapter(adapter);
        listView2.setAdapter(adapter);
        listView3.setAdapter(adapter);

        button = binding.imageGame;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == 0) {
                    play(button);
                    //listView.setAdapter(null);

                    Animation anime = AnimationUtils.loadAnimation(CreateAccountActivity.this,R.anim.caa_button_animation_back);

                    binding.imageSport.startAnimation(anime);
                    binding.textSport.startAnimation(anime);
                    ObjectAnimator animation = ObjectAnimator.ofFloat(binding.imageSport, "translationY",(binding.listGame.getTop()-binding.listGame.getBottom()));
                    animation.setDuration(500);
                    animation.start();

                    i = 1;
                }
                else if(i == 1) {
                    playback(button);
                    //listView.setAdapter(adapter);

                    Animation anime = AnimationUtils.loadAnimation(CreateAccountActivity.this,R.anim.caa_button_animation_go);

                    binding.imageSport.startAnimation(anime);
                    binding.textSport.startAnimation(anime);
                    ObjectAnimator animation = ObjectAnimator.ofFloat(binding.imageSport, "translationY",0);
                    animation.setDuration(500);
                    animation.start();
                    i = 0;
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView1 = (ListView) parent;
                drawables_list.set(position,getResources().getDrawable(R.drawable.caa_button_colorback));
                View targetListView = listView1.getChildAt(position);
                listView1.getAdapter().getView(position,targetListView,listView1);
            }
        });
        */
    }


    void play(ImageView button){
        button.setImageDrawable(null);
        button.setBackgroundResource(R.drawable.caa_button_animation_playback);
        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) button.getBackground();
        // Start the animation (looped playback by default).
        frameAnimation.start();
    }
    void playback(ImageView button){
        button.setImageDrawable(null);
        button.setBackgroundResource(R.drawable.caa_button_animation_play);
        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) button.getBackground();
        // Start the animation (looped playback by default).
        frameAnimation.start();
    }
}