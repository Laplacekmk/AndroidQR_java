package com.example.androidqr_java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter_ma_cc_non extends RecyclerView.Adapter<RecyclerViewAdapter_ma_cc_non.ViewHolder> {
    MainActivity context;
    List<String> Title;
    List<String> Item;
    int itemCount;

    RecyclerViewAdapter_ma_cc_non(MainActivity context,List<String> number){
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
            titleText = (TextView) view.findViewById(R.id.cc_item_non_title);
            itemText = (TextView) view.findViewById(R.id.cc_item_non_item);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_ma_cc_non.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_cc_item_non, parent, false);
        return new RecyclerViewAdapter_ma_cc_non.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_ma_cc_non.ViewHolder holder, int position) {
        holder.titleText.setText(Title.get(position));
        holder.itemText.setText(Item.get(position));
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
