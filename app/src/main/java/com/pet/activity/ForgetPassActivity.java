package com.pet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.CountDownTimerUtils;
import com.pet.utils.FitStateUI;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 忘记密码页面
 */
public class ForgetPassActivity extends BaseActivity {

    private Context mContext;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.btn_next)
    Button mBtNext;
    @BindView(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @BindView(R.id.et_phone_code)
    EditText mEtPhoneCode;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.et_check_pass)
    EditText mEtNewPass;
    @BindView(R.id.pass)
    EditText mEtPass;

    private String phoneNumber;
    private String code;
    private String newPass;
    private String pass;

    private ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.changeStatusBarTextColor(this, false);
        setStatusBarColor(R.color.main_color_press);

        setContentView(R.layout.activity_forget_pass);
        ButterKnife.bind(this);
        mContext = this;
        initData();
    }

    @OnClick({R.id.tv_code, R.id.btn_next, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                checkCode();
                break;
            case R.id.btn_next:
                checkData();
                break;
            case R.id.iv_back:
                finish();
            default:
                break;
        }
    }

    //给用户输入框添加监听
    private void initData() {
        mEtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9)
                                && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    mEtPhoneNumber.setText(sb.toString());
                    mEtPhoneNumber.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    /**
     * 检查用户输入的验证码
     */
    private void checkCode() {
        phoneNumber = mEtPhoneNumber.getText().toString().trim();
        phoneNumber = phoneNumber.replace(' ', ',');
        phoneNumber = phoneNumber.replaceAll(",", "");
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("请先输入手机号");
        } else {
            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(
                    mTvCode, 30000, 1000);
            mCountDownTimerUtils.start();
            //执行相关的网络代码
            sendCode();
        }
    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    Toast.makeText(mContext, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO 处理错误的结果
                    Toast.makeText(mContext, "" + data.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    checkData();
                } else {
                    // TODO 处理错误的结果
                    Toast.makeText(mContext, "验证失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    /**
     * 检查用户的输入信息
     */
    private void checkData() {
        code = mEtPhoneCode.getText().toString().trim();
        newPass = mEtNewPass.getText().toString().trim();
        pass = mEtPass.getText().toString().trim();
        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(pass)) {
            showToast("请填写完整数据");
        } else {
            //提交修改密码
            checkSms();
        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //发送验证码
    private void sendCode() {
        final ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phoneNumber);

        Call<ResultEntity> call = api.sendSms(params);
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
                    Toast.makeText(mContext, "验证码发送成功", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    //忘记密码调用
    private void forgetPass() {
        showProgressDialog();
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, String> params = new HashMap<>();
        params.put("mobilephone", phoneNumber);
        params.put("password", newPass);
        params.put("confirm_password", pass);
        Call<ResultEntity> call = api.forgetPwd(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 注册成功
                    //跳转登陆界面
                    hideProgressDialog();
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    skipPageWithAnim(LoginActivity.class);
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


    //检验验证码
    private void checkSms() {
        showProgressDialog();
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, String> params = new HashMap<>();
        params.put("phone", phoneNumber);
        params.put("code", code);
        Call<ResultEntity> call = api.checkSms(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 注册成功
                    //提交数据
                    boolean data = (boolean) result.getData();
                    if (data)
                    forgetPass();
                    else {
                        hideProgressDialog();
                        Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
