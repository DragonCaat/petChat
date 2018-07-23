package com.pet.activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.AnswerAdapter;
import com.pet.adapter.DynamicAnswerAdapter;
import com.pet.bean.AnswerEntity;
import com.pet.bean.Const;
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
 * 动态详情界面
 */
public class DynamicDetailActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    XRecyclerView xRecyclerView;

    @BindView(R.id.et_userAnswer)
    EditText mEtUserAnswer;

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    private DynamicAnswerAdapter adapter;
    private AnswerEntity answerEntity;
    private List<AnswerEntity.CmmtBean> cmmtBeans;
    private List<AnswerEntity.CmmtBean> dataSet;

    private Context mContext;

    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    private int pageno = 0;
    private boolean isFirst = true;
    private boolean noData = false;
    //private int id = 0;

    //动态的id
    private int postId = 0;

    //用户输入的回复信息
    private String userInput = "";

    private int itemPosition;
    private Bundle mReenterState;

    private int flag = 0;//0:自己的动态，1：别人的动态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        mContext = this;
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);
        postId = intent.getIntExtra("DynamicId", 0);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);
        ButterKnife.bind(this);
        if (flag == 0)
            ivDelete.setVisibility(View.VISIBLE);
        else
            ivDelete.setVisibility(View.GONE);

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
                else {
                    answerEntity = null;
                    cmmtBeans = null;
                    isFirst = true;
                    pageno = 0;
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
                }
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.btn_send,R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            //发表回复
            case R.id.btn_send:
                checkUserInput();
                break;


            case R.id.iv_delete:
                show();
                break;
            default:

                break;
        }
    }

    //展示删除的弹框
    private void show() {
        final Dialog bottomDialog = new Dialog(this,R.style.BottomDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_item, null);

        TextView tvPositive = view.findViewById(R.id.tv_positive);
        tvPositive.setText("删除");

        bottomDialog.setContentView(view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.dialog_animation);
        bottomDialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams params = bottomDialog.getWindow().getAttributes();
        //params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;//点击后会出现系统导航栏
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;//点击后不会出现系统导航栏
        bottomDialog.getWindow().setAttributes(params);

        bottomDialog.show();

        //确认删除
        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
                delete_user_dynamic();
            }
        });
        TextView tvCancle = view.findViewById(R.id.tv_cancel);
        //取消删除
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });

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

    /**
     * 获取动态详情
     * <p>
     * postId:动态的ID
     **/
    private void getData() {
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("pageno", pageno);
        params.put("dynamic_id", postId);
        Call<ResultEntity> call = api.dynamic_post_details(params);
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
                        adapter = new DynamicAnswerAdapter(mContext, answerEntity, cmmtBeans, new OnItemPictureClickListener() {
                            @Override
                            public void onItemPictureClick(int item, int position, String url, List<String> urlList, ImageView imageView) {
                                itemPosition = item;
                                Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                                intent.putStringArrayListExtra("imageList", (ArrayList<String>) urlList);
                                intent.putExtra(P.START_ITEM_POSITION, itemPosition);
                                intent.putExtra(P.START_IAMGE_POSITION, position);
                                intent.putExtra("flag", 1);
                                ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(DynamicDetailActivity.this, imageView, imageView.getTransitionName());
                                startActivity(intent, compat.toBundle());
                            }
                        });
                        //消息数量少于10
                        if (cmmtBeans.size() < 10)
                            noData = true;

                        xRecyclerView.refreshComlete();
                    } else {
                        answerEntity = JSON.parseObject(result.getData().toString(), AnswerEntity.class);
                        dataSet = answerEntity.getCmmt();
                        if (dataSet != null && dataSet.size() > 0) {
                            cmmtBeans.addAll(dataSet);
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

    //删除个人动态
    private void delete_user_dynamic() {
        llLoading.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");
        params.put("dynamic_id",postId);

        Call<ResultEntity> call = api.delete_user_dynamic(params);
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
                    Toast.makeText(mContext,""+result.getMessage(),Toast.LENGTH_SHORT).show();
                    finish();
                }else
                    Toast.makeText(mContext,""+result.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }


    //发表动态评论
    private void sendData() {
        llLoading.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        params.put("cmmt_content", userInput);
        params.put("dynamic_id", postId);
        params.put("super_user_id", "");
        params.put("super_cmmt_id", "");
        Call<ResultEntity> call = api.dynamic_cmmt(params);
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
//                    AnswerEntity.CmmtBean cmmtBean = JSON.parseObject(result.getData().toString(), AnswerEntity.CmmtBean.class);
//                    cmmtBeans.add(cmmtBean);
//                    adapter.notifyDataSetChanged();
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
