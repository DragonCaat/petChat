package com.pet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.adapter.MyFragmentPagerAdapter;
import com.pet.adapter.NearPeopleAdapter;
import com.pet.bean.AttentionEntity;
import com.pet.bean.Const;
import com.pet.bean.NearByEntity;
import com.pet.bean.OtherInfoEntity;
import com.pet.bean.ResultEntity;
import com.pet.bean.UserInfoEntity;
import com.pet.fragment.AttentionFragment;
import com.pet.fragment.CutePetFragment;
import com.pet.fragment.DynamicFragment;
import com.pet.fragment.HotTakePhotoFragment;
import com.pet.fragment.NewTakePhotoFragment;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 展示其他用户数据
 */
public class ShowUserActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.cv_user_head)
    CircleImageView mCvHead;
    @BindView(R.id.tv_my_nickName)
    TextView mTvNickName;
    @BindView(R.id.tv_my_sign)
    TextView mTvSign;
    @BindView(R.id.tv_fans_number)
    TextView mTvFansNumber;
    @BindView(R.id.tv_attention_number)
    TextView mTvAttention;
    @BindView(R.id.iv_bg)
    ImageView mIvMyBg;
    @BindView(R.id.iv_gender)
    ImageView mIvGender;
    @BindView(R.id.tv_follow_status)
    TextView mTvFollowStatus;

    //关注fragment
    private AttentionFragment mAttentionFragment;
    //萌宠fragment
    private CutePetFragment mCutePetFragment;

    private String[] tableTitle = new String[]{"动态", "萌宠"};
    private List<Fragment> mFragmentTab;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";
    private int othet_user_id = 0;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        othet_user_id = intent.getIntExtra("id", 0);
        setContentView(R.layout.activity_show_user);
        mContext = this;
        ButterKnife.bind(this);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);
        initTool();
        initFragmentTab();
        initData();

        getData();
    }

    private void initFragmentTab() {
        mAttentionFragment = new AttentionFragment();
        mCutePetFragment = new CutePetFragment();

        mFragmentTab = new ArrayList<>();

        mFragmentTab.add(mAttentionFragment);
        mFragmentTab.add(mCutePetFragment);
    }

    private void initData() {

        initTabLayout();

    }

    private void initTabLayout() {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), tableTitle, mFragmentTab, 2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.BLACK, Color.parseColor("#d3d3d3"));
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    //初始化折叠栏数据
    @SuppressLint("ResourceAsColor")
    private void initTool() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        collapsingToolbarLayout.setTitle("");
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    //获取个人数据信息
    private void getData() {
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("other_user_id", othet_user_id);
        Call<ResultEntity> call = api.showHomePage(params);
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
                    OtherInfoEntity otherInfoEntity = JSON.parseObject(result.getData().toString(), OtherInfoEntity.class);
                    if (otherInfoEntity != null)
                        setUserData(otherInfoEntity);

                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    //设置用户的信息follow_status
    @SuppressLint("SetTextI18n")
    private void setUserData(OtherInfoEntity otherInfoEntity) {
        if (otherInfoEntity != null) {
            Glide.with(mContext)
                    .load(Const.PIC_URL + otherInfoEntity.getUser_icon())
                    .placeholder(R.mipmap.default_head)
                    .dontAnimate()
                    .error(R.mipmap.load_pic_fail)
                    .into(mCvHead);
            mTvNickName.setText(otherInfoEntity.getUser_name());
            mTvSign.setText((CharSequence) otherInfoEntity.getUser_desc());
            mTvFansNumber.setText("粉丝 " + otherInfoEntity.getFans_count() + "人");
            mTvAttention.setText("关注 " + otherInfoEntity.getFollow_count() + "人");

            if (otherInfoEntity.getUser_gender() == 1)//男
                mIvGender.setImageResource(R.mipmap.boy);
            else
                mIvGender.setImageResource(R.mipmap.girl);

            if (otherInfoEntity.getFollow_status() == 0)
                mTvFollowStatus.setText("已关注");
            else
                mTvFollowStatus.setText("+ 关注");
            Glide.with(mContext)
                    .load(Const.PIC_URL + otherInfoEntity.getBackground_img())
                    //.animate(R.anim.scale_in)
                    .placeholder(R.mipmap.take_photo_loading)
                    .error(R.mipmap.load_pic_fail)
                    .into(mIvMyBg);
        }
    }
}
