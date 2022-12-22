package com.example.androidqr_java;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Resources;

import com.example.androidqr_java.databinding.ListItemsBinding;

public class ListvewAdapter extends BaseAdapter {

    private ListItemsBinding binding;

    private LayoutInflater inflater;
    private int layoutID;
    private String[] nameList;
    private Drawable drawables;

    static class ViewHolder {
        TextView text;
        ImageView img;
    }

    ListvewAdapter(Context context, int itemLayoutId,
                   String[] names, Drawable drawable) {
        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;
        nameList = names;
        drawables = drawable;
    }

    // getViewメソッドをOverride
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // レイアウトを作成
        if (convertView == null) {
            convertView = inflater.inflate(layoutID,null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.img_item);
            holder.text = convertView.findViewById(R.id.text_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setImageDrawable(drawables);

        holder.text.setText(nameList[position]);

        return convertView;
    }

    @Override
    public int getCount() {return nameList.length; }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}
