package com.karashok.demouistudy.widget.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.karashok.demouistudy.R;

/**
 * @author KaraShokZ
 * @since 02-19-2022
 */
public class ZoomImageView extends View {

    public ZoomImageView(Context context) {
        super(context);
        init();
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                         int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        bitmapScale = bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xyjy2);
        //放大后的整个图片
        bitmapScale = Bitmap.createScaledBitmap(bitmapScale,bitmapScale.getWidth() * FACTOR,
                bitmapScale.getHeight() * FACTOR,true);
        BitmapShader bitmapShader = new BitmapShader(bitmapScale, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setShader(bitmapShader);
        // 切出矩形区域，用来画圆（内切圆）
        shapeDrawable.setBounds(0,0,RADIUS * 2,RADIUS * 2);
    }

    //放大倍数
    private static final int FACTOR = 2;
    //放大镜的半径
    private static final int RADIUS  = 100;
    // 原图
    private Bitmap bitmap;
    // 放大后的图
    private Bitmap bitmapScale;
    // 制作的圆形的图片（放大的局部），盖在Canvas上面
    private ShapeDrawable shapeDrawable;

    private Matrix matrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 1、画原图
        canvas.drawBitmap(bitmap, 0 , 0 , null);

        // 2、画放大镜的图
        shapeDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        // 将放大的图片往相反的方向挪动
        matrix.setTranslate(RADIUS - x * FACTOR, RADIUS - y *FACTOR);
        shapeDrawable.getPaint().getShader().setLocalMatrix(matrix);
        // 切出手势区域点位置的圆
        shapeDrawable.setBounds(x-RADIUS,y - RADIUS, x + RADIUS, y + RADIUS);
        invalidate();
        return true;
    }
}
