package com.pet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.HotTakePhotoAdapter;
import com.pet.baseadapter.BaseAdapter;
import com.pet.baseadapter.ILoadCallback;
import com.pet.baseadapter.LoadMoreAdapterWrapper;
import com.pet.baseadapter.OnLoad;
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
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2018/6/28.
 * 最新随手拍界面
 */

public class NewTakePhotoFragment extends Fragment{
    private Context mContext;


    @BindView(R.id.rv_new)
    RecyclerView recyclerView;

    int loadCount = 0;
    private BaseAdapter mAdapter;
    private HotTakePhotoAdapter adapter;
    private List<DynamicEntity> dataSet;


    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";
    private int circle_id = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, null);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        circle_id = getArguments().getInt("id");
        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);

        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);

        initData();
        return view;
    }
    private void initData() {
        //创建被装饰者类实例
        adapter = new HotTakePhotoAdapter(getContext());

        mAdapter = new LoadMoreAdapterWrapper(adapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                getData(callback);
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    //获取最新的随手拍
    private void getData(final ILoadCallback callback) {
        ApiService api = RetrofitClient.getInstance(getActivity()).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pageno", loadCount);
        params.put("circle_id", circle_id);
        params.put("type", "new");
        Call<ResultEntity> call = api.GetHotPast(params);
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
                    if (result.getData() == null) {
                        callback.onFailure();
                        adapter.notifyDataSetChanged();
                    } else {
                        dataSet = JSON.parseArray(result.getData().toString(), DynamicEntity.class);
//                    if (dataSet!=null)
//                        setData(dataSet);
                        if (dataSet != null && dataSet.size() > 0) {
                            //数据的处理最终还是交给被装饰的adapter来处理
                            adapter.appendData(dataSet);
                            loadCount++;
                            callback.onSuccess();
                            adapter.notifyDataSetChanged();
                        } else {
                            callback.onFailure();
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else
                    Toast.makeText(getActivity(), "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

}
