package com.karashok.imgoptimization;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-15-2022
 */
public class ContentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ContentAdapter(@Nullable List<String> data) {
        super(R.layout.item_rv_content, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        Bitmap bitmap = ImageCache.getInstance().getBitmapFromMemory(item);
        if (null == bitmap) {
            Bitmap reuseable = ImageCache.getInstance().getReuseable(100, 130, 1);
            bitmap = ImageCache.getInstance().getBitmapFromDisk(item,reuseable);
            if (null == bitmap) {
                bitmap = ImageResize.resizeBitmap(mContext,R.drawable.liuyan,150,190,false,reuseable);
                ImageCache.getInstance().putBitmapToMemory(item,bitmap);
                ImageCache.getInstance().putBitmapToDisk(item,bitmap);
                Log.d("demoImgOptimi", "从网络加载了数据");
            } else {
                Log.d("demoImgOptimi", "从磁盘中加载了数据");
            }
        } else {
            Log.d("demoImgOptimi", "从内存中加载了数据");
        }
        helper.setImageBitmap(R.id.content_iv,bitmap);
    }

}
