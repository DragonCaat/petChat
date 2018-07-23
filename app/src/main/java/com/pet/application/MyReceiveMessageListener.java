package com.pet.application;

import android.util.Log;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by dragon on 2018/7/11.
 * 融云消息监听
 */

public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

    /**
     * 收到消息的处理。
     * @param message 收到的消息实体。
     * @param left 剩余未拉取消息数目。
     * @return
     */
    @Override
    public boolean onReceived(Message message, int left) {
        //开发者根据自己需求自行处理
        Log.i("hello", "onReceived: "+message);

        return false;
    }
}
