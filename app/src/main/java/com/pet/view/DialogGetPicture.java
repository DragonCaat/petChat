package com.pet.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.pet.R;


public abstract class DialogGetPicture extends Dialog implements
		View.OnClickListener {

	private Activity activity;

	private FrameLayout mFGallery;
	private FrameLayout mFCamera;
	private Button mFCancle;

	public DialogGetPicture(Activity activity) {
		super(activity, R.style.MyDialogTheme);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_get_picture);

		setViewLocation();
		setCanceledOnTouchOutside(true);// 外部点击取消

		mFGallery = findViewById(R.id.flt_amble_upload);
		mFGallery.setOnClickListener(this);

		mFCamera = findViewById(R.id.flt_take_photo_upload);
		mFCamera.setOnClickListener(this);

		mFCancle = findViewById(R.id.btn_cancel);
		mFCancle.setOnClickListener(this);
	}

	/**
	 * 设置dialog位于屏幕底部
	 */
	private void setViewLocation() {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;

		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = 0;
		lp.y = height;
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		onWindowAttributesChanged(lp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.flt_amble_upload:
			amble();
			this.cancel();
			break;
		case R.id.flt_take_photo_upload:
			photo();
			this.cancel();
			break;
		case R.id.btn_cancel:
			this.cancel();
			break;
		}
	}

	public abstract void amble();

	public abstract void photo();

}
