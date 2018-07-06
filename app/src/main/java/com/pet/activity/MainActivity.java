package com.pet.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import com.pet.R;
import com.pet.fragment.MapFragment;
import com.pet.fragment.MessageFragment;
import com.pet.fragment.MyFragment;
import com.pet.fragment.TakePhotoFragment;
import com.pet.utils.FitStateUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private static boolean isExit = false;
    //退出运用的标示
    private static int EXIT_APPLICATION = 1;
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
        mContext = this;
        mIvMap.performClick();
        setHomeListener();

        mIvMap.performClick();
        showFragment(mapFragment, MapFragment.class);
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
            Log.i("hello", "exit: ");
        }
    }

    /**
     * 改变系统栏的文字颜色
     */
//    public void changeStatusBarTextColor(boolean isBlack) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (isBlack) {
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
//            }
//        }
//    }
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
}
