package com.pet.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.pet.R;
import com.pet.utils.FitStateUI;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dragon on 2017/11/26.
 * 欢迎界面
 */

public class WelcomeActivity extends AppCompatActivity {

    private String firstGuide = "";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    WelcomeActivity.this.finish();
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
        FitStateUI.setStatusBarColor(R.color.transparent,this);
//      this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//       this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//               WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        //firstGuide = PreferencesUtils.getString(this,Const.FIRST_GUIDE);
        //标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_welcome);

        handler.sendEmptyMessageDelayed(1, 1000);
    }

    public void getHome() {
        if (TextUtils.isEmpty(firstGuide)) {
//            Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
//            startActivity(intent);
        } else {

        }
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }


}
