package com.karashok.demouistudy.widget.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.karashok.demouistudy.R;

/**
 * @author KaraShokZ
 * @since 02-18-2022
 */
public class BitmapShaderView extends View {

    public BitmapShaderView(Context context) {
        super(context);
        init();
    }

    public BitmapShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BitmapShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BitmapShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        bitMap = ((BitmapDrawable)getResources().getDrawable(R.drawable.xyjy)).getBitmap();
        bitmapWidth = bitMap.getWidth();
        bitmapHeight = bitMap.getHeight();
    }

    private Bitmap bitMap = null;
    private int bitmapWidth;
    private int bitmapHeight;
    private Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * TileMode.CLAMP 拉伸最后一个像素去铺满剩下的地方
         * TileMode.MIRROR 通过镜像翻转铺满剩下的地方。
         * TileMode.REPEAT 重复图片平铺整个画面（电脑设置壁纸）
         * 在图片和显示区域大小不符的情况进行扩充渲染
         */
        BitmapShader bitMapShader = new BitmapShader(bitMap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(bitMapShader);
        paint.setAntiAlias(true);
        //设置像素矩阵，来调整大小，为了解决宽高不一致的问题。
        float scale = Math.max(bitmapWidth,bitmapHeight) / Math.min(bitmapWidth,bitmapHeight);

        Matrix matrix = new Matrix();
        matrix.setScale(scale,scale);
        bitMapShader.setLocalMatrix(matrix);

//        canvas.drawCircle(bitmapHeight / 2,bitmapHeight / 2, bitmapHeight / 2 ,paint);
//        canvas.drawOval(new RectF(0 , 0, bitmapWidth, bitmapHeight),paint);
        canvas.drawRect(new Rect(0,0 , getMeasuredWidth(), getMeasuredHeight()),paint);
    }
}
