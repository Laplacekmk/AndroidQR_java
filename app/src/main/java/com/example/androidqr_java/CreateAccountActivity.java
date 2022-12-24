package com.example.androidqr_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.androidqr_java.databinding.ActivityCreateAccountBinding;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

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
            add(names[0]);
            add(names[1]);
            add(names[2]);
            add(names[0]);
            add(names[1]);
            add(names[2]);
        }
    };
    private List<Drawable> drawables_list;

    BaseAdapter adapter;
    //---------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                    /*
                    Animation anime = AnimationUtils.loadAnimation(CreateAccountActivity.this,R.anim.caa_button_animation_back);

                    binding.imageSport.startAnimation(anime);
                    binding.textSport.startAnimation(anime);*/
                    ObjectAnimator animation = ObjectAnimator.ofFloat(binding.imageSport, "translationY",(binding.listGame.getTop()-binding.listGame.getBottom()));
                    animation.setDuration(500);
                    animation.start();

                    i = 1;
                }
                else if(i == 1) {
                    playback(button);
                    //listView.setAdapter(adapter);
                    /*
                    Animation anime = AnimationUtils.loadAnimation(CreateAccountActivity.this,R.anim.caa_button_animation_go);

                    binding.imageSport.startAnimation(anime);
                    binding.textSport.startAnimation(anime);
                    ObjectAnimator animation = ObjectAnimator.ofFloat(binding.imageSport, "translationY",0);
                    animation.setDuration(500);
                    animation.start();*/
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