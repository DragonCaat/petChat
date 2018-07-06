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
import com.pet.bean.CutePetEntity;
import com.pet.bean.DynamicEntity;
import com.pet.view.NineGridTestLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dragon on 2018/6/27.
 * 萌宠适配器
 */

public class CutePetAdapter extends RecyclerView.Adapter<CutePetAdapter.ViewHolder> {

    private Context mContext;

    private List<CutePetEntity> list;

    public CutePetAdapter(Context activity, List<CutePetEntity> list) {
        this.mContext = activity;
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cvPetHead;
        TextView tvPetName;
        TextView tvPetBirthday;
        TextView tvPetKind;
        TextView tvPetGender;
        TextView tvPetStatue;

        public ViewHolder(View itemView) {
            super(itemView);
            cvPetHead = itemView.findViewById(R.id.cv_pet_head);
            tvPetName = itemView.findViewById(R.id.tv_pet_name);
            tvPetBirthday = itemView.findViewById(R.id.tv_pet_birthday);
            tvPetKind = itemView.findViewById(R.id.tv_pet_kind);
            tvPetGender = itemView.findViewById(R.id.tv_pet_gender);
            tvPetStatue = itemView.findViewById(R.id.tv_pet_statue);
        }
    }

    @Override
    public CutePetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pet_item, parent, false);
        return new CutePetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CutePetAdapter.ViewHolder holder, int position) {

        CutePetEntity entity = list.get(position);
        if (entity.getPet_gender() == 1)
            holder.tvPetGender.setText("女");
        else
            holder.tvPetGender.setText("男");

        holder.tvPetKind.setText(entity.getPet_kind());
        holder.tvPetBirthday.setText(entity.getPet_brith());
        holder.tvPetName.setText(entity.getPet_name());
        holder.tvPetStatue.setText(entity.getPet_status());

        Glide.with(mContext).load(Const.PIC_URL+entity.getPet_icon()).into(holder.cvPetHead);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
