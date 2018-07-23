package com.pet.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.bean.MapPetInfo;

import java.util.List;

/**
 * Created by dragon on 2018/7/7.
 * 地图页面显示约宠人图片grid view的适配
 */

public class PetsIconAdapter extends BaseAdapter{
    private List<MapPetInfo.PetInfoBean.UserIconsBean> user_icons;
    private Context context;
    public PetsIconAdapter(List<MapPetInfo.PetInfoBean.UserIconsBean> user_icons,Context context){
        this.context = context;
        this.user_icons = user_icons;
    }
    @Override
    public int getCount() {
        return user_icons.size();
    }

    @Override
    public Object getItem(int i) {
        return user_icons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MapPetInfo.PetInfoBean.UserIconsBean iconsBean = user_icons.get(i);

        View view;
        if (convertView!=null)
            view = convertView;
        else
            view = View.inflate(context, R.layout.layout_pets_icon_item,null);

        ImageView viewById = view.findViewById(R.id.iv_pets);
        Glide.with(context)
                .load(com.pet.bean.Const.PIC_URL + iconsBean.getUser_icon())
                .dontAnimate()
                .into(viewById);

        return null;
    }
}
