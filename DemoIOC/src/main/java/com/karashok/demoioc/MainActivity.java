package com.karashok.demoioc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@ContentView(R.layout.activity_main)
public class MainActivity extends IOCBaseActivity {

    @ViewInject(R.id.ioc_btn_1)
    Button btn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn1.setText("InjectText");
    }

    @OnClick({
            R.id.ioc_btn_1,
            R.id.ioc_btn_2,
    })
    public void click(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ioc_btn_1) {
            showToast("btn_1 点击");
        } else if (viewId == R.id.ioc_btn_2) {
            NewsDialog newsDialog = new NewsDialog(this);
            newsDialog.show();
        }
    }

    public boolean longClick(View view) {
        showToast("长按了");
        return true;
    }

    private void showToast(String content) {
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }
}