package com.pet.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pet.R;
import com.pet.bean.Const;

/**
 * Created by dragon on 2018/7/10.
 * 显示放大图片的dialog
 */

public class MyImageDialog extends Dialog {

    private Window window = null;
    private ImageView iv;
    //private Bitmap bms;
    private Context mContext;
    private String url;

    public MyImageDialog(Context context, boolean cancelable,
                         DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public MyImageDialog(Context context, int cancelable,int x,int y,String url) {
        super(context, cancelable);
        windowDeploy(x, y);
        this.mContext = context;
        this.url = url;
        //bms = bm;


    }
    public MyImageDialog(Context context,String url) {
        super(context);
        this.mContext = context;
        this.url = url;
    }

    protected void onCreate(Bundle savedInstanceState) {
        //初始化布局
        View loadingview= LayoutInflater.from(getContext()).inflate(R.layout.imagedialogview,null);
        iv= loadingview.findViewById(R.id.imageview_head_big);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final ProgressBar progressBar =loadingview.findViewById(R.id.pb);

        Glide.with(mContext).load(Const.PIC_URL+url).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                progressBar.setVisibility(View.INVISIBLE);
                iv.setImageDrawable(resource);
            }
        });
        //设置dialog的布局
        setContentView(loadingview);
        //如果需要放大或者缩小时的动画，可以直接在此出对loadingview或iv操作，在下面SHOW或者dismiss中操作
        super.onCreate(savedInstanceState);
    }

    //设置窗口显示
    @SuppressLint("ResourceType")
    public void windowDeploy(int x, int y){
        window = getWindow(); //得到对话框
        assert window != null;
        window.setWindowAnimations(R.style.picLook); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.black); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移
//            wl.alpha = 0.6f; //设置透明度
//            wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);
    }

    public void show() {
        //设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(true);
        super.show();
    }
    public void dismiss() {
        super.dismiss();
    }

}
