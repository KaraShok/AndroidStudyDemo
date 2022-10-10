package com.karashok.demouistudy.widget.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author KaraShokZ
 * @since 02-17-2022
 */
public class LinearGradientTextView extends AppCompatTextView {

    private TextPaint paint;
    private LinearGradient gradient;
    private Matrix matrix;
    private float translate;
    private float DELTAX = 20;

    public LinearGradientTextView(@NonNull Context context) {
        super(context);
    }

    public LinearGradientTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearGradientTextView(@NonNull Context context, @Nullable AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint = getPaint();
        String text = getText().toString();
        float textWidth = paint.measureText(text);
        int gradintWidth = (int)(textWidth / text.length() * 3);
        gradient = new LinearGradient(-gradintWidth,0,0,0,new int[]{
                0x22ffffff, 0xffffffff, 0x22ffffff},null, Shader.TileMode.CLAMP);
        paint.setShader(gradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        translate += DELTAX;
        int measureWidth = getMeasuredWidth();
        String text = getText().toString();
        float textWidth = getPaint().measureText(text);
        int gradintWidth = (int)(textWidth / text.length() * 3);
        //到底部进行返回
        if(translate > measureWidth + gradintWidth){
            DELTAX = -DELTAX;
        }
        if(translate < 1){
            DELTAX = -DELTAX;
        }

        matrix = new Matrix();
        matrix.setTranslate(translate, 0);
        gradient.setLocalMatrix(matrix);
        postInvalidateDelayed(50);
    }
}
