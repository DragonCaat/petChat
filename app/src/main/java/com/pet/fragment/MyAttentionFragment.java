package com.pet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.AttentionAdapter;
import com.pet.adapter.MyAttentionAdapter;
import com.pet.baseadapter.ILoadCallback;
import com.pet.bean.AttentionEntity;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shihao.library.XRecyclerView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/7/18.
 * 我关注的动态页面
 */

public class MyAttentionFragment extends Fragment {

    @BindView(R.id.recyclerView)
    XRecyclerView xRecyclerView;

    @BindView(R.id.iv_no_attention)
    ImageView mIvNoAttention;

    private MyAttentionAdapter adapter;

    private int pageno = 0;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";
    private boolean isFirst = true;

    private boolean noData = false;
    //动态 实体的list
    private List<DynamicEntity> list;
    private List<DynamicEntity> dataSet;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_attention, null);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);

        pageno = 0;
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
                } else
                    xRecyclerView.refreshComlete();

            }
        });
    }

    //获取关注的动态
    private void getData() {
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        final Map<String, Object> params = new HashMap<>();
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
                        if (list != null && list.size() > 0) {
                            adapter = new MyAttentionAdapter(list, getActivity());
                            adapter.notifyDataSetChanged();
                            mIvNoAttention.setVisibility(View.GONE);
                        } else {

                            //Log.i("hello", "哈哈onResponse: ");

                            mIvNoAttention.setVisibility(View.VISIBLE);
                        }
                        xRecyclerView.refreshComlete();
                    } else {
                        dataSet = JSON.parseArray(result.getData().toString(), DynamicEntity.class);
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

                } else {
                    Toast.makeText(mContext, "没数据了", Toast.LENGTH_SHORT).show();
                    noData = true;
                    xRecyclerView.refreshComlete();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }
}
