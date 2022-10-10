package com.karashok.demoskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.karashok.demoskin.R;
import com.karashok.demoskin.utils.Skin;
import com.karashok.demoskin.utils.SkinUtils;
import com.karashok.demoskincore.SkinManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SkinActivity extends AppCompatActivity {

    /**
     * 从服务器拉取的皮肤表
     */
    private List<Skin> skins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        skins.add(new Skin("594613b7fa474136b638d25261d65744", "1111111.skin", "app_skin-debug.apk"));
        findViewById(R.id.skin_change)
                .setOnClickListener(clickListener);
        findViewById(R.id.skin_restore)
                .setOnClickListener(clickListener);
        findViewById(R.id.skin_day)
                .setOnClickListener(clickListener);
        findViewById(R.id.skin_night)
                .setOnClickListener(clickListener);
    }

    private void loadSkin(Skin skin) {
        File theme = new File(getFilesDir(),"theme");
        if (theme.exists() && theme.isFile()) {
            theme.delete();
        }
        theme.mkdirs();
        File skinFile = skin.getSkinFile(theme);
        if (skinFile.exists()) {
            return;
        }
        FileOutputStream fos = null;
        InputStream is = null;
        File tempSkin = new File(skinFile.getParentFile(),skin.name + ".temp");
        try{
            fos = new FileOutputStream(tempSkin);
            is = getAssets().open(skin.url);
            byte[] bytes = new byte[10240];
            int len;
            while ((len = is.read(bytes)) != -1) {
                fos.write(bytes,0,len);
            }

            String skinMD5 = SkinUtils.getSkinMD5(tempSkin);
            Log.d("DemoSkinAty", "skinMD5 " + skinMD5);
            boolean equals = TextUtils.equals(skinMD5, skin.md5);

            if (true) {
                Log.d("DemoSkinAty", "校验成功,修改文件名。");
                tempSkin.renameTo(skinFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            tempSkin.delete();
            if (fos != null) {
                try{
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try{
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void changeDayOrNight(boolean isDay) {
        //获取当前的模式，设置相反的模式，这里只使用了，夜间和非夜间模式
//        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (isDay) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();//需要recreate才能生效
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if (vId == R.id.skin_change) {
                Skin skin = skins.get(0);
                loadSkin(skin);
                SkinManager.getInstance().loadSkin(skin.path);
            } else if (vId == R.id.skin_restore) {
                SkinManager.getInstance().loadSkin(null);
            } else if (vId == R.id.skin_day) {
                changeDayOrNight(true);
            } else if (vId == R.id.skin_night) {
                changeDayOrNight(false);
            }
        }
    };

}