package com.pet.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.CompileUserDataActivity;
import com.pet.adapter.MyFragmentPagerAdapter;
import com.pet.activity.SetActivity;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.ResultEntity;
import com.pet.bean.UserInfoEntity;
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
 * Created by dragon on 2018/6/11.
 * 我的fragment
 */

public class MyFragment extends Fragment {
    private View mRoot;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.iv_my_set)
    ImageView mIvMySet;

    @BindView(R.id.iv_pet_bone)
    ImageView mIvPetBone;

    @BindView(R.id.cv_my_head)
    CircleImageView mCvHead;
    @BindView(R.id.tv_my_nickName)
    TextView mTvNickName;
    @BindView(R.id.tv_my_sign)
    TextView mTvSign;
    @BindView(R.id.tv_fans_number)
    TextView mTvFansNumber;
    @BindView(R.id.tv_attention_number)
    TextView mTvAttention;
    @BindView(R.id.iv_my_bg)
    ImageView mIvMyBg;

    //动态fragment
    private DynamicFragment mSeminarFragment;
    //关注fragment
    private AttentionFragment mAttentionFragment;
    //萌宠fragment
    private CutePetFragment mCutePetFragment;

    private String[] tableTitle = new String[]{"关注", "动态", "萌宠"};
    private List<Fragment> mFragmentTab;
    UserInfoEntity userInfoEntity;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_my, container,
                false);
        ButterKnife.bind(this, mRoot);
        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);

        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);
        initFragmentTab();
        initData();
        getData();
        return mRoot;
    }

    private void initFragmentTab() {
        mSeminarFragment = new DynamicFragment();
        mAttentionFragment = new AttentionFragment();
        mCutePetFragment = new CutePetFragment();

        mFragmentTab = new ArrayList<>();
        mFragmentTab.add(mSeminarFragment);
        mFragmentTab.add(mAttentionFragment);
        mFragmentTab.add(mCutePetFragment);
    }

    @OnClick({R.id.iv_my_set, R.id.iv_pet_bone, R.id.iv_editor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_set:
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;

            case R.id.iv_pet_bone:
                showPopWindow();
                break;

            case R.id.iv_editor:
                Intent intent = new Intent(getActivity(), CompileUserDataActivity.class);
                intent.putExtra("bg",userInfoEntity.getBackground_img());
                intent.putExtra("head",userInfoEntity.getUser_icon());
                intent.putExtra("name",userInfoEntity.getUser_name());
                intent.putExtra("gender",userInfoEntity.getUser_gender());
                startActivity(intent);
                break;

            default:

                break;
        }
    }

    //展示狗粮和粮票数量
    private void showPopWindow() {
        PopupWindow popupWindow = new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.layout_pop_item, null));
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        popupWindow.showAsDropDown(mIvPetBone);
    }

    private void initData() {

        initTabLayout();

    }

    private void initTabLayout() {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), tableTitle, mFragmentTab, 3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    //获取我的个人信息数据
    private void getData() {
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        Call<ResultEntity> call = api.GetUserinfo(params);
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
                     userInfoEntity = JSON.parseObject(result.getData().toString(), UserInfoEntity.class);
                    setUserData(userInfoEntity);
                } else
                    Toast.makeText(getActivity(), "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }
    //设置用户的信息
    @SuppressLint("SetTextI18n")
    private void setUserData(UserInfoEntity userInfoEntity) {
        if (userInfoEntity != null) {
            Glide.with(getActivity())
                    .load(Const.PIC_URL + userInfoEntity.getUser_icon())
                    .placeholder(R.mipmap.default_head)
                    .dontAnimate()
                    .error(R.mipmap.load_pic_fail)
                    .into(mCvHead);

            mTvNickName.setText(userInfoEntity.getUser_name());
            mTvSign.setText(userInfoEntity.getUser_desc());
            mTvFansNumber.setText("粉丝 " + userInfoEntity.getFans_count() + "人");
            mTvAttention.setText("关注 " + userInfoEntity.getFollow_count() + "人");

            Glide.with(getActivity())
                    .load(Const.PIC_URL + userInfoEntity.getBackground_img())
                    //.animate(R.anim.scale_in)
                    .placeholder(R.mipmap.take_photo_loading)
                    .error(R.mipmap.load_pic_fail)
                    .into(mIvMyBg);
        }
    }

}
