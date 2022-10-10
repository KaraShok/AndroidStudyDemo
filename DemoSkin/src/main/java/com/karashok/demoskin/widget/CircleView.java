package com.karashok.demoskin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.karashok.demoskin.R;
import com.karashok.demoskincore.SkinViewSupport;
import com.karashok.demoskincore.utils.SkinResources;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-18-2022
 */
public class CircleView extends View implements SkinViewSupport {

    private AttributeSet attrs;
    //画笔
    private Paint mTextPain;
    //半径
    private int radius;

    private int circleColorResId;

    public CircleView(Context context) {
        this(context, null, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        circleColorResId = typedArray.getResourceId(R.styleable.CircleView_circleColor, 0);
        typedArray.recycle();
        mTextPain = new Paint();
        mTextPain.setColor(getResources().getColor(circleColorResId));
        //开启抗锯齿，平滑文字和圆弧的边缘
        mTextPain.setAntiAlias(true);
        //设置文本位于相对于原点的中间
        mTextPain.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取宽度一半
        int width = getWidth() / 2;
        //获取高度一半
        int height = getHeight() / 2;
        //设置半径为宽或者高的最小值
        radius = Math.min(width, height);
        //利用canvas画一个圆
        canvas.drawCircle(width, height, radius, mTextPain);

    }

    public void setCircleColor(@ColorInt int color) {
        mTextPain.setColor(color);
        invalidate();
    }

    @Override
    public void applySkin() {
        if (circleColorResId != 0) {
            setCircleColor(SkinResources.getInstance().getColor(circleColorResId));
        }
    }
}
