package com.karashok.demouistudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.karashok.demouistudy.ui.activity.DemoBezierActivity;
import com.karashok.demouistudy.ui.activity.DemoDrawableActivity;
import com.karashok.demouistudy.ui.activity.DemoFilterActivity;
import com.karashok.demouistudy.ui.activity.DemoPaintActivity;
import com.karashok.demouistudy.ui.activity.DemoPathMeasureActivity;
import com.karashok.demouistudy.ui.activity.DemoXfermodeActivity;
import com.karashok.demouistudy.ui.adapter.UIMainRecyclerViewAdapter;
import com.karashok.demouistudy.ui.entity.UIMainRecyclerEntity;

import java.util.ArrayList;
import java.util.List;

public class UIMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_main);

        List<UIMainRecyclerEntity> itemList = getItemList();
        UIMainRecyclerViewAdapter adapter = new UIMainRecyclerViewAdapter(itemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(UIMainActivity.this,itemList.get(position).cls));
            }
        });
        RecyclerView rv = findViewById(R.id.ui_main_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private List<UIMainRecyclerEntity> getItemList() {
        List<UIMainRecyclerEntity> itemList = new ArrayList<>();
        itemList.add(new UIMainRecyclerEntity(DemoPaintActivity.class,"demo_paint"));
        itemList.add(new UIMainRecyclerEntity(DemoFilterActivity.class,"demo_filter"));
        itemList.add(new UIMainRecyclerEntity(DemoXfermodeActivity.class,"demo_xfermode"));
        itemList.add(new UIMainRecyclerEntity(DemoDrawableActivity.class,"demo_drawable"));
        itemList.add(new UIMainRecyclerEntity(DemoBezierActivity.class,"demo_bezier"));
        itemList.add(new UIMainRecyclerEntity(DemoPathMeasureActivity.class,"demo_path_measure"));
        return itemList;
    }
}