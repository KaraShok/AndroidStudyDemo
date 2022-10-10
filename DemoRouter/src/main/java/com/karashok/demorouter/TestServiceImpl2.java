package com.karashok.demorouter;

import android.util.Log;

import com.karashok.demorouterannotation.Router;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-28-2022
 */
@Router(path = "/main/service2")
public class TestServiceImpl2 implements TestService {

    @Override
    public void test() {
        Log.i("Service", "我是app模块测试服务通信2");
    }
}
