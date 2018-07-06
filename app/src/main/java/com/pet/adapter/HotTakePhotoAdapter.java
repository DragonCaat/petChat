package com.pet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.ShowUserActivity;
import com.pet.activity.TopicDetailActivity;
import com.pet.baseadapter.BaseAdapter;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.view.NineGridTestLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dragon on 2018/5/1.
 * 被装饰类要和装饰类继承自同一父类
 */

public class HotTakePhotoAdapter extends BaseAdapter<DynamicEntity> {
    private static Context mContext;


    public HotTakePhotoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_attention_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DynamicEntity dynamicEntity = getDataSet().get(position);
        ((MyViewHolder) holder).bind(dynamicEntity);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;
        CircleImageView cvHeadImage;
        TextView tvNickName;
        TextView tvContent;
        LinearLayout llAnswer;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.images);
            cvHeadImage = itemView.findViewById(R.id.circleImage_user);
            tvNickName = itemView.findViewById(R.id.tv_nickName);
            tvContent = itemView.findViewById(R.id.tv_content);
            llAnswer = itemView.findViewById(R.id.ll_answer);
        }

        public void bind(final DynamicEntity dynamicEntity) {
            layout.setIsShowAll(true); //当传入的图片数超过9张时，是否全部显示
            layout.setSpacing(5); //动态设置图片之间的间隔
            layout.setUrlList(dynamicEntity.getImg_urls()); //最后再设置图片url

            tvNickName.setText(dynamicEntity.getUser_name());
            tvContent.setText(dynamicEntity.getContent());
            Glide.with(mContext).load(Const.PIC_URL + dynamicEntity.getUser_icon()).into(cvHeadImage);

            //设置头像的点击事件
            cvHeadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转展示用户信息的页面
                    Intent intent = new Intent(mContext, ShowUserActivity.class);
                    intent.putExtra("id",dynamicEntity.getUser_id());
                    mContext.startActivity(intent);
                }
            });



            llAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TopicDetailActivity.class);
                    intent.putExtra("nick",dynamicEntity.getUser_name());
                    intent.putExtra("content",dynamicEntity.getContent());
                    intent.putExtra("head",dynamicEntity.getUser_icon());
                    intent.putExtra("id",dynamicEntity.getUser_id());
                    intent.putExtra("postId",dynamicEntity.getPost_id());
                    intent.putStringArrayListExtra("urls", (ArrayList<String>) dynamicEntity.getImg_urls());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
