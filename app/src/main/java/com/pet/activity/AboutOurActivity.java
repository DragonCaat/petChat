package com.pet.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutOurActivity extends BaseActivity {

    @BindView(R.id.tv_app_version)
    TextView mTvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_our);
        ButterKnife.bind(this);

        mTvAppVersion.setText(getPackageInfo(this).versionName);
    }


    @OnClick({R.id.iv_finish, R.id.tv_privacy,R.id.tv_agreement,R.id.tv_health_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            //用户隐私
            case R.id.tv_privacy:

                break;
            //用户协议
            case R.id.tv_agreement:

                break;
            //健康模块申明
            case R.id.tv_health_notice:

                break;
            default:

                break;
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
}
