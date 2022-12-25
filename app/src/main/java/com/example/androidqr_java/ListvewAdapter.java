package com.example.androidqr_java;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidqr_java.databinding.ActivityCreateAccountBinding;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.example.androidqr_java.databinding.ListItemsBinding;

import java.lang.reflect.Array;
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

class ListAdapters extends BaseExpandableListAdapter {
    //メンバ変数
    List<String> listname;     //親要素のリスト
    List<List<String>> listchild; //子要素のリスト
    Context context;
    ActivityCreateAccountBinding binding;

    //コンストラクタ
    ListAdapters (Context context, List<String> listMaker, List<List<String>> listCar) {
        this.context    = context;
        this.listname = listMaker;
        this.listchild = listCar;
    }

    @Override
    public int getGroupCount() {
        return listname.size();    //親要素の数を返す
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listchild.get(groupPosition).size();   //子要素の数を返す
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listname.get(groupPosition);    //親要素を取得
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listchild.get(groupPosition).get(childPosition);   //子要素を取得
    }

    @Override
    public long getGroupId(int groupPosition) {
        //親要素の固有IDを返す
        //このサンプルでは固有IDは無いので0
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        //子要素の固有IDを返す
        //このサンプルでは固有IDは無いので0
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        //固有IDを持つかどうかを返す
        //このサンプルでは持たないのでfalse
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //親要素のレイアウト生成
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_items, parent, false);
        }
        ImageView iv = convertView.findViewById(R.id.img_item);
        TextView tv = convertView.findViewById(R.id.text_item);
        iv.setImageResource(R.drawable.caa_button_animation1);
        tv.setText(listname.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //子要素のレイアウト生成
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_items_child, parent, false);
        }
        ImageView iv = convertView.findViewById(R.id.img_item_child);
        TextView tv = convertView.findViewById(R.id.text_item_child);
        iv.setImageResource(R.drawable.caa_button_grayback);
        tv.setText(listchild.get(groupPosition).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //子要素がタップ可能かどうかを返す
        //このサンプルでは可能にするのでtrue
        return true;
    }
}

class recycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    String title;
    List<String> data;
    boolean expanded = false;

    public  recycleAdapter(Context con,String tit,List<String> name){
        this.context = con;
        this.title = tit;
        this.data = name;
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
            Log.i("mmmmmm","create "+viewType);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
            return new TitleViewHolder(view);
        }
        else {
            Log.i("mmmmmm","create "+viewType);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_child,parent,false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("mmmmm","position" + position);
        if (position == 0) {
            Log.i("mmmmmm","bind 0");
            ((TitleViewHolder)holder).titleText.setText(title);
            ((TitleViewHolder)holder).titleImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.caa_button_animation1));
            ((TitleViewHolder)holder).titleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleExpand();
                }
            });
        }
        else{
            Log.i("mmmmmm","bind 1");
            ((ItemViewHolder) holder).itemText.setText(data.get(position-1));
            ((ItemViewHolder) holder).itemImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.caa_button_grayback));
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
        Log.i("mmmmmm","position" + position);
        if(position == 0){
            Log.i("mmmmmm","getVIewType 0");
            return 0;
        }
        else {
            Log.i("mmmmmm","getVIewType 1");
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
