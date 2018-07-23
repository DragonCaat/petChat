package com.pet.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.DynamicDetailActivity;
import com.pet.activity.ShowUserActivity;
import com.pet.bean.AttentionEntity;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.ResultEntity;
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
 * Created by dragon on 2018/6/12.
 * 动态的适配器
 */

public class MyAttentionAdapter extends RecyclerView.Adapter<MyAttentionAdapter.ViewHolder> {
    private List<DynamicEntity> list; // 数据源
    private Activity context;    // 上下文Context

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    public MyAttentionAdapter(List<DynamicEntity> list, Activity context) {
        // 初始化变量
        this.list = list;
        this.context = context;

        user_id = PreferencesUtils.getInt(context, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(context, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(context, Const.MOBILE_PHONE);
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
        CircleImageView cvHead;
        TextView tvName;

        TextView tvAddress;

        TextView tvPoint;
        TextView tvAnswer;
        ImageView ivReward;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
            tvImageNum = itemView.findViewById(R.id.tv_pic_number);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageView = itemView.findViewById(R.id.iv_image);
            linearLayout = itemView.findViewById(R.id.ll_attention_item);
            tvName = itemView.findViewById(R.id.tv_userName);
            cvHead = itemView.findViewById(R.id.cv_head);

            tvAddress = itemView.findViewById(R.id.tv_address);
            tvPoint = itemView.findViewById(R.id.tv_point);
            tvAnswer = itemView.findViewById(R.id.tv_answer);

            ivReward = itemView.findViewById(R.id.iv_reward);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.layout_my_attention_item, null,false);
        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 如果是正常的item，直接设置TextView的值
        final DynamicEntity entity = list.get(position);
        holder.textView.setText(entity.getContent());
        holder.tvImageNum.setText("共" + entity.getImg_urls().size() + "张");
        holder.tvTime.setText(entity.getSend_time());
        if (entity.getImg_urls().size() > 0){
            Glide.with(context)
                    .load(Const.PIC_URL + entity.getImg_urls().get(0))
                    .dontAnimate()
                    .placeholder(R.mipmap.take_photo_loading)
                    .into(holder.imageView);
        }

        holder.tvName.setText(entity.getUser_name());
        Glide.with(context)
                .load(Const.PIC_URL + entity.getUser_icon())
                .dontAnimate()
                .placeholder(R.mipmap.take_photo_loading)
                .into(holder.cvHead);
        //头像点击事件
        holder.cvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转展示用户信息的页面
                Intent intent = new Intent(context, ShowUserActivity.class);
                intent.putExtra("id", entity.getUser_id());
                context.startActivity(intent);
            }
        });



        String user_address = entity.getUser_address();
        if (TextUtils.isEmpty(user_address))
            holder.tvAddress.setText("该用户未上传地址");
        else
            holder.tvAddress.setText(user_address);

        holder.tvPoint.setText(""+entity.getUpvoteCount());
        holder.tvAnswer.setText(""+entity.getCmmt_count());

        //跳转动态详情界面
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DynamicDetailActivity.class);
                intent.putExtra("DynamicId",entity.getDynamic_id());
                intent.putExtra("flag",1);//是否是自己的动态判断1：不是自己 0：自己的动态
                context.startActivity(intent);
            }
        });

        //打赏界面
        holder.ivReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_pet_food(1,1);
            }
        });
    }


    private TextView tvFirst;
    private TextView tvSecond;
    private TextView tvThird;
    private EditText etUserReward;

    private TextView tvFoodCount;
    private Dialog dialog;
    private int food_count = 10;
    private int petFood = 0;

    //打赏动态的弹框
    @SuppressLint("SetTextI18n")
    private void showDialog(final int id, final int bereward_user_id) {
        dialog = new Dialog(context, R.style.dialogStyle);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_reward_item, null);

        tvFoodCount = dialogView.findViewById(R.id.tv_pet_food);
        tvFoodCount.setText("你的宠粮：" + petFood);
        Button btnsuer = dialogView.findViewById(R.id.btn_sure);
        //喂宠物
        btnsuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food_count > petFood)
                    Toast.makeText(context, "您的宠粮不足", Toast.LENGTH_SHORT).show();
                else
                    user_reward(id,bereward_user_id);
            }
        });

        tvFirst = dialogView.findViewById(R.id.tv_reward_first);
        tvFirst.setOnClickListener(listener);
        tvSecond = dialogView.findViewById(R.id.tv_reward_second);
        tvSecond.setOnClickListener(listener);
        tvThird = dialogView.findViewById(R.id.tv_reward_third);
        tvThird.setOnClickListener(listener);

        etUserReward = dialogView.findViewById(R.id.et_user_reward);
        etUserReward.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    setTextBg(tvFirst, false);
                    setTextBg(tvSecond, false);
                    setTextBg(tvThird, false);
                    String string = etUserReward.getText().toString().trim();
                    food_count = Integer.valueOf(string);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        int height = context.getWindowManager().getDefaultDisplay().getHeight();
        lp.height = height * 2 / 5;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        //外部可点击
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    //获取宠粮数量
    private void get_pet_food(final int id, final int bereward_user_id) {
        ApiService api = RetrofitClient.getInstance(context).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        Call<ResultEntity> call = api.get_pet_food(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {
                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 提交成功
                    petFood = Integer.parseInt(result.getData().toString());
                    showDialog(id, bereward_user_id);

                } else {

                    Toast.makeText(context, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    /**
     * 打赏
     * @param id               随手拍的id
     * @param bereward_user_id 被打赏者的id
     **/
    private void user_reward(int id, int bereward_user_id) {
        ApiService api = RetrofitClient.getInstance(context).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        params.put("reward_user_id", user_id);
        params.put("bereward_user_id", bereward_user_id);
        params.put("food_count", food_count);
        params.put("type", 0);//0：随手拍 1：动态
        params.put("id", id);

        Call<ResultEntity> call = api.user_reward(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {
                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 提交成功
                    Toast.makeText(context, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } else {

                    Toast.makeText(context, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_reward_first) {
                food_count = 10;
                setTextBg(tvFirst, true);
            } else
                setTextBg(tvFirst, false);

            if (view.getId() == R.id.tv_reward_second) {
                food_count = 50;
                setTextBg(tvSecond, true);
            } else
                setTextBg(tvSecond, false);

            if (view.getId() == R.id.tv_reward_third) {
                food_count = 100;
                setTextBg(tvThird, true);
            } else
                setTextBg(tvThird, false);
        }
    };

    private void setTextBg(TextView textView, boolean isCheck) {
        if (isCheck)
            textView.setBackgroundResource(R.drawable.rectangle_reward_white);
        else
            textView.setBackgroundResource(R.drawable.rectangle_reward_gray);
    }

}
