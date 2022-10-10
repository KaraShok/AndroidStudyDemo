package com.karashok.demouistudy.widget.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.karashok.demouistudy.R;

/**
 * @author KaraShokZ
 * @since 02-18-2022
 */
public class ShapeDrawableView extends View {

    public ShapeDrawableView(Context context) {
        super(context);
        init();
    }

    public ShapeDrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShapeDrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ShapeDrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BitmapShader bitMapShader = new BitmapShader(bitMap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        ShapeDrawable shapeDrawble = new ShapeDrawable(new OvalShape());
        shapeDrawble.getPaint().setShader(bitMapShader);
        shapeDrawble.setBounds(0,0,bitmapWidth,bitmapHeight);
        shapeDrawble.draw(canvas);
    }
}
