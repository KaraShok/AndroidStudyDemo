package com.karashok.demohook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jump2(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        // 系统里面做了手脚   --》newIntent   msg--->obj-->intent
        startActivity(intent);
    }
    public void jump3(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
    public void jump4(View view) {
        Intent intent = new Intent(this,FourthActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        SharedPreferences share = this.getSharedPreferences("DemoHook", MODE_PRIVATE);//实例化
        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
        editor.putBoolean("login",false);   //设置保存的数据
        Toast.makeText(this, "退出登录成功",Toast.LENGTH_SHORT).show();
        editor.commit();    //提交数据保存
    }
}