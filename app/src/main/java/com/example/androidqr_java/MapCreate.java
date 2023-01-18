package com.example.androidqr_java;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapCreate {

    MapCreate(String List){
        Map_setName();
        Map_setFrag();
        setList(List);
    }

    List<String> getSelectedItem(){
        return selected_item;
    }
    List<String> getSelectedTitle(){
        return selected_title;
    }
    List<String> getSelectedNumber(){
        return selected_number;
    }
    Map<String,Integer> getItemMapFrag(){
        return item_map_frag;
    }
    Map<String,String> getItemMapName(){
        return item_map_name;
    }


    //---------------------------------------
    public int i;
    private boolean caa_frag = false;
    public boolean caaFragment_frag = true;

    private enum numTitles {
        outdoor,
        trip,
        reading,
        sns,
        anime,
        game,
        movie,
        music,
        gourmet,
        gambling
    }
    public static final String[] titles = {
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
    public List<String> outdoor_items = new ArrayList<>(){
        {
            add("ランニング/ウォーキング");
            add("ジム");
            add("球技");
            add("釣り");
            add("キャンプ");
            add("ウィンタースポーツ");
        }
    };
    public List<String> trip_items = new ArrayList<>(){
        {
            add("国内旅行");
            add("海外旅行");
        }
    };
    public List<String> reading_items = new ArrayList<>(){
        {
            add("日本文学");
            add("海外文学");
            add("洋書");
            add("純文学");
            add("ミステリ");
            add("SF小説");
        }
    };
    public List<String> sns_items = new ArrayList<>(){
        {
            add("twitter");
            add("instagram");
            add("facebook");
            add("tiktok");
            add("youtube");
        }
    };
    public List<String> anime_items = new ArrayList<>(){
        {
            add("バトル");
            add("ギャグ");
            add("ラブコメ");
            add("日常");
            add("スポーツ");
            add("サスペンス/ホラー");
        }
    };
    public List<String> game_items = new ArrayList<>(){
        {
            add("シューティングゲーム");
            add("ホラーゲーム");
            add("スポーツゲーム");
            add("RPG");
        }
    };
    public List<String> movie_items = new ArrayList<>(){
        {
            add("アクション");
            add("SF");
            add("サスペンス/ホラー");
            add("戦争");
            add("恋愛");
            add("コメディ");
            add("ミュージカル");
        }
    };
    public List<String> music_items = new ArrayList<>(){
        {
            add("j-pop");
            add("k-pop");
            add("洋楽");
            add("アイドル");
            add("バンド");
        }
    };
    public List<String> gourmet_items = new ArrayList<>(){
        {
            add("和食");
            add("洋食");
            add("フレンチ");
            add("中華");
        }
    };
    public List<String> gambling_items = new ArrayList<>(){
        {
            add("競馬");
            add("ボートレース");
            add("パチンコ");
            add("宝くじ");
        }
    };

    public List<String> selected_itemList = new ArrayList<String>();
    public List<String> selected_itemNumber = new ArrayList<String>();

    public List<String> selected_title = new ArrayList<String>();
    public List<String> selected_item = new ArrayList<String>();
    public List<String> selected_number = new ArrayList<String>();

    private void setList(String List){
        String[] splitList = List.split("-");
        String[] s;
        int x;
        for(int i = 0; i < splitList.length; i++){
            selected_number.add(splitList[i]);
            s = splitList[i].split(",");
            x = Integer.parseInt(s[0]);
            selected_title.add(titles[x]);
            //if(titles[x] != null) Log.i("mmmmm",titles[x]);
            //if(item_map_name.get(splitList[i]) != null) Log.i("mmmmm",item_map_name.get(splitList[i]));
            selected_item.add(item_map_name.get(splitList[i]));
        }
    }

    List<String> selectedItemList(Map<String,Integer> item_map_frag){
        List<String> list = new ArrayList<String>();
        String title;
        //outdoor
        title = String.valueOf(numTitles.outdoor.ordinal())+",";
        for (int i = 0; i < outdoor_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //trip
        title = String.valueOf(numTitles.trip.ordinal())+",";
        for (int i = 0; i < trip_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //reading
        title = String.valueOf(numTitles.reading.ordinal()+",");
        for (int i = 0; i < reading_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //sns
        title = String.valueOf(numTitles.sns.ordinal())+",";
        for (int i = 0; i < sns_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //anime
        title = String.valueOf(numTitles.anime.ordinal())+",";
        for (int i = 0; i < anime_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //game
        title = String.valueOf(numTitles.game.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //movie
        title = String.valueOf(numTitles.movie.ordinal())+",";
        for (int i = 0; i < movie_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //music
        title = String.valueOf(numTitles.music.ordinal())+",";
        for (int i = 0; i < music_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //gourmet
        title = String.valueOf(numTitles.gourmet.ordinal())+",";
        for (int i = 0; i < gourmet_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        //gambling
        title = String.valueOf(numTitles.gambling.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(item_map_name.get(title + i));
            }
        }
        return list;
    }
    List<String> selectedItemNumber(Map<String,Integer> item_map_frag){
        List<String> list = new ArrayList<String>();
        String title;
        //outdoor
        title = String.valueOf(numTitles.outdoor.ordinal())+",";
        for (int i = 0; i < outdoor_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //trip
        title = String.valueOf(numTitles.trip.ordinal())+",";
        for (int i = 0; i < trip_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //reading
        title = String.valueOf(numTitles.reading.ordinal())+",";
        for (int i = 0; i < reading_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //sns
        title = String.valueOf(numTitles.sns.ordinal())+",";
        for (int i = 0; i < sns_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //anime
        title = String.valueOf(numTitles.anime.ordinal())+",";
        for (int i = 0; i < anime_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //game
        title = String.valueOf(numTitles.game.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //movie
        title = String.valueOf(numTitles.movie.ordinal())+",";
        for (int i = 0; i < movie_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //music
        title = String.valueOf(numTitles.music.ordinal())+",";
        for (int i = 0; i < music_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //gourmet
        title = String.valueOf(numTitles.gourmet.ordinal())+",";
        for (int i = 0; i < gourmet_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        //gambling
        title = String.valueOf(numTitles.gambling.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1 || item_map_frag.get(title + i) == 2) {
                list.add(title + i);
            }
        }
        return list;
    }

    List<String> selectedItemNumber(){
        List<String> list = new ArrayList<String>();
        String title;
        //outdoor
        title = String.valueOf(numTitles.outdoor.ordinal())+",";
        for (int i = 0; i < outdoor_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //trip
        title = String.valueOf(numTitles.trip.ordinal())+",";
        for (int i = 0; i < trip_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //reading
        title = String.valueOf(numTitles.reading.ordinal())+",";
        for (int i = 0; i < reading_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //sns
        title = String.valueOf(numTitles.sns.ordinal())+",";
        for (int i = 0; i < sns_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //anime
        title = String.valueOf(numTitles.anime.ordinal())+",";
        for (int i = 0; i < anime_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //game
        title = String.valueOf(numTitles.game.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //movie
        title = String.valueOf(numTitles.movie.ordinal())+",";
        for (int i = 0; i < movie_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //music
        title = String.valueOf(numTitles.music.ordinal())+",";
        for (int i = 0; i < music_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //gourmet
        title = String.valueOf(numTitles.gourmet.ordinal())+",";
        for (int i = 0; i < gourmet_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        //gambling
        title = String.valueOf(numTitles.gambling.ordinal())+",";
        for (int i = 0; i < game_items.size(); i++) {
            if(item_map_frag.get(title + i) == 1) {
                list.add(title + i);
            }
        }
        return list;
    }

    private Map<String,Integer> item_map_frag = new HashMap<String,Integer>();
    private Map<String,String> item_map_name = new HashMap<String,String>();
    private void Map_setFrag () {
        //outdoor
        for (int i = 0; i < outdoor_items.size(); i++) {
            item_map_frag.put(numTitles.outdoor.ordinal()+","+i,    0);
        }
        //trip
        for (int i = 0; i < trip_items.size(); i++) {
            item_map_frag.put(numTitles.trip.ordinal()+","+i,       0);
        }
        //reading
        for (int i = 0; i < reading_items.size(); i++) {
            item_map_frag.put(numTitles.reading.ordinal()+","+i,    0);
        }
        //sns
        for (int i = 0; i < sns_items.size(); i++) {
            item_map_frag.put(numTitles.sns.ordinal()+","+i,        0);
        }
        //anime
        for (int i = 0; i < anime_items.size(); i++) {
            item_map_frag.put(numTitles.anime.ordinal()+","+i,      0);
        }
        //game
        for (int i = 0; i < game_items.size(); i++) {
            item_map_frag.put(numTitles.game.ordinal()+","+i,       0);
        }
        //movie
        for (int i = 0; i < movie_items.size(); i++) {
            item_map_frag.put(numTitles.movie.ordinal()+","+i,      0);
        }
        //music
        for (int i = 0; i < music_items.size(); i++) {
            item_map_frag.put(numTitles.music.ordinal()+","+i,      0);
        }
        //gourmet
        for (int i = 0; i < gourmet_items.size(); i++) {
            item_map_frag.put(numTitles.gourmet.ordinal()+","+i,    0);
        }
        //gambling
        for (int i = 0; i < gambling_items.size(); i++) {
            item_map_frag.put(numTitles.gambling.ordinal()+","+i,   0);
        }
    }
    private void Map_setName () {
        //outdoor
        for (int i = 0; i < outdoor_items.size(); i++) {
            item_map_name.put(numTitles.outdoor.ordinal()+","+i,    outdoor_items.get(i));
        }
        //trip
        for (int i = 0; i < trip_items.size(); i++) {
            item_map_name.put(numTitles.trip.ordinal()+","+i,       trip_items.get(i));
        }
        //reading
        for (int i = 0; i < reading_items.size(); i++) {
            item_map_name.put(numTitles.reading.ordinal()+","+i,    reading_items.get(i));
        }
        //sns
        for (int i = 0; i < sns_items.size(); i++) {
            item_map_name.put(numTitles.sns.ordinal()+","+i,        sns_items.get(i));
        }
        //anime
        for (int i = 0; i < anime_items.size(); i++) {
            item_map_name.put(numTitles.anime.ordinal()+","+i,      anime_items.get(i));
        }
        //game
        for (int i = 0; i < game_items.size(); i++) {
            item_map_name.put(numTitles.game.ordinal()+","+i,       game_items.get(i));
        }
        //movie
        for (int i = 0; i < movie_items.size(); i++) {
            item_map_name.put(numTitles.movie.ordinal()+","+i,      movie_items.get(i));
        }
        //music
        for (int i = 0; i < music_items.size(); i++) {
            item_map_name.put(numTitles.music.ordinal()+","+i,      music_items.get(i));
        }
        //gourmet
        for (int i = 0; i < gourmet_items.size(); i++) {
            item_map_name.put(numTitles.gourmet.ordinal()+","+i,    gourmet_items.get(i));
        }
        //gambling
        for (int i = 0; i < gambling_items.size(); i++) {
            item_map_name.put(numTitles.gambling.ordinal()+","+i,   gambling_items.get(i));
        }
    }
}
