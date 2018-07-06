package com.pet.activity;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pet.R;
import com.pet.utils.FitStateUI;
import com.pet.view.DialogGetPicture;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完善用户信息界面
 */
public class CompileUserDataActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CAMERA = 1;// 拍照
    public static final int PHOTO_GALLERY = 2; // 相册
    public static final String IMAGE_UNSPECIFIED = "image/*";//随意图片类型

    @BindView(R.id.ll_girl)
    LinearLayout llGirl;
    @BindView(R.id.iv_girl)
    ImageView ivGirl;
    @BindView(R.id.ll_boy)
    LinearLayout llBoy;
    @BindView(R.id.iv_boy)
    ImageView ivBoy;

    @BindView(R.id.ll_have_pet)
    LinearLayout llHavePet;
    @BindView(R.id.iv_have_pet)
    ImageView ivHavePet;
    @BindView(R.id.ll_no_pet)
    LinearLayout llNoPet;
    @BindView(R.id.iv_no_pet)
    ImageView ivNoPet;

    @BindView(R.id.ll_dog)
    LinearLayout llDog;
    @BindView(R.id.iv_dog)
    ImageView ivDog;
    @BindView(R.id.ll_cat)
    LinearLayout llCat;
    @BindView(R.id.iv_cat)
    ImageView ivCat;
    @BindView(R.id.ll_other)
    LinearLayout llOther;
    @BindView(R.id.iv_other)
    ImageView ivOther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.changeStatusBarTextColor(this, true);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_compile_user_data);

        ButterKnife.bind(this);
        initListener();
    }

    /**
     * 初始化互斥按钮的监听
     */
    private void initListener() {
        llBoy.setOnClickListener(sexListener);
        llGirl.setOnClickListener(sexListener);

        llHavePet.setOnClickListener(identiftyListener);
        llNoPet.setOnClickListener(identiftyListener);

        llDog.setOnClickListener(animalListener);
        llCat.setOnClickListener(animalListener);
        llOther.setOnClickListener(animalListener);
    }

    //选择性别的监听
    private View.OnClickListener sexListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ll_boy){
                ivBoy.setImageResource(R.drawable.circle_press);
            }else {
                ivBoy.setImageResource(R.drawable.circle);
            }

            if (view.getId() == R.id.ll_girl){
                ivGirl.setImageResource(R.drawable.circle_press);
            }else {
                ivGirl.setImageResource(R.drawable.circle);
            }
        }
    };


    //选择身份的监听
    private View.OnClickListener identiftyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ll_have_pet){
                ivHavePet.setImageResource(R.drawable.circle_press);
            }else {
                ivHavePet.setImageResource(R.drawable.circle);
            }

            if (view.getId() == R.id.ll_no_pet){
                ivNoPet.setImageResource(R.drawable.circle_press);
            }else {
                ivNoPet.setImageResource(R.drawable.circle);
            }
        }
    };

    //选择喜欢宠物的监听
    private View.OnClickListener animalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ll_dog){
                ivDog.setImageResource(R.drawable.circle_press);
            }else {
                ivDog.setImageResource(R.drawable.circle);
            }

            if (view.getId() == R.id.ll_cat){
                ivCat.setImageResource(R.drawable.circle_press);
            }else {
                ivCat.setImageResource(R.drawable.circle);
            }
            if (view.getId() == R.id.ll_other){
                ivOther.setImageResource(R.drawable.circle_press);
            }else {
                ivOther.setImageResource(R.drawable.circle);
            }
        }
    };




    @OnClick({R.id.circle_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circle_head:
                new DialogGetPicture(this) {
                    @Override
                    public void amble() {
                        selectPictureFromAlbum(CompileUserDataActivity.this);
                    }

                    @Override
                    public void photo() {
                        photograph(CompileUserDataActivity.this);

                    }
                }.show();
                break;

            default:

                break;
        }
    }

    /**
     * 拍照
     *
     * @param activity
     */
    public void photograph(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 从系统相冊中选取照片上传
     *
     * @param activity
     */
    public static void selectPictureFromAlbum(Activity activity) {
        // 调用系统的相冊
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        activity.startActivityForResult(intent, PHOTO_GALLERY);
    }
}
