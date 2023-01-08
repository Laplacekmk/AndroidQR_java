package com.example.androidqr_java;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter_ma_mylist_edit extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    MainActivity context;
    String title;
    List<String> data;
    String titleNumber;
    boolean expanded = false;
    final Integer cb_gray = 0;
    final Integer cb_color = 1;
    final Integer cb_nonChange = 2;
    final Integer cb_change = 3;

    public  RecyclerViewAdapter_ma_mylist_edit(MainActivity context,String title,List<String> name,Integer titleNumber){
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
            titleText = (TextView) view.findViewById(R.id.myList_edit_title_text);
            titleImage = (ImageView) view.findViewById(R.id.myList_edit_title_img);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView itemText;
        ImageView itemImage;

        ItemViewHolder(View view){
            super(view);
            itemText = (TextView) view.findViewById(R.id.myList_edit_item_text);
            itemImage = (ImageView) view.findViewById(R.id.myList_edit_item_img);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_mylist_edit_title, parent, false);
            return new RecyclerViewAdapter_ma_mylist_edit.TitleViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_mylist_edit_item,parent,false);
            return new RecyclerViewAdapter_ma_mylist_edit.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            RecyclerViewAdapter_ma_mylist_edit.TitleViewHolder titleViewHolder = (RecyclerViewAdapter_ma_mylist_edit.TitleViewHolder) holder;
            titleViewHolder.titleText.setText(title);
            if (expanded) {
                titleViewHolder.titleImage
                        .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ma_mylist_edit_bottom));
            } else {
                titleViewHolder.titleImage
                        .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ma_mylist_edit_top));
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
            RecyclerViewAdapter_ma_mylist_edit.ItemViewHolder itemViewHolder = (RecyclerViewAdapter_ma_mylist_edit.ItemViewHolder) holder;
            itemViewHolder.itemText.setText(data.get(Position));
            String key = titleNumber+","+Position;

            if (context.item_map_frag.get(key) == cb_color){
                //選択されたとき
                itemViewHolder.itemImage.setImageResource(R.drawable.ma_mylist_edit_itemcolor);
                Log.i("mmmmm","0");
            }
            else if(context.item_map_frag.get(key) == cb_gray){
                //何にもないやつ
                itemViewHolder.itemImage.setImageResource(R.drawable.ma_mylist_edit_itemwhite);
                Log.i("mmmmm","1");
            }
            else if(context.item_map_frag.get(key) == cb_change){
                //変更するやつ
                itemViewHolder.itemImage.setImageResource(R.drawable.ma_mylist_edit_itemred);

                Log.i("mmmmm","1");
            }
            else if(context.item_map_frag.get(key) == cb_nonChange){
                //変更しないやつ
                itemViewHolder.itemImage.setImageResource(R.drawable.ma_mylist_edit_itemgray);
                Log.i("mmmmm","1");
            }

            itemViewHolder.itemImage
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String key = titleNumber +","+ Position;
                            if (context.item_map_frag.get(key) == cb_gray) {
                                if (context.changeCount < context.removeCount) {
                                    itemViewHolder.itemImage.setImageResource(R.drawable.ma_mylist_edit_itemcolor);
                                    context.item_map_frag.put(key, cb_color);
                                    ++context.changeCount;
                                }
                            } else if (context.item_map_frag.get(key) == cb_color) {
                                itemViewHolder.itemImage.setImageResource(R.drawable.ma_mylist_edit_itemwhite);
                                context.item_map_frag.put(key, cb_gray);
                                --context.changeCount;
                            }
                            context.recyclerView_Rear.setAdapter(new RecyclerViewAdapter_ma_mylist_Rear(context, context.changeCount, context.removeCount));
                            Log.i("mmmmm", "key：" + key);
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