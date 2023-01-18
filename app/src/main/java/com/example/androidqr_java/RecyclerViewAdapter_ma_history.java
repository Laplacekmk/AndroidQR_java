package com.example.androidqr_java;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Callable;

public class RecyclerViewAdapter_ma_history extends RecyclerView.Adapter<RecyclerViewAdapter_ma_history.ViewHolder> {
    MainActivity context;
    List<String> othersNickname;
    List<String> othersNumber;
    String[] myNumberArray;
    int itemCount;
    boolean buttonFrag = false;

    RecyclerViewAdapter_ma_history(MainActivity context, List<String> othersNick,List<String> othersNumber,String myNumber){
        this.context = context;
        this.othersNickname = othersNick;
        this.othersNumber = othersNumber;
        this.myNumberArray = myNumber.split("-");
        this.itemCount = othersNick.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Image;
        TextView NickText;
        TextView CountText;

        public ViewHolder(@NonNull View view) {
            super(view);
            Image = (ImageView) view.findViewById(R.id.history_item_img);
            NickText = (TextView) view.findViewById(R.id.history_item_nick);
            CountText = (TextView) view.findViewById(R.id.history_item_count);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_ma_history.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_history_item, parent, false);
        return new RecyclerViewAdapter_ma_history.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_ma_history.ViewHolder holder, int position) {
        int P = (itemCount - 1) - position;

        String[] othersNumberArray = othersNumber.get(P).split("-");
        int count = 0;
        for(int i = 0; i < 10; i++){
            for(int x = 0; x < 10; x++) {
                if (myNumberArray[i].equals(othersNumberArray[x])) {
                    count++;
                    break;
                }
            }
        }

        holder.NickText.setText(othersNickname.get(P));
        holder.CountText.setText(count + "/10");
        if(position % 2 == 0) {
            holder.Image.setImageResource(R.drawable.ma_history_item_b);
        }
        else{
            holder.Image.setImageResource(R.drawable.ma_history_item_w);
        }
        holder.Image.setOnClickListener(v -> {
            //othersListへの遷移
            if(context.history_buttonFrag) {
                Log.i("mmmmm", "history");
                context.history_makeRecycler_supported_by_cc(othersNumber.get(P),othersNickname.get(P));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
