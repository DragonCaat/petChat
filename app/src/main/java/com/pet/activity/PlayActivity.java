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


       // initData();
    }





}
