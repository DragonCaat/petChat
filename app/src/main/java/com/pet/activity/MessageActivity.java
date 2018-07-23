package com.pet.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.CutePetAdapter;
import com.pet.adapter.MessageAdapter;
import com.pet.bean.Const;
import com.pet.bean.CutePetEntity;
import com.pet.bean.MessageEntity;
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
 * 消息管理的界面
 */
public class MessageActivity extends BaseActivity {

    @BindView(R.id.rv_message)
    XRecyclerView xRecyclerView;

    private List<MessageEntity> list;
    private MessageAdapter adapter;
    //宠物
    private List<MessageEntity> dataSet;
    private boolean isFirst = true;
    private boolean noData = false;

    private int pageno = 0;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);

        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        mContext = this;
        initData();
        getMessageData();

    }
    //初始化刷新
    private void initData() {
        xRecyclerView.setOnRefreshListener(new XRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                if (list == null)
                    getMessageData();
                else {
                    list = null;
                    pageno = 0;
                    isFirst = true;
                    getMessageData();
                }
            }

            @Override
            public void onLoadMore() {
                //加载更多
                if (!noData) {
                    pageno++;
                    getMessageData();
                } else {
                    xRecyclerView.refreshComlete();
                }

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    //获取个人宠物信息
    private void getMessageData() {
        ApiService api = RetrofitClient.getInstance(mContext).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pageno", pageno);
        Call<ResultEntity> call = api.show_message(params);
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
                        list = JSON.parseArray(result.getData().toString(), MessageEntity.class);
                        if (list != null && list.size() > 0) {
                            adapter = new MessageAdapter(list,MessageActivity.this);

                        } else {
                            Toast.makeText(mContext, "当前没有任何消息", Toast.LENGTH_SHORT).show();
                        }
                        xRecyclerView.refreshComlete();
                    } else {
                        dataSet = JSON.parseArray(result.getData().toString(), MessageEntity.class);
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
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }
}
