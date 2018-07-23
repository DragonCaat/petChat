package com.pet.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.pet.activity.ShowUserActivity;
import com.pet.activity.TopicDetailActivity;
import com.pet.baseadapter.BaseAdapter;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.ResultEntity;
import com.pet.ninelayout.NineGridTestLayout;
import com.pet.ninelayout.OnItemPictureClickListener;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/5/1.
 * 被装饰类要和装饰类继承自同一父类
 */

public class HotTakePhotoAdapter extends BaseAdapter<DynamicEntity> {
    private static Activity mContext;

    private OnItemPictureClickListener listener1;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";


    public HotTakePhotoAdapter(Activity context, OnItemPictureClickListener listener1) {
        mContext = context;
        this.listener1 = listener1;
        user_id = PreferencesUtils.getInt(mContext, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(mContext, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(mContext, Const.MOBILE_PHONE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_attention_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DynamicEntity dynamicEntity = getDataSet().get(position);
        ((MyViewHolder) holder).bind(dynamicEntity, position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;
        CircleImageView cvHeadImage;
        TextView tvNickName;
        TextView tvContent;
        LinearLayout llAnswer;
        TextView tvTime;
        TextView tvPoint;
        //ImageView ivPoint;
        TextView tvAnswer;
        ImageView ivReward;
        TextView tvAnswerName;
        TextView tvAnswerContent;
        TextView tvAnsweCount;
        ImageView ivPoint;

        LinearLayout linearLayout;
        //点赞
        LinearLayout llPoint;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.images);
            cvHeadImage = itemView.findViewById(R.id.circleImage_user);
            tvNickName = itemView.findViewById(R.id.tv_nickName);
            tvContent = itemView.findViewById(R.id.tv_content);
            llAnswer = itemView.findViewById(R.id.ll_answer);
            tvTime = itemView.findViewById(R.id.tv_time);

            tvPoint = itemView.findViewById(R.id.tv_point);
            ivPoint = itemView.findViewById(R.id.iv_point);
            tvAnswer = itemView.findViewById(R.id.tv_answer);
            ivReward = itemView.findViewById(R.id.iv_reward);

            tvAnswerName = itemView.findViewById(R.id.tv_answer_name);
            tvAnswerContent = itemView.findViewById(R.id.tv_answer_content);
            tvAnsweCount = itemView.findViewById(R.id.tv_answer_counet);
            linearLayout = itemView.findViewById(R.id.ll_answer_bottom);

            llPoint = itemView.findViewById(R.id.ll_point);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final DynamicEntity dynamicEntity, int position) {
            layout.setListener(listener1);
            layout.setItemPosition(position);
            layout.setIsShowAll(false); //当传入的图片数超过9张时，是否全部显示
            layout.setSpacing(5); //动态设置图片之间的间隔
            layout.setUrlList(dynamicEntity.getImg_urls());


            tvAnswer.setText("" + dynamicEntity.getCmmt_count());
            tvPoint.setText("" + dynamicEntity.getUpvoteCount());
            tvTime.setText(dynamicEntity.getSend_time());
            tvNickName.setText(dynamicEntity.getUser_name());
            tvContent.setText(dynamicEntity.getContent());
            //判断是否点过赞
            if (dynamicEntity.getUpvote() == 0)
                ivPoint.setImageResource(R.mipmap.point);
            else
                ivPoint.setImageResource(R.mipmap.point_press);

            if (dynamicEntity.getFst_cmmt() != null) {
                tvAnswerName.setText(dynamicEntity.getFst_cmmt().getUser_name() + " :");
                tvAnswerContent.setText(dynamicEntity.getFst_cmmt().getCmmt_content());
                tvAnsweCount.setText("共" + dynamicEntity.getCmmt_count() + "条回复 >");
            } else {
                tvAnswerName.setVisibility(View.GONE);
                tvAnswerContent.setVisibility(View.GONE);
                tvAnsweCount.setText("当前无评论,点击评论 >");
            }

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TopicDetailActivity.class);
                    //intent.putExtra("nick", dynamicEntity.getUser_name());
                    //intent.putExtra("content", dynamicEntity.getContent());
                    //intent.putExtra("head", dynamicEntity.getUser_icon());
                    intent.putExtra("id", dynamicEntity.getUser_id());
                    intent.putExtra("postId", dynamicEntity.getPost_id());
                    intent.putExtra("DynamicId", dynamicEntity.getDynamic_id());
                    //intent.putStringArrayListExtra("urls", (ArrayList<String>) dynamicEntity.getImg_urls());
                    mContext.startActivity(intent);
                }
            });

            //头像
            Glide.with(mContext)
                    .load(Const.PIC_URL + dynamicEntity.getUser_icon())
                    .placeholder(R.mipmap.default_head)
                    .into(cvHeadImage);
            //设置头像的点击事件
            cvHeadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转展示用户信息的页面
                    Intent intent = new Intent(mContext, ShowUserActivity.class);
                    intent.putExtra("id", dynamicEntity.getUser_id());
                    mContext.startActivity(intent);
                }
            });

            //所有回复按钮
            llAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TopicDetailActivity.class);
                    intent.putExtra("id", dynamicEntity.getUser_id());
                    intent.putExtra("postId", dynamicEntity.getPost_id());
                    mContext.startActivity(intent);
                }
            });

            //打赏按钮
            ivReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    get_pet_food(dynamicEntity.getPost_id(), dynamicEntity.getUser_id());
                }
            });

            //点赞
            llPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dynamicEntity.getUpvote() == 0)
                        posts_upvotet(dynamicEntity.getPost_id(), ivPoint,tvPoint,dynamicEntity.getUpvoteCount());
                    else
                        Toast.makeText(mContext, "您已经点过赞了", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private TextView tvFirst;
    private TextView tvSecond;
    private TextView tvThird;
    private EditText etUserReward;
    private TextView tvFoodCount;
    private Dialog dialog;
    private int food_count = 10;
    private int petFood = 0;

    //展示喂宠物的弹框
    @SuppressLint("SetTextI18n")
    private void showDialog(final int id, final int bereward_user_id) {
        dialog = new Dialog(mContext, R.style.dialogStyle);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.layout_reward_item, null);

        tvFoodCount = dialogView.findViewById(R.id.tv_pet_food);
        tvFoodCount.setText("你的宠粮：" + petFood);
        Button btnsuer = dialogView.findViewById(R.id.btn_sure);
        //喂宠物
        btnsuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food_count > petFood)
                    Toast.makeText(mContext, "您的宠粮不足", Toast.LENGTH_SHORT).show();
                else
                    user_reward(id, bereward_user_id);
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
        int height = mContext.getWindowManager().getDefaultDisplay().getHeight();
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
        ApiService api = RetrofitClient.getInstance(mContext).Api();
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

                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
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
        ApiService api = RetrofitClient.getInstance(mContext).Api();
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
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } else {

                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
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

    /**
     * 随手拍点赞
     * @param postId 随手拍的id
     */
    private void posts_upvotet(int postId, final ImageView imageView, final TextView textView, final int pointCount) {
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
        //params1.put("user_id", postId);
        params1.put("post_id", postId);
        Call<ResultEntity> call = api.posts_upvotet(params, params1);
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
                if (res == 200) {// 获取成功
                    imageView.setImageResource(R.mipmap.point_press);
                    int pointCount1 = pointCount + 1;
                    textView.setText("" + pointCount1);
                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();

                   // notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

}
