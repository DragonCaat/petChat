package com.pet.activity;

import android.app.Dialog;
import android.content.ContentProvider;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.NearPeopleAdapter;
import com.pet.bean.Const;
import com.pet.bean.NearByEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;
import com.pet.view.SlideButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/6/12.
 * 设置页面
 */

public class SetActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView mIvFinish;
    @BindView(R.id.slb_pet_location)
    SlideButton slbPetLocation;

    @BindView(R.id.slb_pet_foster)
    SlideButton slbPetFoster;

    @BindView(R.id.slb_pet_appoint)
    SlideButton slbPetAppoint;


    private Context mContext;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    //0:打开按钮 1：关闭按钮
    private int location_flag = 0;
    private int foster_flag = 0;
    private int appoint_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        ButterKnife.bind(this);

        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        location_flag = PreferencesUtils.getInt(this, Const.LOCATION_FLAG, 0);
        appoint_flag = PreferencesUtils.getInt(this, Const.APPOINT_FLAG, 0);

        mContext = this;
        initSlideButton();

    }

    //初始化slideButton默认打开
    private void initSlideButton() {
        if (location_flag == 0)
            slbPetLocation.setChecked(true);
        else
            slbPetLocation.setChecked(false);

        slbPetFoster.setChecked(true);

        if (appoint_flag == 0)
            slbPetAppoint.setChecked(true);
        else
            slbPetAppoint.setChecked(false);
    }


    @OnClick({R.id.iv_finish, R.id.tv_pet_manage, R.id.tv_about_our, R.id.tv_suggest, R.id.tv_forget_pass, R.id.tv_login_out, R.id.tv_message, R.id.slb_pet_location, R.id.slb_pet_appoint})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
//            case R.id.tv_my_data:
//                //Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
//                break;
            //宠物管理
            case R.id.tv_pet_manage:
                skipPageWithAnim(PetManageActivity.class);
                break;
            //关于我们
            case R.id.tv_about_our:
                skipPageWithAnim(AboutOurActivity.class);
                break;
            //意见反馈
            case R.id.tv_suggest:
                skipPageWithAnim(SuggestActivity.class);
                break;
            //忘记密码
            case R.id.tv_forget_pass:
                skipPageWithAnim(ForgetPassActivity.class);
                break;

            //退出登陆
            case R.id.tv_login_out:
                show();
                //Toast.makeText(this, "world", Toast.LENGTH_SHORT).show();
                break;

            //消息提醒
            case R.id.tv_message:
                skipPageWithAnim(MessageActivity.class);
                break;

            //是否在地图上显示宠物
            case R.id.slb_pet_location:
                if (location_flag == 0)//此时是开启状态
                    appoint_agree_or_not(0);
                else
                    appoint_agree_or_not(1);

                break;

            //是否在接受约宠
            case R.id.slb_pet_appoint:
                if (appoint_flag == 0)//此时是开启状态
                    appointment_or_not(0);
                else
                    appointment_or_not(1);
                break;
            default:
                break;
        }

    }

    private void show() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_item, null);

        bottomDialog.setContentView(view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.dialog_animation);
        bottomDialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams params = bottomDialog.getWindow().getAttributes();
        //params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;//点击后会出现系统导航栏
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;//点击后不会出现系统导航栏
        bottomDialog.getWindow().setAttributes(params);
        bottomDialog.getWindow().setNavigationBarColor(getResources().getColor(R.color.white));

        bottomDialog.show();

        TextView tvPositive = view.findViewById(R.id.tv_positive);
        //确认退出
        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        TextView tvCancle = view.findViewById(R.id.tv_cancel);
        //取消退出
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });

    }


    //清楚本地的数据
    private void cleanData() {

    }

    /**
     * 是否显示宠物的位置
     *
     * @param type 1:开启 0:关闭
     */
    private void appoint_agree_or_not(int type) {
        //xRecyclerView.setRefreshing(true);
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("type", type);

        Call<ResultEntity> call = api.appoint_agree_or_not(params);
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
                    if (location_flag == 0) {
                        PreferencesUtils.putInt(mContext, Const.LOCATION_FLAG, 1);
                        slbPetLocation.setChecked(false);
                        location_flag = 1;
                    } else {
                        PreferencesUtils.putInt(mContext, Const.LOCATION_FLAG, 0);
                        slbPetLocation.setChecked(true);
                        location_flag = 0;
                    }
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    /**
     * 是否打开约宠开关
     *
     * @param _switch 1:开启 0:关闭
     */
    private void appointment_or_not(int _switch) {
        //xRecyclerView.setRefreshing(true);
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("switch", _switch);

        Call<ResultEntity> call = api.appointment_or_not(params);
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
                    if (appoint_flag == 0) {
                        PreferencesUtils.putInt(mContext, Const.APPOINT_FLAG, 1);
                        slbPetAppoint.setChecked(false);
                        appoint_flag = 1;
                    } else {
                        PreferencesUtils.putInt(mContext, Const.APPOINT_FLAG, 0);
                        slbPetAppoint.setChecked(true);
                        appoint_flag = 0;
                    }
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }


}
