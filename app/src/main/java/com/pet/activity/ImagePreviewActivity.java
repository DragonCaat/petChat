package com.pet.activity;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.pet.R;
import com.pet.adapter.ImagePreviewAdapter;
import com.pet.ninelayout.P;
import com.pet.view.CustomViewPager;

import java.util.List;
import java.util.Map;

/**
 * 图片预览 Activity
 */
public class ImagePreviewActivity extends AppCompatActivity {

    private int itemPosition;
    private List<String> imageList;
    private CustomViewPager viewPager;
    private LinearLayout main_linear;
    private boolean      mIsReturning;
    private int            mStartPosition;
    private int            mCurrentPosition;
    private ImagePreviewAdapter adapter;

    private int flag;

    private static final int HOTFLAG = 1;
    private static final int NEWFLAG = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        setContentView(R.layout.activity_image_preview);
        initShareElement();
        getIntentData();
        initView();
        renderView();
        getData();
        setListener();
    }

    private void initShareElement() {
        setEnterSharedElementCallback(mCallback);
    }
    private void setListener() {
        main_linear.getChildAt(mCurrentPosition).setEnabled(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideAllIndicator(position);
                main_linear.getChildAt(position).setEnabled(true);
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition/2  + 0.5f);
                page.setScaleY(normalizedposition/2  + 0.5f);
            }
        });
    }
    private void  hideAllIndicator(int position){
        for(int i=0;i<imageList.size();i++){
            if(i!=position){
                main_linear.getChildAt(i).setEnabled(false);
            }
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.imageBrowseViewPager);
        main_linear = findViewById(R.id.main_linear);
    }

    private void renderView() {
        if(imageList==null) return;
        if(imageList.size()==1){
            main_linear.setVisibility(View.GONE);
        }else {
            main_linear.setVisibility(View.VISIBLE);
        }
        adapter = new ImagePreviewAdapter(this,imageList,itemPosition);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mCurrentPosition);
    }

    private void getIntentData() {
        if(getIntent()!=null){
            mStartPosition = getIntent().getIntExtra(P.START_IAMGE_POSITION, 0);
            mCurrentPosition = mStartPosition;
            itemPosition = getIntent().getIntExtra(P.START_ITEM_POSITION, 0);
            imageList = getIntent().getStringArrayListExtra("imageList");
            flag = getIntent().getIntExtra("flag",0);
        }
    }

    /**
     * 获取数据
     */
    private void getData() {

        View view;
        for (String pic : imageList) {
            //创建底部指示器(小圆点)
            view = new View(ImagePreviewActivity.this);
            view.setBackgroundResource(R.drawable.indicator);
            view.setEnabled(false);
            //设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            //设置间隔
            if (!pic.equals(imageList.get(0))) {
                layoutParams.leftMargin = 20;
            }
            //添加到LinearLayout
            main_linear.addView(view, layoutParams);
        }
    }


    @Override
    public void finishAfterTransition() {
        mIsReturning = true;
        Intent data = new Intent();
        data.putExtra(P.START_IAMGE_POSITION, mStartPosition);
        data.putExtra(P.CURRENT_IAMGE_POSITION, mCurrentPosition);
        data.putExtra(P.CURRENT_ITEM_POSITION, itemPosition);

        if (flag == HOTFLAG)
            data.putExtra("flag",1);
        else
            data.putExtra("flag",0);

        setResult(RESULT_OK, data);

        //setResult("flag",);
        super.finishAfterTransition();
    }


    private final SharedElementCallback mCallback = new SharedElementCallback() {

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = adapter.getPhotoView();
                if (sharedElement == null) {
                    names.clear();
                    sharedElements.clear();
                } else if (mStartPosition != mCurrentPosition) {
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };
}
