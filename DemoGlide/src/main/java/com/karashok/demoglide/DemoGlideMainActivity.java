package com.karashok.demoglide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.karashok.demoglide.glide.Glide;
import com.karashok.demoglide.glide.request.RequestOptions;

import java.io.File;

public class DemoGlideMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_glide_main);

        ImageView iv = findViewById(R.id.iv);
        ImageView iv1 = findViewById(R.id.iv1);
        ImageView iv2 = findViewById(R.id.iv2);

        Glide.with(this)
                .load("https://scpic.chinaz.net/files/pic/pic9/202112/apic37600.jpg")
                .apply(new RequestOptions()
                        .error(R.drawable.ic_launcher_background)
                        .placeHolder(R.mipmap.ic_launcher)
                        .override(500,500))
                .into(iv);

//        Glide.with(this)
//                .load(Environment.getExternalStorageDirectory() + "/main.jpg")
//                .into(iv1);
//
//        Glide.with(this)
//                .load(new File(Environment.getExternalStorageDirectory() + "/zyx.jpg"))
//                .into(iv2);

    }


    public void toNext(View view) {
        startActivity(new Intent(this, DemoGlideSecondActivity.class));
    }
}