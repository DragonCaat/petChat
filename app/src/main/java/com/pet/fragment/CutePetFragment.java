package com.pet.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.activity.AddPetActivity;
import com.pet.adapter.AttentionAdapter;
import com.pet.adapter.CutePetAdapter;
import com.pet.bean.AttentionEntity;
import com.pet.bean.Const;
import com.pet.bean.CutePetEntity;
import com.pet.bean.DynamicEntity;
import com.pet.bean.ResultEntity;
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
import me.shihao.library.XRecyclerView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/6/27.
 * 萌宠的fragment
 */

public class CutePetFragment extends Fragment {

    private View mRoot;
    @BindView(R.id.rv_cute_pet)
    XRecyclerView xRecyclerView;

    @BindView(R.id.ll_no_pet)
    LinearLayout mLlNoPet;


    private List<CutePetEntity> list;
    private CutePetAdapter adapter;
    //宠物
    private List<CutePetEntity> dataSet;
    private boolean isFirst = true;

    private boolean noData = false;

    private int pageno = 0;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_cute_pet, container,
                false);
        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);
        ButterKnife.bind(this, mRoot);
        mContext = getActivity();
        initData();
        getData();
        return mRoot;
    }

    @OnClick(R.id.btn_add_pet)
    public void onClick(){
        //添加宠物
        Intent intent = new Intent(mContext, AddPetActivity.class);
        startActivity(intent);
    }



    private void initData() {
        xRecyclerView.setOnRefreshListener(new XRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                if (list == null)
                    getData();
                else
                    xRecyclerView.refreshComlete();
            }

            @Override
            public void onLoadMore() {
                //加载更多
                if (!noData) {
                    pageno++;
                    getData();
                }

            }
        });
    }

    //获取个人宠物信息
    private void getData() {
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pageno", pageno);
        Call<ResultEntity> call = api.ShowUserPetinfo(params);
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
                    if (isFirst) {
                        isFirst = false;
                        list = JSON.parseArray(result.getData().toString(), CutePetEntity.class);
                        if (list != null && list.size() > 0) {
                            adapter = new CutePetAdapter( mContext,list);
                            mLlNoPet.setVisibility(View.GONE);
                        } else {
                            mLlNoPet.setVisibility(View.VISIBLE);
                        }
                        xRecyclerView.refreshComlete();
                    } else {
                        dataSet = JSON.parseArray(result.getData().toString(), CutePetEntity.class);
                        if (dataSet != null && dataSet.size() > 0) {
                            list.addAll(dataSet);
                            adapter.notifyDataSetChanged();
                            xRecyclerView.refreshComlete();
                        } else {
                            Toast.makeText(mContext, "没数据了", Toast.LENGTH_SHORT).show();
                            xRecyclerView.refreshComlete();
                            noData = true;
                        }
                    }
                    xRecyclerView.verticalLayoutManager().setAdapter(adapter);

                } else
                    Toast.makeText(getActivity(), "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

}
