package com.pet.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.NearPeopleAdapter;
import com.pet.bean.Const;
import com.pet.bean.CutePetEntity;
import com.pet.bean.NearByEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shihao.library.XRecyclerView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 附近的人界面
 */
public class NearPeopleActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    XRecyclerView xRecyclerView;

    private Context mContext;

    private NearPeopleAdapter adapter;
    private List<NearByEntity> list;
    private List<NearByEntity> dataSet;

    private boolean isFirst = true;
    private boolean noData = false;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private int pageno = 0;

    //纬度
    private double latitude;
    //经度
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("lat", 0);
        longitude = intent.getDoubleExtra("long", 0);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        setContentView(R.layout.activity_near_people);
        ButterKnife.bind(this);
        mContext = this;
        initData();

        getData();
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

    private void initData() {
        xRecyclerView.setOnRefreshListener(new XRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                if (list == null)
                    getData();
                else {
                    list = null;
                    pageno = 0;
                    isFirst = true;
                    getData();
                }

            }

            @Override
            public void onLoadMore() {
                //加载更多
                if (!noData) {
                    pageno++;
                    getData();
                } else {
                    xRecyclerView.refreshComlete();
                    //Toast.makeText(mContext,"说了没数据",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //获取附近的人信息
    private void getData() {
        //xRecyclerView.setRefreshing(true);
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("distance", 50);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("pageno", pageno);
        Call<ResultEntity> call = api.nearbyUsers(params);
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
                    //xRecyclerView.setRefreshing(false);
                    if (isFirst) {
                        isFirst = false;
                        list = JSON.parseArray(result.getData().toString(), NearByEntity.class);
                        adapter = new NearPeopleAdapter(mContext, list);
                        xRecyclerView.refreshComlete();
                    } else {
                        dataSet = JSON.parseArray(result.getData().toString(), NearByEntity.class);
                        if (dataSet != null && dataSet.size() > 0) {
                            list.addAll(dataSet);

                            adapter.notifyDataSetChanged();
                            xRecyclerView.refreshComlete();
                        } else {
                            xRecyclerView.refreshComlete();
                            noData = true;
                            Toast.makeText(mContext, "没数据了", Toast.LENGTH_SHORT).show();
                        }
                    }
                    xRecyclerView.verticalLayoutManager().setAdapter(adapter);


                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

}
