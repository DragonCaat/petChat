package com.pet.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.pet.R;
import com.pet.bean.Const;
import com.pet.bean.LoginEntity;
import com.pet.service.LocationService;
import com.pet.utils.FitStateUI;
import com.pet.utils.PreferencesUtils;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * Created by dragon on 2017/11/26.
 * 欢迎界面
 */

public class WelcomeActivity extends AppCompatActivity {

    private String firstGuide = "";

    private String phone = "";

    private String rcToken ="";

    private boolean isLogin;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getHome();
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.changeStatusBarTextColor(this, true);
        FitStateUI.setStatusBarColor(R.color.transparent, this);

        rcToken = PreferencesUtils.getString(this,Const.RC_TOKEN);

        firstGuide = PreferencesUtils.getString(this, Const.FIRST_GUIDE);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);
        if (TextUtils.isEmpty(phone))
            isLogin = false;
        else
            isLogin = true;
        setContentView(R.layout.activity_welcome);


        handler.sendEmptyMessageDelayed(1, 1000);
    }

    //判断并选择跳转的页面
    public void getHome() {
        if (TextUtils.isEmpty(firstGuide)) {
            Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
            startActivity(intent);
        } else {
            //判断是否已经登陆
            if (isLogin) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

}
