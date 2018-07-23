package com.pet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.bean.AnswerEntity;
import com.pet.bean.Const;
import com.pet.bean.ResultEntity;
import com.pet.ninelayout.NineGridTestLayout;
import com.pet.ninelayout.OnItemPictureClickListener;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/6/25.
 * 评论的适配器
 */

public class DynamicAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int ITEM_TYPE_MAIN = 0;
    private int ITEM_TYPE_HEAD = 1;

    private OnItemPictureClickListener listener1;
    private Context mContext;
    private AnswerEntity answerEntity;
    private List<AnswerEntity.CmmtBean> cmmtBeans;

    private String acces_token = "";
    private String phone = "";
    private int user_id = 0;

    private int postId;

    public DynamicAnswerAdapter(Context activity, AnswerEntity answerEntity, List<AnswerEntity.CmmtBean> cmmtBeans,OnItemPictureClickListener listener1) {
        this.mContext = activity;
        this.answerEntity = answerEntity;
        this.cmmtBeans = cmmtBeans;
        this.listener1 = listener1;
    }

    public DynamicAnswerAdapter(Context activity, List<AnswerEntity.CmmtBean> cmmtBeans) {
        this.mContext = activity;
        this.cmmtBeans = cmmtBeans;
    }

    //添加头布局
    static class HeadViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mCvHead;
        TextView mTvNickName;
        TextView mTvContent;
        NineGridTestLayout layout;
        TextView tvTime;
        TextView tvPoint;
        TextView tvCount;
        ImageView ivPoint;
        LinearLayout llPoint;

        public HeadViewHolder(View v) {
            super(v);
            mCvHead = v.findViewById(R.id.circleImage_user);
            mTvNickName = v.findViewById(R.id.tv_nickName);
            mTvContent = v.findViewById(R.id.tv_content);
            layout = v.findViewById(R.id.images);
            tvTime = v.findViewById(R.id.tv_time);
            tvPoint = v.findViewById(R.id.tv_point);
            tvCount = v.findViewById(R.id.tv_comment_count);
            llPoint = v.findViewById(R.id.ll_point);
            ivPoint = v.findViewById(R.id.iv_point);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cvHead;
        TextView tvNick;
        TextView tvContent;
        TextView tvTime;
        TextView tvPoint;
        LinearLayout llPoint;
        ImageView ivPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.cv_answer_head);
            tvNick = itemView.findViewById(R.id.tv_answer_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_answer_time);
            tvPoint = itemView.findViewById(R.id.tv_point);
            llPoint = itemView.findViewById(R.id.ll_point);
            ivPoint = itemView.findViewById(R.id.iv_point);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_MAIN) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_answer_item, parent, false);
            return new DynamicAnswerAdapter.ViewHolder(view);

        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_answer_head, parent, false);
            return new DynamicAnswerAdapter.HeadViewHolder(view);
        }


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).mTvNickName.setText(answerEntity.getUser_name());
            ((HeadViewHolder) holder).mTvContent.setText(answerEntity.getContent());
            ((HeadViewHolder) holder).tvTime.setText(answerEntity.getSend_time());
            ((HeadViewHolder) holder).tvPoint.setText("" + answerEntity.getUpvoteCount());
            Glide.with(mContext)
                    .load(Const.PIC_URL + answerEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((HeadViewHolder) holder).mCvHead);

            ((HeadViewHolder) holder).layout.setListener(listener1);
            ((HeadViewHolder) holder).layout.setIsShowAll(true); //当传入的图片数超过9张时，是否全部显示
            ((HeadViewHolder) holder).layout.setSpacing(5); //动态设置图片之间的间隔
            ((HeadViewHolder) holder).layout.setUrlList(answerEntity.getImg_urls()); //最后再设置图片url
            ((HeadViewHolder) holder).tvCount.setText("" + answerEntity.getCmmt_count());

            postId = answerEntity.getPost_id();
            if (answerEntity.getUpvote() == 0)
                ((HeadViewHolder) holder).ivPoint.setImageResource(R.mipmap.point);
            else
                ((HeadViewHolder) holder).ivPoint.setImageResource(R.mipmap.point_press);

            //点赞按钮
            ((HeadViewHolder) holder).llPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    posts_upvotet(postId);
                }
            });


        } else {
            final AnswerEntity.CmmtBean cmmtBean = cmmtBeans.get(position - 1);
            ((ViewHolder) holder).tvNick.setText(cmmtBean.getUser_name());
            ((ViewHolder) holder).tvContent.setText(cmmtBean.getCmmt_content());
            ((ViewHolder) holder).tvTime.setText(cmmtBean.getCreate_time());
            if (cmmtBean.getCmmt_upvote() == 0)
                ((ViewHolder) holder).ivPoint.setImageResource(R.mipmap.hand);
            else
                ((ViewHolder) holder).ivPoint.setImageResource(R.mipmap.hand_press);

            Glide.with(mContext)
                    .load(Const.PIC_URL + cmmtBean.getUser_icon())
                    .placeholder(R.mipmap.default_head)
                    .dontAnimate()
                    .into(((ViewHolder) holder).cvHead);

            ((ViewHolder) holder).tvPoint.setText("" + cmmtBean.getCmmt_upvoteCount());
            //评论点赞按钮
            ((ViewHolder) holder).llPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cmmtBean.getCmmt_upvote() == 0)
                        sendData(cmmtBean.getCmmt_id(), cmmtBean.getUser_id());
                    else
                        Toast.makeText(mContext, "你已经点过赞了", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ITEM_TYPE_HEAD;
        else
            return ITEM_TYPE_MAIN;
    }


    @Override
    public int getItemCount() {
        return cmmtBeans.size() + 1;
    }


    /**
     * 随手评论拍点赞
     *
     * @param body_id 随手拍的id
     * @param cmmt_id 评论的id
     */
    private void sendData(int cmmt_id, int body_id) {
        user_id = PreferencesUtils.getInt(mContext, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(mContext, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(mContext, Const.MOBILE_PHONE);
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        Map<String, Object> params1 = new HashMap<>();
        params1.put("user_id", body_id);
        params1.put("post_id", postId);
        params1.put("cmmt_id", cmmt_id);
        Call<ResultEntity> call = api.cmmt_upvote(params, params1);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 获取成功

                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }


    /**
     * 随手拍点赞
     *
     * @param body_id 随手拍的id
     */
    private void posts_upvotet(int body_id) {
        user_id = PreferencesUtils.getInt(mContext, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(mContext, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(mContext, Const.MOBILE_PHONE);
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        Map<String, Object> params1 = new HashMap<>();
        params1.put("user_id", body_id);
        params1.put("post_id", postId);
        Call<ResultEntity> call = api.posts_upvotet(params, params1);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 获取成功

                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }
}
