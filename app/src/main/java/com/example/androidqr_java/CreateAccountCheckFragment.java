package com.example.androidqr_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreateAccountCheckFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 所属している親アクティビティを取得
        CreateAccountActivity activity = (CreateAccountActivity) getActivity();

        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_check_create_account, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCheck_caa);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        );
        recyclerView.setAdapter(new RecyclerViewAdapter_caaCheck(activity));

        // ボタン要素を取得
        ImageView completeImg = view.findViewById(R.id.complete_image);
        TextView completeText = view.findViewById(R.id.complete_text);
        ImageView backImg = view.findViewById(R.id.caa_back_image);

        // ボタンをクリックした時の処理
        completeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });
        completeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.caaFragment_frag = true;
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack();
            }
        });


        return view;
    }
}
