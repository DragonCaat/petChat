package com.pet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pet.R;

/**
 * Created by dragon on 2018/7/4.
 * 宠物种类适配器
 */

public class PetKindAdapter extends BaseAdapter{

    String[] pets;
    Activity context;

    public PetKindAdapter(String[] pets,Activity context){
        this.pets = pets;
        this.context = context;
    }
    @Override
    public int getCount() {
        return pets.length;
    }

    @Override
    public Object getItem(int i) {
        return pets[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, final View convertViw, ViewGroup viewGroup) {
        View view;
        if (convertViw==null)
            view = View.inflate(viewGroup.getContext(), R.layout.pet_kind_item,null);
        else
            view = convertViw;

        TextView textView = view.findViewById(R.id.tv_pet);
        textView.setText(pets[i]);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTemp = new Intent();
                intentTemp.putExtra("pet",pets[i]);
                context.setResult(1,intentTemp);
                context.finish();
            }
        });

        return view;
    }
}
