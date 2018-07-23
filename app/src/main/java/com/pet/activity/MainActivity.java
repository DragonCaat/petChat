package com.pet.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.LoginEntity;
import com.pet.bean.ResultEntity;
import com.pet.bean.RongImEntity;
import com.pet.bean.UserInfoEntity;
import com.pet.fragment.MapFragment;
import com.pet.fragment.MessageFragment;
import com.pet.fragment.MyFragment;
import com.pet.fragment.TakePhotoFragment;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.service.LocationService;
import com.pet.utils.FitStateUI;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import retrofit2.Call;
import retrofit2.Response;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

public class MainActivity extends BaseActivity {
    private Context mContext;
    private MapFragment mapFragment;
    private TakePhotoFragment takePhotoFragment;
    private MessageFragment messageFragment;
    private MyFragment myFragment;

    @BindView(R.id.ll_map)
    LinearLayout mLlMap;
    @BindView(R.id.iv_map)
    ImageView mIvMap;
    @BindView(R.id.tv_map)
    TextView mTvMap;

    @BindView(R.id.ll_take_photo)
    LinearLayout mLlTakePhoto;
    @BindView(R.id.iv_take_photo)
    ImageView mIvTakePhoto;
    @BindView(R.id.tv_take_photo)
    TextView mTvTakePhoto;

    @BindView(R.id.ll_message)
    LinearLayout mLlMessage;
    @BindView(R.id.iv_message)
    ImageView mIvMessage;
    @BindView(R.id.tv_message)
    TextView mTvMessage;

    @BindView(R.id.ll_my)
    LinearLayout mLlMy;
    @BindView(R.id.iv_my)
    ImageView mIvMy;
    @BindView(R.id.tv_my)
    TextView mTvMy;

    @BindView(R.id.photo_zone)
    ImageView mIvPhotoZone;

    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private static boolean isExit = false;
    //退出运用的标示
    private static int EXIT_APPLICATION = 1;

    private String registration_id = "";

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                default:
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.changeStatusBarTextColor(this, true);
        setStatusBarColor(R.color.main_color_press);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        registration_id = PreferencesUtils.getString(this,Const.REGID);

        //Log.i("hello", "onCreate: "+registration_id);

        mContext = this;
        mIvMap.performClick();
        setHomeListener();

        mIvMap.performClick();
        showFragment(mapFragment, MapFragment.class);

        init();

