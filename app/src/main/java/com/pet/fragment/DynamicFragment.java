package com.pet.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.activity.ImagePagerActivity;
import com.pet.activity.ImagePreviewActivity;
import com.pet.adapter.DynamicAdapter;
import com.pet.adapter.HotTakePhotoAdapter;
import com.pet.adapter.NearPeopleAdapter;
import com.pet.baseadapter.BaseAdapter;
import com.pet.baseadapter.ILoadCallback;
import com.pet.baseadapter.LoadMoreAdapterWrapper;
import com.pet.baseadapter.OnLoad;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.NearByEntity;
import com.pet.bean.NineGridTestModel;
import com.pet.bean.ResultEntity;
import com.pet.bean.TakePhotoEntity;
import com.pet.ninelayout.OnItemPictureClickListener;
import com.pet.ninelayout.P;
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

public class DynamicFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_no_attention)
    ImageView mIvNoAttention;
    @BindView(R.id.swipe_fresh)
    SwipeRefreshLayout swipeRefreshLayout;

    //private DynamicAdapter adapter;

    private Context mContext;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private HotTakePhotoAdapter adapter;
    int pageno = 0;
    private BaseAdapter mAdapter;

    private List<DynamicEntity> dataSet;

    private int itemPosition;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, null);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        user_id = PreferencesUtils.getInt(getActivity(), Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(getActivity(), Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(getActivity(), Const.MOBILE_PHONE);
        pageno=0;
        initData();

        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @OnClick()
    public void onClick() {

    }

    private void initData() {
        //创建被装饰者类实例
        adapter = new HotTakePhotoAdapter(getActivity(), new OnItemPictureClickListener() {
            @Override
            public void onItemPictureClick(int item,int position, String url, List<String> urlList, ImageView imageView) {
                itemPosition = item;
                Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                intent.putStringArrayListExtra("imageList", (ArrayList<String>) urlList);
                intent.putExtra(P.START_ITEM_POSITION, itemPosition);
                intent.putExtra(P.START_IAMGE_POSITION, position);
                ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, imageView.getTransitionName());
                startActivity(intent, compat.toBundle());
            }
        });

        mAdapter = new LoadMoreAdapterWrapper(adapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                getData(callback);

            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }


    //获取关注的动态
    private void getData(final ILoadCallback callback) {
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
                    if (result.getData() == null) {
                        callback.onFailure();
                        adapter.notifyDataSetChanged();
                    } else {
                        dataSet = JSON.parseArray(result.getData().toString(), DynamicEntity.class);
                        if (dataSet != null && dataSet.size() > 0) {
                            mIvNoAttention.setVisibility(View.INVISIBLE);
                            //数据的处理最终还是交给被装饰的adapter来处理
                            adapter.appendData(dataSet);
                            pageno++;
                            callback.onSuccess();
                            adapter.notifyDataSetChanged();
                        } else {
                            //请求不到数据的时候
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

    //刷新
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false); 
        dataSet = null;
        pageno = 0;
        adapter =null;
        initData();
    }
}
