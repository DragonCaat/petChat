package com.pet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.RegisterEntity;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import retrofit2.Call;
import retrofit2.Response;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity {

    private final int SEND_MESSAGE_FAIL = 1;
    private final int SEND_MESSAGE_SUCCESS = 0;
    private Context mContext;
    @BindView(R.id.et_phoneNumber)
    EditText mEtNumber;
    @BindView(R.id.et_phone_code)
    EditText mEtPhoneCode;
    @BindView(R.id.et_pass)
    EditText mEtPass;
    @BindView(R.id.et_check_pass)
    EditText mEtCheckPass;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.rl_loading)
    RelativeLayout mRlLoading;
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    ImageView mIvBack;
    Button mBtnRegister;

    private String phoneNumber = "";
    private String code = "";
    private String passWord = "";
    private String checkPass = "";

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_MESSAGE_FAIL:
                    Toast.makeText(mContext, "验证码发送失败", Toast.LENGTH_SHORT).show();
                    break;

                case SEND_MESSAGE_SUCCESS:
                    Toast.makeText(mContext, "验证码已发送", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.changeStatusBarTextColor(this,false);
        setStatusBarColor(R.color.main_color_press);

        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        initData();
    }

    //给用户输入框添加监听
    private void initData() {
        mEtNumber.addTextChangedListener(new TextWatcher() {
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
                    mEtNumber.setText(sb.toString());
                    mEtNumber.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.btn_register, R.id.tv_code, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            //注册
            case R.id.btn_register:
                checkData();
                break;

            //验证码按钮
            case R.id.tv_code:
                checkCode();
                break;
            //返回按钮
            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    //检查验证码
    private void checkCode() {
        phoneNumber = mEtNumber.getText().toString().trim();
        phoneNumber = phoneNumber.replace(' ', ',');
        phoneNumber = phoneNumber.replaceAll(",", "");
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else {
            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(
                    mTvCode, 30000, 1000);
            mCountDownTimerUtils.start();
            sendCode("86", phoneNumber);

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
                    handler.sendEmptyMessage(SEND_MESSAGE_SUCCESS);
                    Log.i("hello", "checkCode: 验证码发送成功");
                } else {
                    // TODO 处理错误的结果
                    handler.sendEmptyMessage(SEND_MESSAGE_FAIL);
                    Log.i("hello", "checkCode: 验证码发送失败"+data.toString());
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
                    register();
                } else {
                    // TODO 处理错误的结果
                    Toast.makeText(mContext, "验证失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    //注册
    private void register() {
        loading(true);
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, String> params = new HashMap<>();
        params.put("mobilephone", phoneNumber);
        params.put("password", passWord);
        params.put("confirm_password", checkPass);
        Call<ResultEntity> call = api.register(params);
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
                    //跳转主界面
                    loading(false);
                    skipPageWithAnim(LoginActivity.class);
                    finish();
                } else {
                    loading(false);
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResultEntity> arg0, Throwable arg1) {

            }
        });

    }

    //检查用户输入信息
    private void checkData() {
        passWord = mEtPass.getText().toString().trim();
        checkPass = mEtCheckPass.getText().toString().trim();
        code = mEtPhoneCode.getText().toString().trim();

        phoneNumber = mEtNumber.getText().toString().trim();
        phoneNumber = phoneNumber.replace(' ', ',');
        phoneNumber = phoneNumber.replaceAll(",", "");

        if (TextUtils.isEmpty(passWord) || TextUtils.isEmpty(checkPass) || TextUtils.isEmpty(code))
            Toast.makeText(mContext, "请填写完整信息", Toast.LENGTH_SHORT).show();
        else
            //submitCode("86", phoneNumber, code);
            register();
    }

    //加载条
    private void loading(boolean show) {
        if (show) {
            mRlLoading.setVisibility(View.VISIBLE);
            Animation myAlphaAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);
            myAlphaAnimation.setInterpolator(new LinearInterpolator());
            mIvLoading.startAnimation(myAlphaAnimation);
        } else {
            mRlLoading.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }
}
