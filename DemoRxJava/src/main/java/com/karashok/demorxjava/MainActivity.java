package com.karashok.demorxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("DemoRxJava", "main thread---" + Thread.currentThread().getName());
        Observable.create(
                new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(Emitter<Integer> emitter) throws Exception {
                        Log.d("DemoRxJava", "发射器 thread---" + Thread.currentThread().getName());
                        Log.d("DemoRxJava", "发射器---发射");
                        emitter.onNext(1);
                        emitter.onComplete();
                    }
                })
                .map(
                        new Function<Integer, String>() {
                            @Override
                            public String apply(Integer integer) {
                                return integer + " - map";
                            }
                        })
                .subscribeOn(Scheduler.IO)
                .observeOn(Scheduler.AndroidMain)
                .subscribe(
                        new Observe<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d("DemoRxJava", "订阅者 onSubscribe thread---" + Thread.currentThread().getName());
                                Log.d("DemoRxJava", "订阅者---onSubscribe");
                            }

                            @Override
                            public void onNext(String integer) {
                                Log.d("DemoRxJava", "订阅者 onNext thread---" + Thread.currentThread().getName());
                                Log.d("DemoRxJava", "订阅者---onNext---" + integer);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("DemoRxJava", "订阅者 onError thread---" + Thread.currentThread().getName());
                                Log.d("DemoRxJava", "订阅者---onError");
                            }

                            @Override
                            public void onComplete() {
                                Log.d("DemoRxJava", "订阅者 onComplete thread---" + Thread.currentThread().getName());
                                Log.d("DemoRxJava", "订阅者---onComplete");
                            }
                        });
    }
}