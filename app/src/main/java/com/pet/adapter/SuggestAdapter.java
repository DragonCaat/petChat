package com.pet.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.pet.R;
import com.pet.view.PageRecyclerView;

import java.util.List;

/**
 * Created by dragon on 2018/7/5.
 * 意见反馈的适配器
 */

public class SuggestAdapter extends BaseAdapter {
    private List<String> list;
    private Context mContext;
    private int clickTemp = -1;
    public SuggestAdapter(List<String> list,Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        if (convertView != null)
            view = convertView;
        else
            view = View.inflate(viewGroup.getContext(), R.layout.layout_suggest_item, null);

        TextView text = view.findViewById(R.id.tv_suggest);
        text.setText(list.get(position));

        if (clickTemp == position){
            text.setTextColor(mContext.getResources().getColor(R.color.white));
            text.setBackgroundResource(R.drawable.shape_corner_normal);
        }
        else{
            text.setTextColor(mContext.getResources().getColor(R.color.black));
            text.setBackgroundResource(R.drawable.shape_corner_white);
        }

        return view;
    }

    public void setSelection(int position){
        clickTemp = position;
    }
}
