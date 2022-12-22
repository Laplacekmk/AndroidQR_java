package com.example.androidqr_java;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidqr_java.databinding.ActivityCreateAccountBinding;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

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

    ListView listView = binding.listView;
    BaseAdapter adapter = new ListvewAdapter(this.getApplicationContext(),R.layout.list_items,names,getResources().getDrawable(R.drawable.login_button_back));
    //---------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listView.setAdapter(adapter);

        button = binding.image;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == 0) {
                    button.setImageDrawable(null);
                    button.setBackgroundResource(R.drawable.caa_button_animation_play);
                    // Get the background, which has been compiled to an AnimationDrawable object.
                    AnimationDrawable frameAnimation = (AnimationDrawable) button.getBackground();
                    // Start the animation (looped playback by default).
                    frameAnimation.start();
                    i = 1;
                }
                else if(i == 1) {
                    button.setImageDrawable(null);
                    button.setBackgroundResource(R.drawable.caa_button_animation_playback);
                    // Get the background, which has been compiled to an AnimationDrawable object.
                    AnimationDrawable frameAnimation = (AnimationDrawable) button.getBackground();
                    // Start the animation (looped playback by default).
                    frameAnimation.start();
                    i = 0;
                }
            }
        });
    }
}