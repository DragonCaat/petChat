package com.pet.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.activity.ImagePagerActivity;
import com.pet.adapter.DynamicAdapter;
import com.pet.adapter.NearPeopleAdapter;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.NearByEntity;
import com.pet.bean.NineGridTestModel;
import com.pet.bean.ResultEntity;
import com.pet.bean.TakePhotoEntity;
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
 * Created by dragon on 2018/6/12.
 * 我的关注fragment
 */

public class DynamicFragment extends Fragment {

    @BindView(R.id.recyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.iv_no_attention)
    ImageView mIvNoAttention;

    private DynamicAdapter adapter;

    private Context mContext;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private List<DynamicEntity> list;
    private List<DynamicEntity> dataSet;
    private boolean isFirst = true;
    private boolean noData = false;
    private int pageno = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, null);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);
        initData();
        getData();
        return view;
    }

    @OnClick()
    public void onClick() {

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

                if (noData) {
                    xRecyclerView.refreshComlete();
                    Toast.makeText(mContext, "no message", Toast.LENGTH_SHORT).show();
                } else {
                    //加载更多
                    pageno++;
                    getData();
                }
            }
        });
    }


    //获取关注的动态
    private void getData() {
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pageno", pageno);
        Call<ResultEntity> call = api.GetDynamic(params);
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
                        list = JSON.parseArray(result.getData().toString(), DynamicEntity.class);
                        if (list!=null && list.size()>0){
                            adapter = new DynamicAdapter(mContext, list);
                            mIvNoAttention.setVisibility(View.INVISIBLE);
                        }else {
                            mIvNoAttention.setVisibility(View.VISIBLE);
                        }
                        xRecyclerView.refreshComlete();
                    } else {
                        dataSet = JSON.parseArray(result.getData().toString(), DynamicEntity.class);
                        if (dataSet != null && dataSet.size() > 0) {
                            adapter.appendData(dataSet);
                            adapter.notifyDataSetChanged();
                            xRecyclerView.refreshComlete();
                        } else {
                            noData = true;
                            xRecyclerView.refreshComlete();
                            Toast.makeText(mContext, "没数据了", Toast.LENGTH_SHORT).show();
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