        String rcToken = PreferencesUtils.getString(this, Const.RC_TOKEN);
        connect(rcToken);
        //开启定位服务
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);

        //上传极光的registerId
        get_RegistrationId();

    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
    }


    @OnClick({R.id.photo_zone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo_zone:
                Animation rotate = AnimationUtils.loadAnimation(this,
                        R.anim.down_img_anim);
                rotate.setFillAfter(true);
                mIvPhotoZone.startAnimation(rotate);
                showDialog();
                break;

            default:

                break;
        }
    }

    //设置监听
    private void setHomeListener() {
        mLlMap.setOnClickListener(listener);
        mLlTakePhoto.setOnClickListener(listener);
        mLlMessage.setOnClickListener(listener);
        mLlMy.setOnClickListener(listener);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //点击地图按钮
            if (view.getId() == R.id.ll_map) {
                showFragment(mapFragment, MapFragment.class);
                mIvMap.setImageResource(R.mipmap.home_location_press);
                mTvMap.setTextColor(getResources().getColor(R.color.black));
                setStatusBarColor(R.color.main_color_press);
                mRlMain.setFitsSystemWindows(false);
            } else {
                mIvMap.setImageResource(R.mipmap.home_location_normal);
                mTvMap.setTextColor(getResources().getColor(R.color.gray));
            }
            //随手拍按钮
            if (view.getId() == R.id.ll_take_photo) {
                showFragment(takePhotoFragment, TakePhotoFragment.class);
                mIvTakePhoto.setImageResource(R.mipmap.take_photo_press);
                mTvTakePhoto.setTextColor(getResources().getColor(R.color.black));
                setStatusBarColor(R.color.main_color_press);
                mRlMain.setFitsSystemWindows(true);
            } else {
                mIvTakePhoto.setImageResource(R.mipmap.take_photo_normal);
                mTvTakePhoto.setTextColor(getResources().getColor(R.color.gray));
            }

            //点击消息按钮
            if (view.getId() == R.id.ll_message) {
                showFragment(messageFragment, MessageFragment.class);
                mIvMessage.setImageResource(R.mipmap.message_press);
                mTvMessage.setTextColor(getResources().getColor(R.color.black));
                setStatusBarColor(R.color.main_color_press);
                mRlMain.setFitsSystemWindows(true);
            } else {
                mIvMessage.setImageResource(R.mipmap.message_normal);
                mTvMessage.setTextColor(getResources().getColor(R.color.gray));
            }

            //点击我的按钮
            if (view.getId() == R.id.ll_my) {
                showFragment(myFragment, MyFragment.class);
                mIvMy.setImageResource(R.mipmap.my_press);
                mTvMy.setTextColor(getResources().getColor(R.color.black));
                setStatusBarColor(android.R.color.transparent);
                mRlMain.setFitsSystemWindows(false);
            } else {
                mIvMy.setImageResource(R.mipmap.my_normal);
                mTvMy.setTextColor(getResources().getColor(R.color.gray));
            }
        }
    };


    /**
     * 展示fragment*
     */
    private void showFragment(Fragment fragment, Class<?> cl) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        if (fragment == null) {
            if (cl == MapFragment.class) {
                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                    transaction.remove(mapFragment)
                            .add(R.id.main_contain, mapFragment).show(mapFragment);
                }
            } else if (cl == MessageFragment.class) {
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.remove(messageFragment)
                            .add(R.id.main_contain, messageFragment)
                            .show(messageFragment);
                }
            } else if (cl == MyFragment.class) {
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.remove(myFragment)
                            .add(R.id.main_contain, myFragment)
                            .show(myFragment);
                }
            } else if (cl == TakePhotoFragment.class) {
                if (takePhotoFragment == null) {
                    takePhotoFragment = new TakePhotoFragment();
                    transaction.remove(takePhotoFragment)
                            .add(R.id.main_contain, takePhotoFragment)
                            .show(takePhotoFragment);
                }
            }
        } else {
            if (cl == MapFragment.class)
                transaction.show(mapFragment);
            if (cl == MessageFragment.class)
                transaction.show(messageFragment);
            if (cl == MyFragment.class)
                transaction.show(myFragment);
            if (cl == TakePhotoFragment.class)
                transaction.show(takePhotoFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
        if (takePhotoFragment != null)
            transaction.hide(takePhotoFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapFragment = null;
        takePhotoFragment = null;
        messageFragment = null;
        myFragment = null;

        isExit = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 连续点击两次返回退出
     */
    protected void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(EXIT_APPLICATION, 2000);
        } else {
            finish();
        }
    }

    //底部的弹出框
    private void showDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.dialogStyle);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.photo_dialog, null);

        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button btnTakePhoto = dialogView.findViewById(R.id.btn_take_photo);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipPageWithAnim(PublishActivity.class);
                dialog.dismiss();
            }
        });

        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);

        window.setNavigationBarColor (getResources().getColor(R.color.transparent));

        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = 500;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        //外部可点击
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        // 对dialog进行监听
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Animation rotate = AnimationUtils.loadAnimation(mContext,
                        R.anim.up_img_anim);
                rotate.setFillAfter(true);
                mIvPhotoZone.startAnimation(rotate);
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
    private void connect(final String token) {

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
                    //Log.i("hello", "onSuccess: " + "融云链接成功" + userid);
                    initRongIm();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }


    private void initRongIm() {
        /**
         * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
         *
         * @param userInfoProvider 用户信息提供者。
         * @param isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
         *                         如果 App 提供的 UserInfoProvider
         *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
         *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
         * @see UserInfoProvider
         */
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {

                getData(userId);
                return null;//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }
        }, true);

    }

    //获取我的个人信息数据
    private void getData(final String userId) {
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        Call<ResultEntity> call = api.get_user_info(params);
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
                    RongImEntity rongImEntity = JSON.parseObject(result.getData().toString(), RongImEntity.class);
                    UserInfo userInfo = new UserInfo(userId, rongImEntity.getUser_name(), Uri.parse(Const.PIC_URL + rongImEntity.getSmall_user_icon()));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);

                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }


    //上传极光的registerId
    private void get_RegistrationId() {
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("registration_id", registration_id);
        Call<ResultEntity> call = api.get_RegistrationId(params);
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

                    //Log.i("hello", "onResponse: 极光上传成功");
                } else {
                    //Log.i("hello", "onResponse: 极光上传失败");

                }
            }

            @Override
            public void onFailure(Call<ResultEntity> arg0, Throwable arg1) {

            }
        });

    }

}
