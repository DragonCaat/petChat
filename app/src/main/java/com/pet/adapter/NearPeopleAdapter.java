package com.pet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.ShowUserActivity;
import com.pet.bean.Const;
import com.pet.bean.NearByEntity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dragon on 2018/7/2.
 * 附近的人适配器
 */

public class NearPeopleAdapter extends RecyclerView.Adapter<NearPeopleAdapter.ViewHolder> {


    private List<NearByEntity> dataList;
    private Context context;


    public NearPeopleAdapter(Context context, List<NearByEntity> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CircleImageView circleImageView;
        RelativeLayout relativeLayout;
        TextView tvDes;
        TextView tvDistance;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv);
            circleImageView = itemView.findViewById(R.id.cv_head);
            relativeLayout = itemView.findViewById(R.id.rl_near);
            tvDes = itemView.findViewById(R.id.tv_des);
            tvDistance = itemView.findViewById(R.id.tv_distance);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.layout_near_people_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.name.setText(dataList.get(position).getUser_info().getUser_name().trim());
        holder.tvDes.setText(dataList.get(position).getUser_info().getUser_desc());
        holder.tvDistance.setText("距离你约"+dataList.get(position).getDistances()+" 米");
        Glide.with(context)
                .load(Const.PIC_URL + dataList.get(position).getUser_info().getUser_icon())
                .placeholder(R.mipmap.default_head)
                .dontAnimate()
                .into(holder.circleImageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowUserActivity.class);
                intent.putExtra("id",dataList.get(position).getUser_info().getUser_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
