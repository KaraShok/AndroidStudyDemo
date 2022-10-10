package com.karashok.demouistudy.widget.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author KaraShokZ
 * @since 02-19-2022
 */
public class RadarGradientView extends View {

    public RadarGradientView(Context context) {
        super(context);
        init();
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                             int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // 画圆用到的paint
        paintCircle.setStyle(Paint.Style.STROKE); // 描边
        paintCircle.setStrokeWidth(1); // 宽度
        paintCircle.setAlpha(100); // 透明度
        paintCircle.setAntiAlias(true); // 抗锯齿
        paintCircle.setColor(Color.parseColor("#B0C4DE")); // 设置颜色 亮钢兰色

        // 扫描用到的paint
        paintRadar.setStyle(Paint.Style.FILL_AND_STROKE); // 填充
        paintRadar.setAntiAlias(true); // 抗锯齿

        post(run);
    }

    private int width, height;
    //五个圆
    private float[] pots = {0.05f, 0.25f, 0.5f, 0.75f, 1f};
    private Matrix matrix = new Matrix(); // 旋转需要的矩阵
    private int scanSpeed = 5; // 扫描速度

    private Paint paintCircle = new Paint(); // 画圆用到的paint
    private Paint paintRadar = new Paint(); // 扫描用到的paint

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        width = height = Math.min(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < pots.length; i++) {
            canvas.drawCircle(width / 2, height / 2, width * pots[i] / 2, paintCircle);
        }
        // 画布的旋转变换 需要调用save() 和 restore()
        canvas.save();

        SweepGradient scanShader = new SweepGradient(width / 2, height / 2,
                new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
        paintRadar.setShader(scanShader); // 设置着色器
        canvas.concat(matrix);
        canvas.drawCircle(width / 2, height / 2, width * pots[4] / 2, paintRadar);

        canvas.restore();
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            matrix.postRotate(scanSpeed, width / 2, height / 2); // 旋转矩阵
            invalidate(); // 通知view重绘
            postDelayed(run, 50); // 调用自身 重复绘制
        }
    };
}
