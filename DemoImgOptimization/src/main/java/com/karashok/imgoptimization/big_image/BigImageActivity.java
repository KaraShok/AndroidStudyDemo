package com.karashok.imgoptimization.big_image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.karashok.imgoptimization.R;

import java.io.InputStream;

public class BigImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        BigImageView biv = findViewById(R.id.big_image_view);
        InputStream is = null;
        try{
            //加载图片
            is = getAssets().open("big.png");
            biv.setImage(is);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}