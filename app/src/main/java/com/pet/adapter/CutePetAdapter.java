package com.pet.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.CutePetEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;
import com.pet.view.CircleNumberProgress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/6/27.
 * 萌宠适配器
 */

public class CutePetAdapter extends RecyclerView.Adapter<CutePetAdapter.ViewHolder> {

    private Activity mContext;

    private List<CutePetEntity> list;
    int fans_count;
    int Pet_hungry_leve;

    private int userId = 0;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    public CutePetAdapter(Activity activity, List<CutePetEntity> list) {
        this.mContext = activity;
        this.list = list;
        user_id = PreferencesUtils.getInt(mContext, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(mContext, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(mContext, Const.MOBILE_PHONE);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cvPetHead;
        TextView tvPetName;
        TextView tvPetBirthday;
        TextView tvPetKind;
        TextView tvPetGender;
        TextView tvPetStatue;

        ImageView ivLove1;
        ImageView ivLove2;
        ImageView ivLove3;
        ImageView ivLove4;
        ImageView ivLove5;

        Button btnFeed;
        CircleNumberProgress circleNumberProgress;
        TextView tvMeal;

        public ViewHolder(View itemView) {
            super(itemView);
            cvPetHead = itemView.findViewById(R.id.cv_pet_head);
            tvPetName = itemView.findViewById(R.id.tv_pet_name);
            tvPetBirthday = itemView.findViewById(R.id.tv_pet_birthday);
            tvPetKind = itemView.findViewById(R.id.tv_pet_kind);
            tvPetGender = itemView.findViewById(R.id.tv_pet_gender);
            tvPetStatue = itemView.findViewById(R.id.tv_pet_statue);

            ivLove1 = itemView.findViewById(R.id.iv_love1);
            ivLove2 = itemView.findViewById(R.id.iv_love2);
            ivLove3 = itemView.findViewById(R.id.iv_love3);
            ivLove4 = itemView.findViewById(R.id.iv_love4);
            ivLove5 = itemView.findViewById(R.id.iv_love5);

            btnFeed = itemView.findViewById(R.id.btn_feed);
            circleNumberProgress = itemView.findViewById(R.id.cn_progress);

            tvMeal = itemView.findViewById(R.id.tv_meal);
        }
    }

    @Override
    public CutePetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pet_item, parent, false);
        return new CutePetAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CutePetAdapter.ViewHolder holder, int position) {

        final CutePetEntity entity = list.get(position);
        if (entity.getPet_gender() == 1)
            holder.tvPetGender.setText("女");
        else
            holder.tvPetGender.setText("男");

        holder.tvPetKind.setText(entity.getPet_kind());
        holder.tvPetBirthday.setText(entity.getPet_brith());
        holder.tvPetName.setText(entity.getPet_name());
        holder.tvPetStatue.setText(entity.getPet_status());

        holder.btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = PreferencesUtils.getInt(mContext, Const.USER_ID);
                if (userId == entity.getUser_id())
                    Toast.makeText(mContext, "不能喂自己的宠物哦", Toast.LENGTH_SHORT).show();
                else
                    get_pet_food(entity.getUser_id(), entity.getPet_id());
            }
        });

        Pet_hungry_leve = entity.getPet_hungry_level();
        int number = Pet_hungry_leve / 10;
        //设置饭量百分比
        holder.circleNumberProgress.setProgress(number);
        //设置饭量
        holder.tvMeal.setText("1000 ／" + Pet_hungry_leve);

        Glide.with(mContext)
                .load(Const.PIC_URL + entity.getPet_icon())
                .placeholder(R.mipmap.default_head)
                .dontAnimate()
                .into(holder.cvPetHead);
        //表示很无语的界面
        fans_count = entity.getFans_count();
        if (fans_count < 1) {
            showLove(holder.ivLove1, false);
            showLove(holder.ivLove2, false);
            showLove(holder.ivLove3, false);
            showLove(holder.ivLove4, false);
            showLove(holder.ivLove5, false);
        } else if (fans_count < 10 && fans_count > 0) {
            showLove(holder.ivLove1, true);
            showLove(holder.ivLove2, false);
            showLove(holder.ivLove3, false);
            showLove(holder.ivLove4, false);
            showLove(holder.ivLove5, false);
        } else if (fans_count < 30 && fans_count > 9) {
            showLove(holder.ivLove1, true);
            showLove(holder.ivLove2, true);
            showLove(holder.ivLove3, false);
            showLove(holder.ivLove4, false);
            showLove(holder.ivLove5, false);
        } else if (fans_count < 70 && fans_count > 29) {
            showLove(holder.ivLove1, true);
            showLove(holder.ivLove2, true);
            showLove(holder.ivLove3, true);
            showLove(holder.ivLove4, false);
            showLove(holder.ivLove5, false);
        } else if (fans_count < 100 && fans_count > 69) {
            showLove(holder.ivLove1, true);
            showLove(holder.ivLove2, true);
            showLove(holder.ivLove3, true);
            showLove(holder.ivLove4, true);
            showLove(holder.ivLove5, false);
        } else {
            showLove(holder.ivLove1, true);
            showLove(holder.ivLove2, true);
            showLove(holder.ivLove3, true);
            showLove(holder.ivLove4, true);
            showLove(holder.ivLove5, true);
        }
    }

    private void showLove(ImageView imageView, boolean status) {
        if (status)
            imageView.setImageResource(R.mipmap.love_full);
        else
            imageView.setImageResource(R.mipmap.love);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    TextView tvFirst;
    TextView tvSecond;
    TextView tvThird;
    EditText etUserReward;

    int food_count = 10;

    int petFood = 0;

    TextView tvFoodCount;
    Dialog dialog;

    //展示喂宠物的弹框
    @SuppressLint("SetTextI18n")
    private void showDialog(final int be_user_id, final int pet_id) {
        dialog = new Dialog(mContext, R.style.dialogStyle);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.layout_reward_item, null);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        tvTitle.setText("喂他");
        tvFoodCount = dialogView.findViewById(R.id.tv_pet_food);
        tvFoodCount.setText("你的宠粮：" + petFood);
        Button btnsuer = dialogView.findViewById(R.id.btn_sure);
        btnsuer.setText("喂他");
        //喂宠物
        btnsuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food_count > petFood)
                    Toast.makeText(mContext, "您的宠粮不足", Toast.LENGTH_SHORT).show();
                else
                    pet_hungry_level(be_user_id, pet_id);
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

    //喂宠物
    private void pet_hungry_level(int be_user_id, int pet_id) {
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        params.put("be_user_id", be_user_id);
        params.put("pet_id", pet_id);
        params.put("food_count", food_count);

        Call<ResultEntity> call = api.pet_hungry_level(params);
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

                    //tvFoodCount.setText("你的宠粮：" + (petFood - food_count));
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

    //获取宠粮数量
    private void get_pet_food(final int be_user_id, final int pet_id) {
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
                    showDialog(be_user_id, pet_id);

                } else {

                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

}
