package com.pet.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pet.R;
import com.pet.adapter.AnswerAdapter;
import com.pet.baseadapter.ILoadCallback;
import com.pet.bean.AnswerEntity;
import com.pet.bean.Const;
import com.pet.bean.DynamicEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.PreferencesUtils;
import com.pet.view.NineGridTestLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 话题详情界面
 */
public class TopicDetailActivity extends AppCompatActivity {

    @BindView(R.id.rv_answer)
    RecyclerView mRvAnswer;
    @BindView(R.id.circleImage_user)
    CircleImageView mCvHead;
    @BindView(R.id.tv_nickName)
    TextView mTvNickName;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.images)
    NineGridTestLayout layout;

    private AnswerAdapter answerAdapter;
    private List<AnswerEntity> list;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private String nickName;
    private String tvContent;
    private String head;
    private int id = 0;
    private String postId = "";

    private ArrayList<String> urlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        Intent intent = getIntent();
        nickName = intent.getStringExtra("nick");
        tvContent = intent.getStringExtra("content");
        head = intent.getStringExtra("head");
        id = intent.getIntExtra("id", 0);
        postId = intent.getStringExtra("postId");
        urlList = intent.getStringArrayListExtra("urls");

        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);
        ButterKnife.bind(this);

        setData();
        getData();

    }

    private void setData() {
        mTvNickName.setText(nickName);
        mTvContent.setText(tvContent);
        Glide.with(this).load(Const.PIC_URL + head).into(mCvHead);

        layout.setIsShowAll(true); //当传入的图片数超过9张时，是否全部显示
        layout.setSpacing(5); //动态设置图片之间的间隔
        layout.setUrlList(urlList); //最后再设置图片url
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

    //获取评论
    private void getData() {
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pageno", 0);
        params.put("post_id", postId);

        Call<ResultEntity> call = api.ShowPostCmmt(params);
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
                    if (result.getData() != null) {
                        list = JSON.parseArray(result.getData().toString(), AnswerEntity.class);
                        if (list != null)
                            setCommitData();
                        else
                            Toast.makeText(TopicDetailActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    //设置评论数据
    private void setCommitData() {
        answerAdapter = new AnswerAdapter(this, list);
        mRvAnswer.setLayoutManager(new GridLayoutManager(this, 1));
        mRvAnswer.setAdapter(answerAdapter);
    }
}
