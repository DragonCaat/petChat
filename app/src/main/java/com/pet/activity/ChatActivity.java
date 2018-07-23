package com.pet.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.fragment.ConversationFragment;

/**
 * 会话界面
 */
public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        String name = getIntent().getData().getQueryParameter("title");
        tvTitle.setText(""+name);
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
