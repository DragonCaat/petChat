package com.pet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.activity.TakePhotoDetailActivity;
import com.pet.adapter.TakePhotoAdapter;
import com.pet.bean.Const;
import com.pet.bean.RangeEntity;
import com.pet.bean.ResultEntity;
import com.pet.bean.TakePhotoEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;
import com.pet.view.DiscolourScrollView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/6/11.
 * 随手拍的fragment
 */

public class TakePhotoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View mRoot;

    @BindView(R.id.rv_take_photo)
    RecyclerView mRvTakePhoto;

    @BindView(R.id.swipe_fresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int user_id = 0;

    private String acces_token = "";

    private String phone = "";

    private List<TakePhotoEntity> takePhotoEntities;
    private TakePhotoAdapter takePhotoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_take_photo, container,
                false);
        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);

        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);
        ButterKnife.bind(this, mRoot);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getTakePhotoData();

        return mRoot;
    }

    /**
     * 跳转界面
     */
    public void skipPage(Class<? extends Activity> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    /**
     * 获取随手拍数据
     */
    private void getTakePhotoData() {
        mSwipeRefreshLayout.setRefreshing(true);
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        Call<ResultEntity> call = api.GetSnapshot(params);
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
                    takePhotoEntities = JSON.parseArray(result.getData().toString(), TakePhotoEntity.class);
                    if (takePhotoEntities != null && takePhotoEntities.size() > 0) {
                        setData(takePhotoEntities);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }else
                    Toast.makeText(getActivity(),""+result.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    //绑定数据
    private void setData(List<TakePhotoEntity> takePhotoEntities) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRvTakePhoto.setLayoutManager(gridLayoutManager);
        takePhotoAdapter = new TakePhotoAdapter(getActivity(), takePhotoEntities);
        mRvTakePhoto.setAdapter(takePhotoAdapter);
    }

    @Override
    public void onRefresh() {
        if (takePhotoEntities == null)
            getTakePhotoData();
        else
            mSwipeRefreshLayout.setRefreshing(false);

    }
}
