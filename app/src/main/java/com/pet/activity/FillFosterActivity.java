package com.pet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pet.R;

import org.feezu.liuli.timeselector.TimeSelector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 寄养申请信息填写
 */
public class FillFosterActivity extends BaseActivity {

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_foster);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back,R.id.tv_start_time,R.id.tv_end_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_start_time:
                selectTime(tvStartTime);
                break;

            case R.id.tv_end_time:
                selectTime(tvEndTime);
                break;
        }
    }
    //时间选择器
    private void selectTime(final TextView textView){
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                textView.setText(time);
            }
        }, "2018-07-29 17:34", "2020-01-01 15:20");
        timeSelector.show();
    }
}
