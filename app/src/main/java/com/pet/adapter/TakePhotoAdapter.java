package com.pet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.TakePhotoDetailActivity;
import com.pet.bean.NineGridTestModel;
import com.pet.bean.TakePhotoEntity;
import com.pet.view.NineGridTestLayout;

import java.util.List;

/**
 * Created by dragon on 2018/6/12.
 * 随手拍的适配器
 */

public class TakePhotoAdapter extends RecyclerView.Adapter<TakePhotoAdapter.ViewHolder> {

    private Context mContext;

    private List<TakePhotoEntity> list;

    public TakePhotoAdapter(Context activity,List<TakePhotoEntity> list) {
        this.mContext = activity;
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvTitle;
        TextView tvContent;
        TextView tvJoin;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvJoin = itemView.findViewById(R.id.tv_join);
        }
    }

    @Override
    public TakePhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_take_photo_item, parent, false);
        return new TakePhotoAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(TakePhotoAdapter.ViewHolder holder, int position) {

        final TakePhotoEntity takePhotoEntity = list.get(position);
        holder.tvTitle.setText(takePhotoEntity.getCircle_title());
        holder.tvContent.setText(takePhotoEntity.getCircle_subtitle());
        holder.tvJoin.setText(takePhotoEntity.getCount()+" 人参与");
        Glide.with(mContext)
                .load(takePhotoEntity.getCircle_img())
                .placeholder(R.mipmap.take_photo_loading)
                .into(holder.ivImage);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TakePhotoDetailActivity.class);
                intent.putExtra("title",takePhotoEntity.getCircle_title());
                intent.putExtra("id",takePhotoEntity.getCircle_id());
                intent.putExtra("big_bg",takePhotoEntity.getCircle_big_img());
                intent.putExtra("des",takePhotoEntity.getCircle_desc());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
