package com.karashok.androidstudydemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.karashok.demoaidlclient.AidlClientMainActivity;
import com.karashok.demoglide.DemoGlideMainActivity;
import com.karashok.demoglide.glide.Glide;
import com.karashok.demohermes.demo.HermesMainActivity;
import com.karashok.demookhttp.OkHttpMainActivity;
import com.karashok.demorouter.RouterMainActivity;
import com.karashok.demouistudy.UIMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private HashMap<Integer,Class> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ContentData> dataList = new ArrayList<>();
        dataList.add(new ContentData("module_ui_study_demo",UIMainActivity.class));
        dataList.add(new ContentData("module_demo_aidl_client",AidlClientMainActivity.class));
        dataList.add(new ContentData("module_demo_hermes",HermesMainActivity.class));
        dataList.add(new ContentData("module_demo_okhttp",OkHttpMainActivity.class));
        dataList.add(new ContentData("module_demo_router", RouterMainActivity.class));
        dataList.add(new ContentData("module_demo_glide", DemoGlideMainActivity.class));

        MainContentAdapter adapter = new MainContentAdapter(dataList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(MainActivity.this,dataList.get(position).cls));
            }
        });
        RecyclerView rv = findViewById(R.id.content_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    private void retrofitDemo() {
        String url = "https://www.baidu.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        NetApi netApi = retrofit.create(NetApi.class);
        retrofit2.Call<Object> author = netApi.getAuthor("");
        author.enqueue(new retrofit2.Callback<Object>() {
            @Override
            public void onResponse(retrofit2.Call<Object> call,
                                   retrofit2.Response<Object> response) {

            }

            @Override
            public void onFailure(retrofit2.Call<Object> call, Throwable t) {

            }
        });
    }

    private void okhttpDemo() {
        String url = "https://www.baidu.com";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
//        call.execute();
        try{
            call.enqueue(
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                    });
        } catch (Exception e) {

        }
    }

    private void glideDemo(ImageView iv) {
        Glide.with(this).load("http://goo.gl/gEgYUd").into(iv);
    }
}