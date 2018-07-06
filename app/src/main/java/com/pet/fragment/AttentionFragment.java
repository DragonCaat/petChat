package com.pet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.AttentionAdapter;
import com.pet.adapter.DynamicAdapter;
import com.pet.bean.AttentionEntity;
import com.pet.bean.Const;
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
import me.shihao.library.XRecyclerView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/6/20.
 * 动态的fragment
 */

public class AttentionFragment extends Fragment {

    private Context mContext;
//    @BindView(R.id.iv_no_message)
//    ImageView ivNoMessage;

    @BindView(R.id.recyclerView)
    XRecyclerView xRecyclerView;

    @BindView(R.id.iv_no_attention)
    ImageView mIvNoAttention;

    private AttentionAdapter adapter;

    private int pageno = 0;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";
    private boolean isFirst = true;

    private boolean noData = false;
    //动态 实体的list
    private List<AttentionEntity> list;
    private List<AttentionEntity> dataSet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fouces, null);
        mContext = getActivity();
        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);
        ButterKnife.bind(this, view);

        initData();

        getData();

        return view;
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


    //获取动态数据
    private void getData() {
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("page", pageno);
        params.put("num", 6);
        Call<ResultEntity> call = api.ShowUserDynamic(params);
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
                        list = JSON.parseArray(result.getData().toString(), AttentionEntity.class);
                        if (list!=null && list.size()>0){
                            adapter = new AttentionAdapter(list, mContext);
                            mIvNoAttention.setVisibility(View.INVISIBLE);
                        }else {
                            mIvNoAttention.setVisibility(View.VISIBLE);
                        }
                        xRecyclerView.refreshComlete();
                    } else {
                        dataSet = JSON.parseArray(result.getData().toString(), AttentionEntity.class);
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
