package com.pet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.DynamicDetailActivity;
import com.pet.bean.AttentionEntity;
import com.pet.bean.Const;

import java.util.List;

/**
 * Created by dragon on 2018/6/12.
 * 动态的适配器
 */

public class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.ViewHolder> {
    private List<AttentionEntity> list; // 数据源
    private Context context;    // 上下文Context

    public AttentionAdapter(List<AttentionEntity> list, Context context) {
        // 初始化变量
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView tvImageNum;
        TextView tvTime;
        ImageView imageView;

        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
            tvImageNum = itemView.findViewById(R.id.tv_pic_number);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageView = itemView.findViewById(R.id.iv_image);

            linearLayout = itemView.findViewById(R.id.ll_attention_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dynamic_item, null);
        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 如果是正常的item，直接设置TextView的值
        final AttentionEntity entity = list.get(position);
        holder.textView.setText(entity.getContent());
        holder.tvImageNum.setText("共 " + entity.getImgs().size() + "张");
        holder.tvTime.setText(entity.getSend_time());
        if (entity.getImgs().size() > 0){
            Glide.with(context)
                    .load(Const.PIC_URL + entity.getImgs().get(0))
                    .dontAnimate()
                    .placeholder(R.mipmap.take_photo_loading)
                    .into(holder.imageView);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DynamicDetailActivity.class);
                intent.putExtra("DynamicId",entity.getDynamic_id());
                intent.putExtra("flag",0);//是否是自己的动态判断1：不是自己 0：自己的动态
                context.startActivity(intent);
            }
        });
    }

}
