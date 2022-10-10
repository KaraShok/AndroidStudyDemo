package com.karashok.demoaop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Proxy;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ILoginRouter {


    private ILoginRouter loginRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginRouter = (ILoginRouter)Proxy.newProxyInstance(getClassLoader(),
                new Class[]{ILoginRouter.class},
                new MyInvocationHandler(this)
        );
    }

    //摇一摇
    @UserInfoBehaviorTrace("摇一摇")
    @BehaviorTrace("摇一摇")
    public void mShake(View view) {
        long begin = System.currentTimeMillis();
        SystemClock.sleep(new Random().nextInt(2000));
        long duration = System.currentTimeMillis() - begin;
    }

    //语音消息
    @BehaviorTrace("语音消息")
    public void mAudio(View view) {
        loginRouter.toRouter();
    }

    //视频通话
    @BehaviorTrace("视频通话")
    public void mVideo(View view) {

    }

    //发表说说
    @BehaviorTrace("发表说说")
    public void saySomething(View view) {

    }

    @Override
    public void toRouter() {
        Toast.makeText(this,"正常跳转",Toast.LENGTH_SHORT).show();
    }
}