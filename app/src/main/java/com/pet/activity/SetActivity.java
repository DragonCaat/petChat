package com.pet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pet.R;
import com.pet.activity.BaseActivity;
import com.pet.utils.FitStateUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragon on 2018/6/12.
 * 设置页面
 */

public class SetActivity extends BaseActivity {

    @BindView(R.id.iv_finish)
    ImageView mIvFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.iv_finish, R.id.tv_my_data, R.id.tv_pet_manage, R.id.tv_about_our,R.id.tv_suggest})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_my_data:
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                break;
            //宠物管理
            case R.id.tv_pet_manage:
                skipPageWithAnim(PetManageActivity.class);
                break;
            //关于我们
            case R.id.tv_about_our:
                skipPageWithAnim(AboutOurActivity.class);
                break;
            //意见反馈
            case R.id.tv_suggest:
                skipPageWithAnim(SuggestActivity.class);
                break;
            default:
                break;
        }

    }


}
