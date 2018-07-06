package com.pet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.bean.AnswerEntity;
import com.pet.bean.Const;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dragon on 2018/6/25.
 * 评论的适配器
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private Context mContext;

    private List<AnswerEntity> list;

    public AnswerAdapter(Context activity,List<AnswerEntity> list) {
        this.mContext = activity;
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cvHead;
        TextView tvNick;
        TextView tvContent;
        TextView tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.cv_answer_head);
            tvNick = itemView.findViewById(R.id.tv_answer_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_answer_time);
        }
    }

    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_answer_item, parent, false);
        return new AnswerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerAdapter.ViewHolder holder, int position) {
        AnswerEntity answerEntity = list.get(position);

        holder.tvTime.setText(answerEntity.getCreate_time());
        holder.tvNick.setText(answerEntity.getUser_name());
        holder.tvContent.setText(answerEntity.getCmmt_content());
        Glide.with(mContext)
                .load(Const.PIC_URL+answerEntity.getUser_icon())
                .placeholder(R.mipmap.default_head)
                .into(holder.cvHead);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
