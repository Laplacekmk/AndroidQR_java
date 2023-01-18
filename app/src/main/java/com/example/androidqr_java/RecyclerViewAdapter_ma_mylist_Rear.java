package com.example.androidqr_java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter_ma_mylist_Rear extends RecyclerView.Adapter<RecyclerViewAdapter_ma_mylist_Rear.RecyclerViewHolder_ma_mylist_Rear> {

    private MainActivity context;
    private int rearCount;
    private int removeCount;

    RecyclerViewAdapter_ma_mylist_Rear(MainActivity context,int rearCount,int removeCountCount) {
        this.context = context;
        this.rearCount = rearCount;
        this.removeCount = removeCountCount;
    }

    static class RecyclerViewHolder_ma_mylist_Rear extends RecyclerView.ViewHolder{
        ImageView rearImage;

        RecyclerViewHolder_ma_mylist_Rear(View view){
            super(view);
            rearImage = (ImageView) view.findViewById(R.id.myList_edit_count_img);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_ma_mylist_Rear.RecyclerViewHolder_ma_mylist_Rear onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_mylist_edit_count,parent,false);
        return new RecyclerViewAdapter_ma_mylist_Rear.RecyclerViewHolder_ma_mylist_Rear(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder_ma_mylist_Rear holder, int position) {
        if (rearCount > position){
            holder.rearImage.setImageResource(R.drawable.ma_mylist_edit_count_color);
        }
        else {
            holder.rearImage.setImageResource(R.drawable.ma_mylist_edit_count_gray);
        }

        if(removeCount <= context.changeCount){
            context.myList_changeText_update(true);
        }
        else{
            context.myList_changeText_update(false);
        }
    }

    @Override
    public int getItemCount() {
        return removeCount;
    }
}

