package com.karashok.demoinstrumentation1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StaticBroadcast extends BroadcastReceiver {

    static final String ACTION = "com.karashok.demoinstrumentationhost.host_broadcast_action";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("StaticBroadcast", "我是插件   收到宿主的消息  静态注册的广播  收到宿主的消息");
        context.sendBroadcast(new Intent(ACTION));
    }
}