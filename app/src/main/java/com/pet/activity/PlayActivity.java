package com.pet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.view.PageIndicatorView;
import com.pet.view.PageRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayActivity extends AppCompatActivity {

    PageRecyclerView mRecyclerView;
    List<String> list_tequan1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        for (int i = 0; i < 6; i++) {
            list_tequan1.add("hello" + i);
        }


        initData();
    }

    private void initData() {
        mRecyclerView =  findViewById(R.id.cusom_swipe_view);
        // 设置指示器
        mRecyclerView.setIndicator((PageIndicatorView) findViewById(R.id.indicator));
        // 设置行数和列数
        mRecyclerView.setPageSize(1, 1);

        // 设置页间距
        mRecyclerView.setPageMargin(0);
        // 设置数据
        mRecyclerView.setAdapter(mRecyclerView.new PageAdapter(list_tequan1,PlayActivity.this));
    }



}
