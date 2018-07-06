package com.pet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.NineGridTestModel;
import com.pet.view.NineGridTestLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dragon on 2018/6/12.
 * 动态的适配器
 */

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> {

    private Context mContext;

    private List<DynamicEntity> list;

    public DynamicAdapter(Context activity, List<DynamicEntity> list) {
        this.mContext = activity;
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        NineGridTestLayout layout;
        CircleImageView cvHeadImage;
        TextView tvNickName;
        TextView tvContent;
        public ViewHolder(View itemView) {
            super(itemView);
            layout =  itemView.findViewById(R.id.images);
            cvHeadImage = itemView.findViewById(R.id.circleImage_user);
            tvNickName = itemView.findViewById(R.id.tv_nickName);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

    @Override
    public DynamicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_attention_item, parent, false);
        return new DynamicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DynamicAdapter.ViewHolder holder, int position) {

        DynamicEntity dynamicEntity = list.get(position);

        holder.layout.setIsShowAll(true); //当传入的图片数超过9张时，是否全部显示
        holder.layout.setSpacing(5); //动态设置图片之间的间隔
        holder.layout.setUrlList(dynamicEntity.getImg_urls()); //最后再设置图片url

        holder.tvNickName.setText(dynamicEntity.getUser_name());
        holder.tvContent.setText(dynamicEntity.getContent());

        Glide.with(mContext).load(Const.PIC_URL+dynamicEntity.getUser_icon()).into(holder.cvHeadImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void appendData(List<DynamicEntity> dataSet) {
        if (dataSet != null && !dataSet.isEmpty()) {
            this.list.addAll(dataSet);
            notifyDataSetChanged();
        }
    }
}
