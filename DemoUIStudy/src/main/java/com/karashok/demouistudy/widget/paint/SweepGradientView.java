package com.karashok.demouistudy.widget.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author KaraShokZ
 * @since 02-19-2022
 */
public class SweepGradientView extends View {

    public SweepGradientView(Context context) {
        super(context);
    }

    public SweepGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SweepGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SweepGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                             int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Paint paint = new Paint();
    private int[] colors = {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        SweepGradient sweepGradient = new SweepGradient(300, 300, colors, null);
        paint.setShader(sweepGradient);
		canvas.drawCircle(300, 300, 300, paint);
    }
}
