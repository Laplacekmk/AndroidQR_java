package com.example.androidqr_java;



import android.content.Context;
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

import com.google.common.io.Resources;

import java.util.List;

import io.opencensus.resource.Resource;

public class RecyclerViewAdapter_caa extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    CreateAccountActivity context;
    String title;
    List<String> data;
    String titleNumber;
    boolean expanded = false;
    final Integer cb_gray = 0;
    final Integer cb_color = 1;


    public  RecyclerViewAdapter_caa(CreateAccountActivity context,String title,List<String> name,Integer titleNumber){
        this.context = context;
        this.title = title;
        this.data = name;
        this.titleNumber = String.valueOf(titleNumber);
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder{
        TextView titleText;
        ImageView titleImage;

        TitleViewHolder(View view){
            super(view);
            titleText = (TextView) view.findViewById(R.id.text_item);
            titleImage = (ImageView) view.findViewById(R.id.img_item);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView itemText;
        ImageView itemImage;

        ItemViewHolder(View view){
            super(view);
            itemText = (TextView) view.findViewById(R.id.text_item_child);
            itemImage = (ImageView) view.findViewById(R.id.img_item_child);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
            return new RecyclerViewAdapter_caa.TitleViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_child,parent,false);
            return new RecyclerViewAdapter_caa.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            RecyclerViewAdapter_caa.TitleViewHolder titleViewHolder = (RecyclerViewAdapter_caa.TitleViewHolder) holder;
            titleViewHolder.titleText.setText(title);
            if (expanded) {
                titleViewHolder.titleImage
                        .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.caa_title_arrowbottom));
            } else {
                titleViewHolder.titleImage
                        .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.caa_title_arrowtop));
            }
            titleViewHolder.titleImage.
                    setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toggleExpand();
                        }
                    });
        } else {
            int Position = position - 1;
            RecyclerViewAdapter_caa.ItemViewHolder itemViewHolder = (RecyclerViewAdapter_caa.ItemViewHolder) holder;
            itemViewHolder.itemText.setText(data.get(Position));
            String key = titleNumber+","+Position;

            if (context.item_map_frag.get(key) == cb_color){
                itemViewHolder.itemImage.setImageResource(R.drawable.caa_button_colorback);
                Log.i("mmmmm","0");
            } else if(context.item_map_frag.get(key) == cb_gray){
                itemViewHolder.itemImage.setImageResource(R.drawable.caa_button_grayback);
                Log.i("mmmmm","1");
            }

            itemViewHolder.itemImage
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(context.caaFragment_frag) {
                                String key = titleNumber +","+ Position;
                                if (context.item_map_frag.get(key) == cb_gray) {
                                    if (context.i < 10) {
                                        itemViewHolder.itemImage.setImageResource(R.drawable.caa_button_colorback);
                                        context.item_map_frag.put(key, cb_color);
                                        ++context.i;
                                    }
                                } else if (context.item_map_frag.get(key) == cb_color) {
                                    itemViewHolder.itemImage.setImageResource(R.drawable.caa_button_grayback);
                                    context.item_map_frag.put(key, cb_gray);
                                    --context.i;
                                }
                                context.recyclerView_Rear.setAdapter(new RecyclerViewAdapter_caaRear(context, context.i));
                                Log.i("mmmmm", "key：" + key);
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        if(expanded){
            Log.i("mmmmmm","data.size:"+ (data.size() + 1));
            return data.size() + 1;
        }
        else {
            Log.i("mmmmmm","data 1");
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position){
        if(position == 0){
            return 0;
        }
        else {
            return 1;
        }
    }

    private void toggleExpand() {
        expanded = !expanded;
        notifyItemChanged(0);
        if (expanded) {
            notifyItemRangeInserted(1, data.size());
            Log.i("mmmmmm","insert");
        } else {
            notifyItemRangeRemoved(1, data.size());
            Log.i("mmmmmm","remove");
        }
    }
}