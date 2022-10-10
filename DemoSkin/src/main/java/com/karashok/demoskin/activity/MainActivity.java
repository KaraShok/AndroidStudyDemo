package com.karashok.demoskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.karashok.demoskin.adapter.MainPagerAdapter;
import com.karashok.demoskin.fragment.MusicFragment;
import com.karashok.demoskin.fragment.RadioFragment;
import com.karashok.demoskin.fragment.VideoFragment;
import com.karashok.demoskin.widget.MyTabLayout;
import com.karashok.demoskin.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,SkinActivity.class));
                    }
                });
        MyTabLayout tl = findViewById(R.id.main_tl);
        ViewPager vp = findViewById(R.id.main_vp);

        List<Fragment> list = new ArrayList<>();
        list.add(new MusicFragment());
        list.add(new VideoFragment());
        list.add(new RadioFragment());
        List<String> listTitle = new ArrayList<>();
        listTitle.add("音乐");
        listTitle.add("视频");
        listTitle.add("电台");
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), list, listTitle);
        vp.setAdapter(adapter);
        tl.setupWithViewPager(vp);
    }
}