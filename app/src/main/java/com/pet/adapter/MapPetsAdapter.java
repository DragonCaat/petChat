package com.pet.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.pet.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dragon on 2018/7/7.
 * 展示聚合数据的适配器
 */

public class MapPetsAdapter extends BaseAdapter{
    
    private List<String> heads;
    private Context mContext;
    
    public MapPetsAdapter(List<String> heads,Context mContext){
        this.heads = heads;
        this.mContext = mContext;
    }
    
    @Override
    public int getCount() {
        return heads.size();
    }

    @Override
    public Object getItem(int i) {
        return heads.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;

        if (convertView!=null)
            view = convertView;
        else
            view = View.inflate(viewGroup.getContext(), R.layout.map_pets_item,null);

        CircleImageView head = view.findViewById(R.id.cv_head);
        Glide.with(mContext)
                .load(heads.get(i))
                .placeholder(R.mipmap.default_head)
                .dontAnimate()
                .into(head);

        return view;
    }
}
