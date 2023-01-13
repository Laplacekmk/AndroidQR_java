package com.example.androidqr_java;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter_ma_cc_match extends RecyclerView.Adapter<RecyclerViewAdapter_ma_cc_match.ViewHolder> {
    MainActivity context;
    List<String> Title;
    List<String> Item;
    int itemCount;

    RecyclerViewAdapter_ma_cc_match(MainActivity context,List<String> number){
        this.context = context;
        this.itemCount = number.size();
        String s = number.get(0);
        for(int i = 1; i < number.size(); i++){
            s = s + "-" + number.get(i);
        }
        MapCreate mc = new MapCreate(s);
        this.Title = mc.getSelectedTitle();
        this.Item = mc.getSelectedItem();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleText;
        TextView itemText;

        public ViewHolder(@NonNull View view) {
            super(view);
            titleText = (TextView) view.findViewById(R.id.cc_item_match_title);
            itemText = (TextView) view.findViewById(R.id.cc_item_match_item);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_ma_cc_match.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_cc_item_match, parent, false);
        return new RecyclerViewAdapter_ma_cc_match.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleText.setText(Title.get(position));
        holder.itemText.setText(Item.get(position));
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
