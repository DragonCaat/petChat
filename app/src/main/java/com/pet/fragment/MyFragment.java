package com.pet.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.pet.R;
import com.pet.activity.CompileUserDataActivity;
import com.pet.activity.RefuseCallBackActivity;
import com.pet.adapter.MyFragmentPagerAdapter;
import com.pet.activity.SetActivity;
import com.pet.bean.AppointPetEntity;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.PetFoodTicketEntity;
import com.pet.bean.ResultEntity;
import com.pet.bean.UserInfoEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;
import com.pet.view.MyImageDialog;

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
    private MyAttentionFragment mSeminarFragment;
    //关注fragment
    private AttentionFragment mAttentionFragment;
    //萌宠fragment
    private CutePetFragment mCutePetFragment;

    private String[] tableTitle = new String[]{"关注", "动态", "萌宠"};
    private List<Fragment> mFragmentTab;
    private UserInfoEntity userInfoEntity;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";


    private Context mContext;

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
        mContext = getActivity();
        return mRoot;
    }

    private void initFragmentTab() {
        mSeminarFragment = new MyAttentionFragment();
        mAttentionFragment = new AttentionFragment(0);
        mCutePetFragment = new CutePetFragment(0);

        mFragmentTab = new ArrayList<>();
        mFragmentTab.add(mSeminarFragment);
        mFragmentTab.add(mAttentionFragment);
        mFragmentTab.add(mCutePetFragment);
    }

    @OnClick({R.id.iv_my_set, R.id.iv_pet_bone, R.id.iv_editor, R.id.cv_my_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_set:
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;

            //展示宠粮和粮票数
            case R.id.iv_pet_bone:
                GetFoodandCoupon();
                break;

            case R.id.iv_editor:
                Intent intent = new Intent(getActivity(), CompileUserDataActivity.class);
                intent.putExtra("bg", userInfoEntity.getBackground_img());
                intent.putExtra("head", userInfoEntity.getUser_icon());
                intent.putExtra("name", userInfoEntity.getUser_name());
                intent.putExtra("gender", userInfoEntity.getUser_gender());
                intent.putExtra("sign", userInfoEntity.getUser_desc());
                startActivity(intent);
                break;
            //点击头像
            case R.id.cv_my_head:
                MyImageDialog myImageDialog = new MyImageDialog(getActivity(), R.style.picLook, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, userInfoEntity.getUser_icon());
                myImageDialog.show();
                break;

            default:

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }


    //获取宠粮数量
    private void GetFoodandCoupon() {
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        Call<ResultEntity> call = api.GetFoodandCoupon(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {
                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 提交成功

                    PetFoodTicketEntity entity = JSON.parseObject(result.getData().toString(),PetFoodTicketEntity.class);
                    if (entity!=null)
                        showPopWindow(entity);
                } else {

                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }


    //展示狗粮和粮票数量
    @SuppressLint("SetTextI18n")
    private void showPopWindow(PetFoodTicketEntity entity) {
        final PopupWindow popupWindow = new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_pop_item, null);
        TextView tvFood = view.findViewById(R.id.tv_food);
        tvFood.setText("宠粮 x"+entity.getPet_food());

        TextView tvTicket = view.findViewById(R.id.tv_ticket);
        tvTicket.setText("粮票 x"+entity.getUsed_amount());

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePop();
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(mIvPetBone);
    }

    private void initData() {
        initTabLayout();

    }

    //显示粮票兑换
    @SuppressLint("SetTextI18n")
    private void showChangePop() {
        View popView = getActivity().getLayoutInflater().inflate(R.layout.layout_show_change_pop, null);
        final PopupWindow popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //确认兑换粮票按钮
        Button btnOk = popView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                exchangeCashCoupon();
            }
        });

        popupWindow.setAnimationStyle(R.style.picLook);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        popupWindow.setWidth(width * 2 / 5);
        popupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
    }

    //宠粮兑换粮票
    private void exchangeCashCoupon() {
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        Call<ResultEntity> call = api.exchangeCashCoupon(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {
                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 提交成功
                    Toast.makeText(mContext, "兑换成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
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
                    .dontAnimate()
                    .placeholder(R.mipmap.take_photo_loading)
                    .error(R.mipmap.load_pic_fail)
                    .into(mIvMyBg);
        }
    }
}
