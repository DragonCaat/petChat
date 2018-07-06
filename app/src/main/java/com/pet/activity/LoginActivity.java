package com.pet.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.LoginEntity;
import com.pet.bean.RcEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import retrofit2.Call;
import retrofit2.Response;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity {

    private String TAG = "hello";
    private Context mContext;

    @BindView(R.id.et_userName)
    EditText mEtUserName;
    @BindView(R.id.et_passWord)
    EditText mEtPassWord;

    @BindView(R.id.iv_forget_pass)
    ImageView mIvForgetPass;
    @BindView(R.id.iv_register)
    ImageView mIvRegister;

    @BindView(R.id.rl_login_bg)
    RelativeLayout mRlLogin;
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    @BindView(R.id.rl_loading)
    RelativeLayout mRlLoading;

    private String username = "";
    private String password = "";

    private String RToken = "";
    private String accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        initData();

        mEtUserName.setText("17671714521");
        mEtPassWord.setText("12345678");

        permissions();
    }

    //给用户输入框添加监听
    private void initData() {
        mEtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRlLogin.setBackgroundResource(R.mipmap.login_bg);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEtPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRlLogin.setBackgroundResource(R.mipmap.login_bg);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

    //展示吐司
    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_login, R.id.ll_register, R.id.ll_forget_pass})
    public void onclick(View view) {
        switch (view.getId()) {
            //登陆
            case R.id.btn_login:
                checkData();
                //skipPageWithAnim(MainActivity.class);
                break;
            case R.id.ll_register:
                skipPageWithAnim(RegisterActivity.class);
                break;
            case R.id.ll_forget_pass:
                skipPageWithAnim(ForgetPassActivity.class);
                break;
            default:
                break;
        }
    }

    //登陆服务器
    private void login() {
        loading(true);
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, String> params = new HashMap<>();
        params.put("mobilephone", username);
        params.put("password", password);


        Call<ResultEntity> call = api.login(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 登录成功
                    LoginEntity loginData = JSON.parseObject(result.getData().toString(), LoginEntity.class);
                    if (loginData != null) {
                        saveData(loginData);
                        //获取融云token
                        getRongToken(loginData);
                    }
                } else {

                    loading(false);
                    mRlLogin.setBackgroundResource(R.mipmap.angry_bg);
                    Toast.makeText(LoginActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResultEntity> arg0, Throwable arg1) {

            }
        });

    }

    //保存用户的信息
    private void saveData(LoginEntity loginData) {
        PreferencesUtils.putString(this, Const.ACCESS_TOKEN, loginData.getAccess_token());

        PreferencesUtils.putString(this, Const.MOBILE_PHONE, username);

        PreferencesUtils.putInt(this, Const.USER_ID, loginData.getUser_id());
    }


    private void chooseImage(final ImageView imageView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.mipmap.foot_press);
            }
        }, 300);

        imageView.setImageResource(R.mipmap.foot_normal);

    }

    //检查用户的输入信息
    private void checkData() {
        username = mEtUserName.getText().toString().trim();
        password = mEtPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            showToast("用户名或者密码不能为空");
        } else {
            //登陆服务器
            login();
        }
    }

    //获取融云的token
    private void getRongToken(LoginEntity loginData) {
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("mobilephone", username);
        params.put("access_token", loginData.getAccess_token());
        params.put("app_key", "yunmiao");
        params.put("user_id", "" + loginData.getUser_id());
        params.put("user_type", 1);

        Call<ResultEntity> call = api.getIMToken(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 登录成功
                    // RToken
                    RcEntity rcEntity = JSON.parseObject(result.getData().toString(), RcEntity.class);
                    if (rcEntity != null) {
                        RToken = rcEntity.getRc_token();
                        PreferencesUtils.putString(LoginActivity.this, Const.RC_TOKEN, RToken);
                        //连接融云服务器
                        connect(RToken);
                    }


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

    /**
     * 登陆融云
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link @init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {

        Log.i(TAG, "connect: 开始连接融云");
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查
                 * 1.Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.i("hello", "onSuccess: " + "融云token错误");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.i("hello", "onSuccess: " + "融云链接成功" + userid);
                    loading(false);
                    skipPageWithAnim(MainActivity.class);

                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("hello", "onError: " + "融云链接失败");
                }
            });
        }
    }

    /**
     * 获取权限
     */
    private void permissions() {
        HiPermission.create(mContext)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        //Log.i(TAG, "onClose");
                        showToast("您已取消同意权限");
                    }

                    @Override
                    public void onFinish() {
                        //showToast("All permissions requested completed");
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        //Log.i(TAG, "onDeny");
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                        //Log.i(TAG, "onGuarantee");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEtUserName.setText("");
        mEtPassWord.setText("");
    }
}
