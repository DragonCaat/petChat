package com.pet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 拒绝约宠界面
 */
public class RefuseCallBackActivity extends BaseActivity {

    @BindView(R.id.et_user_input)
    EditText mEtUserInput;

    private Context mContext;

    private String content = "";

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";


    private int appointment_id;

    private ProgressDialog proDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuse_call_back);
        Intent intent = getIntent();
        appointment_id = intent.getIntExtra("appointment_id",0);

        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.iv_back,R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_ok:

                checkData();
                break;
            default:

                break;
        }
    }


    private void checkData() {
        content = mEtUserInput.getText().toString().trim();
        if (TextUtils.isEmpty(content))
            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
        else
            sendData();
    }

    //提交拒绝理由申请数据
    private void sendData() {
        showProgressDialog();
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        params.put("appointment_id", appointment_id);
        params.put("reason", content);
        params.put("appointment_agree", 0);
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
                if (res == 200) {// 提交成功
                    hideProgressDialog();
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    hideProgressDialog();
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResultEntity> arg0, Throwable arg1) {

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
