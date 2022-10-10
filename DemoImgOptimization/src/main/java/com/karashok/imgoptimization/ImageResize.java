package com.karashok.imgoptimization;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-15-2022
 */
public class ImageResize {

    public static Bitmap resizeBitmap(Context context, int id, int maxW,int maxH,
                                      boolean hasAlpha, Bitmap reusable) {
        Resources resources = context.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources,id,options);
        int w = options.outWidth;
        int h = options.outHeight;

        options.inSampleSize = calcuteInSampleSize(w,h,maxW,maxH);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        options.inMutable = true;
        options.inBitmap = reusable;
        return BitmapFactory.decodeResource(resources,id,options);
    }

    private static int calcuteInSampleSize(int w, int h, int maxW, int maxH) {
        int inSampleSize = 1;
        if (w > maxW && h > maxH) {
            while (w / inSampleSize > maxW && h / inSampleSize > maxH) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
