package com.example.androidqr_java;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter_caaCheck extends RecyclerView.Adapter<RecyclerViewAdapter_caaCheck.RecyclerViewHolder_caaCheck>{

    private CreateAccountActivity context;
    private final int itemCount = 10;
    private List<Integer> itemNumber = new ArrayList<Integer>();
    private static final String[] titles = {
            "アウトドア",
            "旅行",
            "読書",
            "SNS",
            "アニメ/漫画",
            "ゲーム",
            "映画鑑賞",
            "音楽鑑賞",
            "グルメ",
            "ギャンブル"
    };

    RecyclerViewAdapter_caaCheck(CreateAccountActivity context) {
        this.context = context;
        String[] number;
        int n;
        for(int i = 0; i < itemCount; i++){
            number = context.selected_itemNumber.get(i).split(",");
            n = Integer.valueOf(number[0]);
            itemNumber.add(n);
        }
    }

    static class RecyclerViewHolder_caaCheck extends RecyclerView.ViewHolder{
        ImageView checkImage;
        TextView checkText;
        TextView checkTitle;

        RecyclerViewHolder_caaCheck(View view){
            super(view);
            checkImage = (ImageView) view.findViewById(R.id.check_item_img);
            checkText = (TextView) view.findViewById(R.id.check_item_text);
            checkTitle = (TextView) view.findViewById(R.id.check_item_title);
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder_caaCheck onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_item,parent,false);
        return new RecyclerViewHolder_caaCheck(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder_caaCheck holder, int position) {
        holder.checkText.setText(context.selected_itemList.get(position));
        holder.checkTitle.setText(titles[itemNumber.get(position)]);
        if(position + 1 == itemCount) holder.checkImage.setImageResource(0);
        else holder.checkImage.setImageResource(R.drawable.caa_line);
        Log.i("mmmmm",context.selected_itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
