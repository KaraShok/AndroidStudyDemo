package com.karashok.demouistudy.widget.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.karashok.demouistudy.R;

/**
 * @author KaraShokZ
 * @since 02-19-2022
 */
public class ComposeShaderView extends View {

    public ComposeShaderView(Context context) {
        super(context);
        init();
    }

    public ComposeShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ComposeShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ComposeShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                             int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.heart)).getBitmap();
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
    }

    private Bitmap bitmap = null;
    private Paint paint = new Paint();
    private int bitmapWidth;
    private int bitmapHeight;
    private int[] colors = {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /***************用ComposeShader即可实现心形图渐变效果*********************************/
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //创建LinearGradient，用以产生从左上角到右下角的颜色渐变效果
        LinearGradient linearGradient = new LinearGradient(0, 0, bitmapWidth, bitmapHeight, Color.GREEN, Color.BLUE, Shader.TileMode.CLAMP);
        //bitmapShader对应目标像素，linearGradient对应源像素，像素颜色混合采用MULTIPLY模式
        ComposeShader composeShader = new ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY);
//        ComposeShader composeShader2 = new ComposeShader(composeShader, linearGradient, PorterDuff.Mode.MULTIPLY);

        //将组合的composeShader作为画笔paint绘图所使用的shader
        paint.setShader(composeShader);

        //用composeShader绘制矩形区域
        canvas.drawRect(0, 0, bitmapWidth, bitmapHeight, paint);
    }
}
