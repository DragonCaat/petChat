package com.pet.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pet.R;
import com.pet.adapter.SuggestAdapter;
import com.pet.bean.Const;
import com.pet.bean.LoginEntity;
import com.pet.bean.ResultEntity;
import com.pet.retrofit.ApiService;
import com.pet.retrofit.RetrofitClient;
import com.pet.utils.AnimationRotate;
import com.pet.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 意见反馈界面
 */
public class SuggestActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.listView)
    GridView gridView;
    @BindView(R.id.et_suggest)
    EditText mEtSuggest;

    private SuggestAdapter adapter;
    private PopupWindow popupWindow;
    private Context mContext;
    //用户输入数据
    private String userInput = "";
    private int user_id = 0;
    private String acces_token = "";
    private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        user_id = PreferencesUtils.getInt(this, Const.USER_ID, 0);
        acces_token = PreferencesUtils.getString(this, Const.ACCESS_TOKEN);
        phone = PreferencesUtils.getString(this, Const.MOBILE_PHONE);
        ButterKnife.bind(this);
        mContext = this;
        initData();
    }

    //初始化数据
    private void initData() {
        List<String> list = new ArrayList<>();
        list.add("软件功能");
        list.add("用户体验");
        list.add("定位问题");
        list.add("问诊服务");
        list.add("周边功能");
        list.add("其他问题");

        adapter = new SuggestAdapter(list, this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @OnClick({R.id.iv_finish, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                //提交反馈数据
            case R.id.btn_commit:
                checkData();
                break;
        }
    }

    private void checkData() {
        userInput = mEtSuggest.getText().toString().trim();
        if (TextUtils.isEmpty(userInput))
            Toast.makeText(this, "请输入反馈信息", Toast.LENGTH_SHORT).show();
        else
            addSuggest();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.setSelection(i);
        adapter.notifyDataSetChanged();
    }

    //登陆服务器
    private void addSuggest() {
        showLoadingDialog("正在上传...");
        ApiService api = RetrofitClient.getInstance(this).Api();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("access_token", acces_token);
        params.put("mobilephone", phone);
        params.put("app_Key", "yunmiao");

        params.put("content", userInput);
        Call<ResultEntity> call = api.addFeedback(params);
        call.enqueue(new retrofit2.Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call,
                                   Response<ResultEntity> response) {

                if (response.body() == null) {
                    return;
                }
                ResultEntity result = response.body();
                int res = result.getCode();
                if (res == 200) {// 登录成功
                    hideLoadingDialog();
                    mEtSuggest.setText("");
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    hideLoadingDialog();
                    Toast.makeText(mContext, "" + result.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResultEntity> arg0, Throwable arg1) {

            }
        });

    }

    //展示加载框
    private void showLoadingDialog(String s) {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(this).inflate(R.layout.loading_item, null);
        popupWindow.setContentView(view);
        ImageView ivLoading = view.findViewById(R.id.iv_loading);
        AnimationRotate.rotatebolowImage(ivLoading);
        TextView tvDes = view.findViewById(R.id.tv_des);
        tvDes.setText(s);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    //隐藏
    private void hideLoadingDialog() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }
}
