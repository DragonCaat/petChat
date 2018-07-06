package com.pet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pet.R;

import java.util.List;

/**
 * Created by dragon on 2018/6/23.
 * 展示图片
 */

public class GridViewShowPicAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;

    public GridViewShowPicAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size() + 1;//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.grid_item, null);
        mImageView = convertView.findViewById(R.id.item);
        if (position < list.size()) {
            Glide.with(context).load(list.get(position))
                    .centerCrop()
                    .into(mImageView);
        } else {
            mImageView.setImageResource(R.mipmap.add);//最后一个显示加号图片
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return convertView;
    }
}
