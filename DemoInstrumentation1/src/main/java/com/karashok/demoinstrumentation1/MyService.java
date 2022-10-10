package com.karashok.demoinstrumentation1;

import android.util.Log;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-03-2022
 */
public class MyService extends BaseService{

    private int i = 0;

    @Override
    public void onCreate() {
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    Log.i("MyService", "run: "+(i++));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
