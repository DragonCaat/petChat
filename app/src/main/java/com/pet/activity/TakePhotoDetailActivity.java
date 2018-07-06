package com.pet.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.adapter.MyFragmentPagerAdapter;
import com.pet.fragment.AttentionFragment;
import com.pet.fragment.DynamicFragment;
import com.pet.fragment.HotTakePhotoFragment;
import com.pet.fragment.NewTakePhotoFragment;
import com.pet.utils.FitStateUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 随手拍详情界面
 */
public class TakePhotoDetailActivity extends AppCompatActivity {


    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.collapsingLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_image)
    ImageView mIvBg;
    @BindView(R.id.tv_des)
    TextView mTvDes;

    //热门fragment
    private HotTakePhotoFragment hotTakePhotoFragment;
    //关注fragment
    private NewTakePhotoFragment newTakePhotoFragment;
    private String[] tableTitle = new String[]{"热门", "最新"};
    private List<Fragment> mFragmentTab;


    private String title = "";
    private int id = 0;
    private String bigImageBg = "";
    private String content = "";

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.changeStatusBarTextColor(this, true);
        setContentView(R.layout.activity_take_photo_detail);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        id = intent.getIntExtra("id", 0);
        bigImageBg = intent.getStringExtra("big_bg");
        content = intent.getStringExtra("des");

        mContext = this;
        ButterKnife.bind(this);
        initFragmentTab();
        initData();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initFragmentTab() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        hotTakePhotoFragment = new HotTakePhotoFragment();
        hotTakePhotoFragment.setArguments(bundle);

        newTakePhotoFragment = new NewTakePhotoFragment();
        bundle.putInt("id", id);
        newTakePhotoFragment.setArguments(bundle);

        mFragmentTab = new ArrayList<>();
        mFragmentTab.add(hotTakePhotoFragment);
        mFragmentTab.add(newTakePhotoFragment);
    }

    private void initData() {
        initTabLayout();
        mTvTitle.setText(title);
        mTvDes.setText(content);
        Glide.with(this)
                .load(bigImageBg)
                .placeholder(R.mipmap.take_photo_loading)
                .into(mIvBg);
    }

    @OnClick({R.id.iv_back, R.id.join_topic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //加入话题按钮
            case R.id.join_topic:
                Intent intent = new Intent(mContext, PublishActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    private void initTabLayout() {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), tableTitle, mFragmentTab, 2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.BLACK, Color.RED);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
