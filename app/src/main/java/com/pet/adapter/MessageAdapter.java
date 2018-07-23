package com.pet.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.MessageActivity;
import com.pet.activity.RefuseCallBackActivity;
import com.pet.bean.AppointPetEntity;
import com.pet.bean.Const;
import com.pet.bean.MessageEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/7/13.
 * 消息管理适配器
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //0：关注 1：约宠 2：点赞 3：评论 4：回复 5：打赏 6：寄养 7：同意或不同意寄养 8：同意或不同意约宠 9:喂养宠物
    private static final int TYPE_ATTENTION = 0;//关注
    private static final int TYPE_APPOINT_PET = 1;
    private static final int TYPE_GIVE_LIKE = 2;
    private static final int TYPE_COMMENT = 3;
    private static final int TYPE_REPLY = 4;
    private static final int TYPE_REWARD = 5;
    private static final int TYPE_FOSTER = 6;
    private static final int TYPE_AGREE_FOSTER = 7;
    private static final int TYPE_AGREE_APPOINT = 8;
    private static final int TYPE_FEED_PET = 9;

    private List<MessageEntity> list;
    private Activity mContext;


    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private String reason = "";

    public MessageAdapter(List<MessageEntity> list, Activity mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //0：关注 1：约宠 2：点赞 3：评论 4：回复 5：打赏 6：寄养 7：同意或不同意寄养 8：同意或不同意约宠 9:喂养宠物
        if (viewType == TYPE_ATTENTION) {//关注
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_attention_item, parent, false);
            return new AttentionHolder(view);
        } else if (viewType == TYPE_APPOINT_PET) {//约宠
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_appoint_item, parent, false);
            return new AppointPetHolder(view);
        } else if (viewType == TYPE_GIVE_LIKE) {//点赞
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_givelike_item, parent, false);
            return new GiveLikeHolder(view);
        } else if (viewType == TYPE_COMMENT) {//评论
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_givelike_item, parent, false);
            return new CommentHolder(view);
        } else if (viewType == TYPE_REPLY) {//回复
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_givelike_item, parent, false);
            return new GiveLikeHolder(view);
        } else if (viewType == TYPE_REWARD) {//打赏
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_givelike_item, parent, false);
            return new RewardHolder(view);
        } else if (viewType == TYPE_FOSTER) {//寄养
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_attention_item, parent, false);
            return new FosterHolder(view);
        } else if (viewType == TYPE_AGREE_FOSTER) {//是否接受寄养
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_attention_item, parent, false);
            return new AgreeFosterHolder(view);
        } else if (viewType == TYPE_AGREE_APPOINT) {//是否接受约宠
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_attention_item, parent, false);
            return new AgreeAppointHolder(view);
        } else if (viewType == TYPE_FEED_PET) {//喂宠物
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_givelike_item, parent, false);
            return new FeedPetHolder(view);
        }

        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MessageEntity messageEntity = list.get(position);
        //关注
        if (holder instanceof AttentionHolder) {
            ((AttentionHolder) holder).tvName.setText(messageEntity.getUser_name());
            ((AttentionHolder) holder).tvTime.setText("关注了你 " + messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((AttentionHolder) holder).cvHead);
            //约宠
        } else if (holder instanceof AppointPetHolder) {
            ((AppointPetHolder) holder).tvName.setText(messageEntity.getUser_name() + ":   约宠申请");
            ((AppointPetHolder) holder).tvTime.setText("向你申请约宠 " + messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((AppointPetHolder) holder).cvHead);

            //约宠查看详情
            ((AppointPetHolder) holder).btnAppoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    show_appointment(messageEntity.getAppointment_id());
                }
            });


            //点赞
        } else if (holder instanceof GiveLikeHolder) {
            ((GiveLikeHolder) holder).tvName.setText(messageEntity.getUser_name());
            ((GiveLikeHolder) holder).tvTime.setText("赞了你的评论 " + messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((GiveLikeHolder) holder).cvHead);
            //评论
        } else if (holder instanceof CommentHolder) {
            ((CommentHolder) holder).tvName.setText(messageEntity.getUser_name() + " :" + messageEntity.getContent());
            ((CommentHolder) holder).tvTime.setText("评论了你的话题 " + messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((CommentHolder) holder).cvHead);
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getImg_url())
                    .dontAnimate()
                    .placeholder(R.mipmap.take_photo_loading)
                    .into(((CommentHolder) holder).imageView);
            //回复
        } else if (holder instanceof ReplyHolder) {
            ((ReplyHolder) holder).tvName.setText(messageEntity.getUser_name() + " :" + messageEntity.getContent());
            ((ReplyHolder) holder).tvTime.setText("评论了你的话题 " + messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((ReplyHolder) holder).cvHead);
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getImg_url())
                    .dontAnimate()
                    .placeholder(R.mipmap.take_photo_loading)
                    .into(((ReplyHolder) holder).imageView);
            //打赏
        } else if (holder instanceof RewardHolder) {
            ((RewardHolder) holder).tvName.setText(messageEntity.getUser_name() + " :" + "+" + messageEntity.getFood_count() + "宠粮");
            ((RewardHolder) holder).tvTime.setText("打赏了你的动态 " + messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((RewardHolder) holder).cvHead);
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getImg_url())
                    .dontAnimate()
                    .placeholder(R.mipmap.take_photo_loading)
                    .into(((RewardHolder) holder).imageView);
            //寄养
        } else if (holder instanceof FosterHolder) {
            ((FosterHolder) holder).tvName.setText(messageEntity.getUser_name() + " 寄养申请");
            ((FosterHolder) holder).tvTime.setText(messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((FosterHolder) holder).cvHead);
            //是否接受寄养
        } else if (holder instanceof AgreeFosterHolder) {
            ((AgreeFosterHolder) holder).tvName.setText(messageEntity.getUser_name() + " 是否接受寄养");
            ((AgreeFosterHolder) holder).tvTime.setText(messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((AgreeFosterHolder) holder).cvHead);
            //是否接受约宠
        } else if (holder instanceof AgreeAppointHolder) {
            if (messageEntity.getAppointment_status() == 0) {
                ((AgreeAppointHolder) holder).tvTime.setText("拒绝了你的约宠 " + messageEntity.getCreate_time());
                ((AgreeAppointHolder) holder).tvName.setText(messageEntity.getUser_name() + ": " + messageEntity.getRefuse_reason());
            } else if (messageEntity.getAppointment_status() == 1) {
                ((AgreeAppointHolder) holder).tvName.setText(messageEntity.getUser_name() + " 接受了你的约宠");
                ((AgreeAppointHolder) holder).tvTime.setText(messageEntity.getCreate_time());
            }

            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((AgreeAppointHolder) holder).cvHead);
        } else if (holder instanceof FeedPetHolder) {
            ((FeedPetHolder) holder).tvName.setText(messageEntity.getUser_name() + " +" + messageEntity.getFood_count() + "宠粮");
            ((FeedPetHolder) holder).tvTime.setText("喂了你的宠物 " + messageEntity.getCreate_time());
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getUser_icon())
                    .dontAnimate()
                    .placeholder(R.mipmap.default_head)
                    .into(((FeedPetHolder) holder).cvHead);
            Glide.with(mContext).load(Const.PIC_URL + messageEntity.getImg_url())
                    .dontAnimate()
                    .placeholder(R.mipmap.take_photo_loading)
                    .into(((FeedPetHolder) holder).imageView);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageEntity entity = list.get(position);
        if (entity.getMessage_type() == TYPE_ATTENTION)
            return TYPE_ATTENTION;
        else if (entity.getMessage_type() == TYPE_APPOINT_PET)
            return TYPE_APPOINT_PET;
        else if (entity.getMessage_type() == TYPE_GIVE_LIKE)
            return TYPE_GIVE_LIKE;
        else if (entity.getMessage_type() == TYPE_COMMENT)
            return TYPE_COMMENT;
        else if (entity.getMessage_type() == TYPE_REPLY)
            return TYPE_REPLY;
        else if (entity.getMessage_type() == TYPE_REWARD)
            return TYPE_REWARD;
        else if (entity.getMessage_type() == TYPE_FOSTER)
            return TYPE_FOSTER;
        else if (entity.getMessage_type() == TYPE_AGREE_FOSTER)
            return TYPE_AGREE_FOSTER;
        else if (entity.getMessage_type() == TYPE_AGREE_APPOINT)
            return TYPE_AGREE_APPOINT;
        else
            return TYPE_FEED_PET;
    }

    //关注的viewHolder
    static class AttentionHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;

        public AttentionHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    //约宠的viewHolder
    static class AppointPetHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;
        Button btnAppoint;

        AppointPetHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnAppoint = itemView.findViewById(R.id.btn_appoint);
        }
    }

    //点赞的viewHolder
    static class GiveLikeHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;

        public GiveLikeHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    //评论的viewHolder
    static class CommentHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;
        ImageView imageView;

        public CommentHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }

    //回复的viewHolder
    static class ReplyHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;
        ImageView imageView;

        public ReplyHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageView = itemView.findViewById(R.id.iv_image);
        }

    }

    //打赏的viewHolder
    static class RewardHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;
        ImageView imageView;

        public RewardHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageView = itemView.findViewById(R.id.iv_image);
        }

    }

    //寄养的viewHolder
    static class FosterHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;

        public FosterHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

    }

    //同意或不同意寄养的viewHolder
    static class AgreeFosterHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;

        public AgreeFosterHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

    }

    //同意或不同意约宠的viewHolder
    static class AgreeAppointHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;

        public AgreeAppointHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

    }

    //喂宠物
    static class FeedPetHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvTime;
        ImageView imageView;

        public FeedPetHolder(View itemView) {
            super(itemView);
            cvHead = itemView.findViewById(R.id.head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }

    //展示约宠详情的popWindow
    @SuppressLint("SetTextI18n")
    private void showAppointPetWindow(final AppointPetEntity entity) {
        View popView = mContext.getLayoutInflater().inflate(R.layout.layout_show_appoint_pop, null);
        final PopupWindow popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvName = popView.findViewById(R.id.tv_userName);
        tvName.setText(entity.getUser_name());
        CircleImageView cvHead = popView.findViewById(R.id.iv_appoint_head);
        Glide.with(mContext).load(Const.PIC_URL + entity.getUser_icon())
                .dontAnimate()
                .placeholder(R.mipmap.take_photo_loading)
                .into(cvHead);

        CircleImageView cvPet = popView.findViewById(R.id.iv_appointed_head);
        Glide.with(mContext).load(Const.PIC_URL + entity.getPet_icon())
                .dontAnimate()
                .placeholder(R.mipmap.take_photo_loading)
                .into(cvPet);

        TextView tvNumber = popView.findViewById(R.id.tv_number);
        tvNumber.setText(entity.getNumber_of_people() + " 位");

        TextView tvTime = popView.findViewById(R.id.tv_time);
        tvTime.setText(entity.getAppointment_time());

        TextView tvAddress = popView.findViewById(R.id.tv_address);
        tvAddress.setText(entity.getAppointment_location());

        //拒绝约宠
        LinearLayout llRefuse = popView.findViewById(R.id.ll_refuse);
        TextView tvRefuse = popView.findViewById(R.id.tv_refuse);

        //接受约宠
        LinearLayout llAccept = popView.findViewById(R.id.ll_accept);
        TextView tvAgree = popView.findViewById(R.id.tv_agree);

        if (entity.getAppointment_status() == 0)//拒绝
        {
            llAccept.setVisibility(View.GONE);
            tvRefuse.setText("已拒绝");
        } else if (entity.getAppointment_status() == 1)//已同意
        {
            llRefuse.setVisibility(View.GONE);
            tvAgree.setText("已同意");
        } else {
            //拒绝按钮
            llRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    Intent intent = new Intent(mContext, RefuseCallBackActivity.class);
                    intent.putExtra("appointment_id", entity.getAppointment_id());

                    mContext.startActivity(intent);
                }
            });
            //同意按钮
            llAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agree_appointment_pet(entity.getAppointment_id(), 1, entity.getUser_id(), entity.getUser_name());
                    popupWindow.dismiss();
                }
            });
        }

        popupWindow.setAnimationStyle(R.style.picLook);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        int width = mContext.getWindowManager().getDefaultDisplay().getWidth();
        popupWindow.setWidth(width * 4 / 5);

        popupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
    }

    //获取约宠信息
    private void show_appointment(int appointment_id) {
        user_id = PreferencesUtils.getInt(mContext, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(mContext, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(mContext, Const.MOBILE_PHONE);
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("appointment_id", appointment_id);

        Call<ResultEntity> call = api.show_appointment(params);
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
                    AppointPetEntity entity = JSON.parseObject(result.getData().toString(), AppointPetEntity.class);
                    showAppointPetWindow(entity);

                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    /**
     * 拒绝或者同意约宠
     *
     * @param appointment_agree 0:拒绝 1同意
     **/
    private void agree_appointment_pet(int appointment_id, int appointment_agree, final int userId, final String title) {
        user_id = PreferencesUtils.getInt(mContext, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(mContext, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(mContext, Const.MOBILE_PHONE);
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("appointment_id", appointment_id);

        params.put("reason", reason);
        params.put("appointment_agree", appointment_agree);

        Call<ResultEntity> call = api.agree_appointment_pet(params);
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
                    sendMessage(userId);
                    RongIM.getInstance().startPrivateChat(mContext, "" + userId, title);

                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    private void sendMessage(int userId) {
        // 构建文本消息实例
        TextMessage textMessage = TextMessage.obtain("我已接受你的申请");
        Message myMessage = Message.obtain(String.valueOf(userId), Conversation.ConversationType.PRIVATE, textMessage);
        RongIMClient.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });

    }


}
