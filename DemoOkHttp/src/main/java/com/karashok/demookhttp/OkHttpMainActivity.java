package com.karashok.demookhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class OkHttpMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_main);

        String url = "http://www.baidu.com";

        HttpClient httpClient = new HttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = httpClient.newCall(request);
    call.enqueue(
        new Callback() {
          @Override
          public void onFailure(Call call, Throwable throwable) {
            Log.d("DemoOkHttp", "onFailure: a");
          }

          @Override
          public void onResponse(Call call, Response response) {
              Log.d("DemoOkHttp", "Response: " + response.body);
          }
        });
    }
}