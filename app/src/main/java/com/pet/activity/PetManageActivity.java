package com.pet.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.NearPeopleAdapter;
import com.pet.adapter.PetManageAdapter;
import com.pet.bean.Const;
import com.pet.bean.CutePetEntity;
import com.pet.bean.NearByEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shihao.library.XRecyclerView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 宠物管理界面
 */
public class PetManageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView xRecyclerView;
    @BindView(R.id.swipe_fresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private Context mContext;

    private PetManageAdapter adapter;
    private List<CutePetEntity> list;
    private List<CutePetEntity> dataSet;

    private boolean isFirst = true;
    private boolean noData = false;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private int pageno = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_manage);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);
        ButterKnife.bind(this);
        mContext = this;
        initData();

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void initData() {

    }

    //获取个人宠物信息
    private void getData() {
        swipeRefreshLayout.setRefreshing(true);
        final ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pageno", pageno);
        Call<ResultEntity> call = api.PetManage(params);
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
                    list = JSON.parseArray(result.getData().toString(), CutePetEntity.class);
                    adapter = new PetManageAdapter(mContext, list);
                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    xRecyclerView.setLayoutManager(manager);
                    xRecyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.iv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (list == null)
            getData();
        else {
            list.clear();
            getData();
        }

    }
}
