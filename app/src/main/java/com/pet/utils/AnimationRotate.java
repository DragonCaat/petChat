package com.pet.utils;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

//旋转图片
public class AnimationRotate{
	
	/*private int progress = 0;
	private RoundProgressBar mRoundProgressBar;	*/
	@SuppressWarnings("unused")
	private ImageView imageView;
	public AnimationRotate() {
		super();
	}

	
	/*public AnimationRotate(RoundProgressBar mRoundProgressBar) {
		this.mRoundProgressBar = mRoundProgressBar;
	}*/
	
	public static void rotatebolowImage(ImageView imageView){
		
		RotateAnimation ra = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(600);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		LinearInterpolator lir = new LinearInterpolator(); 
		ra.setInterpolator(lir);	
		imageView.setAnimation(ra);
	}
	
public static void stopRotatebolowImage(ImageView imageView){
		
		RotateAnimation ra = new RotateAnimation(0, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(0);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		LinearInterpolator lir = new LinearInterpolator(); 
		ra.setInterpolator(lir);	
		imageView.setAnimation(ra);
	}
	
	/*
	public void rotate(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(progress <= 100){
					progress += 3;	
					
					if(progress == 100){
						progress = 0;
					}
					System.out.println(progress);					
					mRoundProgressBar.setProgress(progress);
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}*/
}
