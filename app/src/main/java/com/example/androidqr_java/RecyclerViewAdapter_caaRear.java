package com.example.androidqr_java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter_caaRear extends RecyclerView.Adapter<RecyclerViewAdapter_caaRear.RecyclerViewHolder_caaRear> {

    private CreateAccountActivity context;
    private int rearCount;
    private final int itemCount = 10;

    RecyclerViewAdapter_caaRear(CreateAccountActivity context,int rearCount) {
        this.context = context;
        this.rearCount = rearCount;
    }

    static class RecyclerViewHolder_caaRear extends RecyclerView.ViewHolder{
        ImageView rearImage;

        RecyclerViewHolder_caaRear(View view){
            super(view);
            rearImage = (ImageView) view.findViewById(R.id.rear_item);
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder_caaRear onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rear_item,parent,false);
        return new RecyclerViewHolder_caaRear(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder_caaRear holder, int position) {
        if (rearCount > position){
            holder.rearImage.setImageResource(R.drawable.caa_count_black);
        }
        else {
            holder.rearImage.setImageResource(R.drawable.caa_count_gray);
        }

        if(rearCount == itemCount && position + 1 == itemCount) context.setText_rear(true);
        else context.setText_rear(false);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}

