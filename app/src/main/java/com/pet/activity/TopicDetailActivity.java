package com.pet.activity;

import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.AnswerAdapter;
import com.pet.adapter.NearPeopleAdapter;
import com.pet.bean.AnswerEntity;
import com.pet.bean.Const;
import com.pet.bean.NearByEntity;
import com.pet.bean.ResultEntity;
import com.pet.ninelayout.OnItemPictureClickListener;
import com.pet.ninelayout.P;
import com.pet.ninelayout.Utils;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.KeyCodeUtils;
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
 * 话题详情界面
 */
public class TopicDetailActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    XRecyclerView xRecyclerView;

    @BindView(R.id.et_userAnswer)
    EditText mEtUserAnswer;

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private AnswerAdapter adapter;
    private AnswerEntity answerEntity;
    private List<AnswerEntity.CmmtBean> cmmtBeans;
    private List<AnswerEntity.CmmtBean> dataSet;
    private Context mContext;
    private int pageno = 0;

    private boolean isFirst = true;
    private boolean noData = false;
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private int id = 0;
    //帖子的id
    private int postId = 0;

    //用户输入的回复信息
    private String userInput = "";
    private int itemPosition;
    private Bundle mReenterState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        mContext = this;
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        postId = intent.getIntExtra("postId", 0);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);
        ButterKnife.bind(this);

        initData();
        getData();

    }

    private void initData() {
        xRecyclerView.setOnRefreshListener(new XRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                if (answerEntity == null)
                    getData();
                else
                    xRecyclerView.refreshComlete();
            }

            @Override
            public void onLoadMore() {
                //加载更多
                if (!noData){
                    pageno++;
                    getData();
                }else {
                    xRecyclerView.refreshComlete();
                }
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            //发表回复
            case R.id.btn_send:
                checkUserInput();
                break;
            default:

                break;
        }
    }

    //检查用户的输入信息
    private void checkUserInput() {
        userInput = mEtUserAnswer.getText().toString();
        if (TextUtils.isEmpty(userInput))
            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
        else
            //网络请求
            sendData();
    }

    //获取评论
    private void getData() {
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pageno", pageno);
        params.put("post_id", postId);
        Call<ResultEntity> call = api.ShowSnapshot(params);
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
                        answerEntity = JSON.parseObject(result.getData().toString(), AnswerEntity.class);
                        cmmtBeans = answerEntity.getCmmt();
                        adapter = new AnswerAdapter(mContext, answerEntity, cmmtBeans, new OnItemPictureClickListener() {
                            @Override
                            public void onItemPictureClick(int item, int position, String url, List<String> urlList, ImageView imageView) {
                                itemPosition = item;
                                Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                                intent.putStringArrayListExtra("imageList", (ArrayList<String>) urlList);
                                intent.putExtra(P.START_ITEM_POSITION, itemPosition);
                                intent.putExtra(P.START_IAMGE_POSITION, position);
                                intent.putExtra("flag",1);
                                ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(TopicDetailActivity.this, imageView, imageView.getTransitionName());
                                startActivity(intent, compat.toBundle());
                            }
                        });

                        xRecyclerView.refreshComlete();
                    } else {
                        answerEntity = JSON.parseObject(result.getData().toString(), AnswerEntity.class);
                        dataSet = answerEntity.getCmmt();
                        if (dataSet != null && dataSet.size() > 0) {
                            cmmtBeans.addAll(dataSet);
                            adapter.notifyDataSetChanged();
                            xRecyclerView.refreshComlete();
                        }else {
                            xRecyclerView.refreshComlete();
                            noData = true;
                            Toast.makeText(mContext,"没数据了",Toast.LENGTH_SHORT).show();
                        }
                    }
                    xRecyclerView.verticalLayoutManager().setAdapter(adapter);


                } else
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();


//                    if (result.getData() != null) {
//                        answerEntity = JSON.parseObject(result.getData().toString(), AnswerEntity.class);
//                        cmmtBeans = answerEntity.getCmmt();
//                        if (cmmtBeans != null)
//                            setCommitData();
//                    }

            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }


    //发表评论
    private void sendData() {
        llLoading.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("cmmt_content", userInput);
        params.put("post_id", postId);
        params.put("super_user_id", "");
        params.put("super_cmmt_id", "");
        Call<ResultEntity> call = api.posts_cmmt(params);
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
                    mEtUserAnswer.setText("");
                    KeyCodeUtils.closeKeyCode(mContext, mEtUserAnswer);
                    llLoading.setVisibility(View.GONE);
                    AnswerEntity.CmmtBean cmmtBean =JSON.parseObject(result.getData().toString(),AnswerEntity.CmmtBean.class);
                    cmmtBeans.add(cmmtBean);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityReenter(int requestCode, Intent data) {
        super.onActivityReenter(requestCode, data);

        initShareElement();
        getActivityData(data);
    }


    public void initShareElement() {
        setExitSharedElementCallback(mCallback);
    }
    //调用activity返回数据的时候调用
    public void getActivityData(Intent data) {
        mReenterState = new Bundle(data.getExtras());
        int startingPosition = mReenterState.getInt(P.CURRENT_ITEM_POSITION);
        if (startingPosition != itemPosition) {//如果不是同一个item就滚动到指定的item
           // xRecyclerView.scrollToPosition(itemPosition);
        }
        postponeEnterTransition();
        xRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                xRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                xRecyclerView.requestLayout();
                startPostponedEnterTransition();
                return true;
            }
        });

    }

    private final SharedElementCallback mCallback = new SharedElementCallback() {

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mReenterState != null) {
                //从别的界面返回当前界面
                int startingPosition = mReenterState.getInt(P.START_IAMGE_POSITION);
                int currentPosition = mReenterState.getInt(P.CURRENT_IAMGE_POSITION);
                if (startingPosition != currentPosition) {//如果不是之前的那张图片就切换到指定的图片
                    String newTransitionName = Utils.getNameByPosition(itemPosition, currentPosition);
                    View newSharedElement = xRecyclerView.findViewWithTag(newTransitionName);
                    if (newSharedElement != null) {
                        names.clear();
                        names.add(newTransitionName);
                        sharedElements.clear();
                        sharedElements.put(newTransitionName, newSharedElement);
                    }
                }
                mReenterState = null;
            }
        }
    };

}
