package com.example.androidqr_java;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Resources;

import com.example.androidqr_java.databinding.ListItemsBinding;

import org.checkerframework.common.initializedfields.qual.EnsuresInitializedFields;

import java.util.List;
import java.util.Map;

public class ListvewAdapter extends BaseAdapter {

    private ListItemsBinding binding;
    private Context conText;
    private LayoutInflater inflater;
    private int layoutID;
    private List nameList;
    private List<Drawable> drawableList;
    private Drawable drawables;
    private Map<String,Drawable> listMap;

    static class ViewHolder {
        TextView text;
        ImageView img;
    }

    ListvewAdapter(Context context, int itemLayoutId,
                   List names, List drawable) {
        conText = context;
        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;
        nameList = names;
        drawableList = drawable;
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

        holder.img.setImageDrawable(drawableList.get(position));

        holder.text.setText(nameList.get(position).toString());

        convertView.startAnimation(AnimationUtils.loadAnimation(conText,R.anim.caa_list_animation));
        return convertView;
    }

    @Override
    public int getCount() {return nameList.size(); }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}
