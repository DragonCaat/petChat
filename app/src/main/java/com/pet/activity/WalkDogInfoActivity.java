package com.pet.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.ResultEntity;
import com.pet.bean.UserInfoEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 约宠信息填写按钮
 */
public class WalkDogInfoActivity extends BaseActivity {
    private static final int RESULT_CODE = 1;

    @BindView(R.id.cv_head)
    CircleImageView cvHead;
    @BindView(R.id.tv_location)
    EditText etLocation;
    @BindView(R.id.tv_people)
    TextView tvPeople;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_remarkers)
    EditText etReMarker;

    private Context mContext;
    private String headUrl = "";
    private int petId = 0;

    private int peopleCount = 1;
    private String address = "";
    private String appointment_time = "";
    private String appointment_remarks = "";

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        headUrl = intent.getStringExtra("head");
        petId = intent.getIntExtra("pet_id", 0);

        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        setContentView(R.layout.activity_walk_dog_info);
        mContext = this;
        ButterKnife.bind(this);
        Glide.with(this)
                .load(Const.PIC_URL + headUrl)
                .placeholder(R.mipmap.default_head)
                .dontAnimate()
                .into(cvHead);
    }

    @SuppressLint("SetTextI18n")
    @OnClick({R.id.iv_back, R.id.iv_location, R.id.btn_cut, R.id.btn_add, R.id.iv_time_select, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            //选择定位信息
            case R.id.iv_location:
                Intent intent = new Intent(mContext, GetLocationActivity.class);
                startActivityForResult(intent, RESULT_CODE);
                break;
            //减少人
            case R.id.btn_cut:
                if (peopleCount > 1) {
                    peopleCount--;
                    tvPeople.setText(peopleCount + " 位");
                }
                break;
            case R.id.btn_add:
                peopleCount++;
                tvPeople.setText(peopleCount + " 位");
                break;
            //选择时间
            case R.id.iv_time_select:
                selectTime(tvTime);
                break;
            //提交约宠信息
            case R.id.btn_commit:
                checkUserInput();
                break;

            default:
                break;
        }
    }
    //检查用户的输入信息
    private void checkUserInput() {
        address = etLocation.getText().toString().trim();
        appointment_remarks = etReMarker.getText().toString().trim();
        if (TextUtils.isEmpty(address)||TextUtils.isEmpty(appointment_time)){
            Toast.makeText(mContext,"除备注外，其他不能为空",Toast.LENGTH_SHORT).show();
        }else {
            //提交
            appointmentPet();
        }
    }

    //时间选择器
    private void selectTime(final TextView textView) {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                textView.setText(time);
                appointment_time = time;
            }
        }, "2018-07-29 00:00", "2020-01-01 00:00");
        timeSelector.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE) {
            if (data != null) {
                String extra = data.getStringExtra("location");
                etLocation.setText(extra);
            }
        }
    }

    //提交约宠信息
    private void appointmentPet() {
        showProgressDialog();
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        params.put("pet_id", petId);
        params.put("appointment_location", address);
        params.put("number_of_people", peopleCount);
        params.put("appointment_time", appointment_time);
        params.put("appointment_remarks", appointment_remarks);

        Call<ResultEntity> call = api.appointmentPet(params);
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
                    hideProgressDialog();
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else{
                    hideProgressDialog();
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                hideProgressDialog();
            }
        });
    }

    //展示加载对话框
    private void showProgressDialog() {
        proDialog = android.app.ProgressDialog.show(this, "", "正在提交...");
        proDialog.setCanceledOnTouchOutside(true);
    }

    private void hideProgressDialog() {
        if (proDialog != null)
            proDialog.dismiss();
    }
}
