package com.example.androidqr_java;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter_ma_mylist extends RecyclerView.Adapter<RecyclerViewAdapter_ma_mylist.ViewHolder> {
    MainActivity context;
    List<String> Title;
    List<String> Item;
    List<String> Number;
    int itemCount = 10;
    int removeCount = 0;
    boolean frag;

    RecyclerViewAdapter_ma_mylist(MainActivity context,List<String> title,List<String> item,List<String> number,boolean frag){
        this.context = context;
        this.Title = title;
        this.Item = item;
        this.Number = number;
        this.frag = frag;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        TextView titleText;
        TextView itemText;
        ImageView editImage;

        public ViewHolder(@NonNull View view) {
            super(view);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.activity_main_mylist_items);
            titleText = (TextView) view.findViewById(R.id.myList_title);
            itemText = (TextView) view.findViewById(R.id.myList_item);
            editImage = (ImageView) view.findViewById(R.id.myList_minus);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_ma_mylist.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_mylist_items, parent, false);
        return new RecyclerViewAdapter_ma_mylist.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("mmmmm",Title.get(position));
        Log.i("mmmmm",Item.get(position));
        holder.titleText.setText(Title.get(position));
        holder.itemText.setText(Item.get(position));
        if(holder.editImage.getTag() == null){
            holder.editImage.setTag(false);
            context.myList_Map_frag.put(position,false);
        }
        if(frag){
            holder.editImage.setImageResource(R.drawable.ma_mylist_itemimage_minus_gray);
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //notifyItemRemoved(holder.getLayoutPosition());
                    //itemCount -= 1;
                    int Position = holder.getLayoutPosition();
                    if((boolean)holder.editImage.getTag()) {
                        holder.editImage.setImageResource(R.drawable.ma_mylist_itemimage_minus_gray);
                        holder.editImage.setTag(false);
                        context.myList_Map_frag.put(Position,false);
                        --removeCount;
                    }
                    else{
                        holder.editImage.setImageResource(R.drawable.ma_mylist_itemimage_minus);
                        holder.editImage.setTag(true);
                        context.myList_Map_frag.put(Position,true);
                        ++removeCount;
                    }

                    if(removeCount > 0){
                        context.myList_changeText_update(true);
                    }
                    else{
                        context.myList_changeText_update(false);
                    }

                    context.removeCount = removeCount;
                    Log.i("mmmmm",String.valueOf(context.myList_Map_frag));
                    Log.i("mmmmm",String.valueOf(context.removeCount));
                }
            });
        }
        else{
            holder.editImage.setImageResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
