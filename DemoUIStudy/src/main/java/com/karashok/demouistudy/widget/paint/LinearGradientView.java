package com.karashok.demouistudy.widget.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author KaraShokZ
 * @since 02-18-2022
 */
public class LinearGradientView extends View {

    private Paint paint = new Paint();
    private int[] colors = {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW};

    public LinearGradientView(Context context) {
        super(context);
    }

    public LinearGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinearGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**线性渐变
         * x0, y0, 起始点
         *  x1, y1, 结束点
         * int[]  mColors, 中间依次要出现的几个颜色
         * float[] positions,数组大小跟colors数组一样大，中间依次摆放的几个颜色分别放置在那个位置上(参考比例从左往右)
         *    tile
         */
		LinearGradient linearGradient = new LinearGradient(0, 0,800, 800, colors, null, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
		canvas.drawRect(0, 0, 800, 800, paint);
    }
}
