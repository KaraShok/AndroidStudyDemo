package com.karashok.demouistudy.widget.filter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.karashok.demouistudy.R;

import androidx.annotation.Nullable;

/**
 * @author KaraShokZ
 * @since 02-20-2022
 */
public class ColorMartrixFilterView extends View {

    public ColorMartrixFilterView(Context context) {
        super(context);
        init(null);
    }

    public ColorMartrixFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ColorMartrixFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public ColorMartrixFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                                  int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.ColorMartrixFilterView);
            try {
                if (typedArray.hasValue(R.styleable.ColorMartrixFilterView_cmfv_filter_type)) {
                    type = typedArray.getInt(R.styleable.ColorMartrixFilterView_cmfv_filter_type,0);
                }
            } catch (Exception e) {

            } finally {
                typedArray.recycle();
            }
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xyjy2);
    }

    private Paint paint;
    private Bitmap bitmap;
    private int type = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        ColorMatrix colorMartrix = null;

        switch (type) {
            case 1:
                // 平移运算---加法
                colorMartrix = new ColorMatrix(new float[]{
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 100,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0,
                });
                break;
            case 2:
                // 反相效果 -- 底片效果
                colorMartrix = new ColorMatrix(new float[]{
                        -1, 0, 0, 0, 255,
                        0, -1, 0, 0, 255,
                        0, 0, -1, 0, 255,
                        0, 0, 0, 1, 0,
                });
                break;
            case 3:
                // 缩放运算---乘法 -- 颜色增强
                colorMartrix = new ColorMatrix(new float[]{
                        1.2f, 0, 0, 0, 0,
                        0, 1.2f, 0, 0, 0,
                        0, 0, 1.2f, 0, 0,
                        0, 0, 0, 1.2f, 0,
                });
                break;
            case 4:
                // 黑白照片
                // 是将我们的三通道变为单通道的灰度模式
                // 去色原理：只要把R G B 三通道的色彩信息设置成一样，那么图像就会变成灰色，
                // 同时为了保证图像亮度不变，同一个通道里的R+G+B =1
                //
                colorMartrix = new ColorMatrix(new float[]{
                        0.213f, 0.715f, 0.072f, 0, 0,
                        0.213f, 0.715f, 0.072f, 0, 0,
                        0.213f, 0.715f, 0.072f, 0, 0,
                        0, 0, 0, 1, 0,
                });
                break;
            case 5:
                // 发色效果---（比如红色和绿色交换）
                colorMartrix = new ColorMatrix(new float[]{
                        1, 0, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 0, 0.5F, 0,
                });
                break;
            case 6:
                // 复古效果
                colorMartrix = new ColorMatrix(new float[]{
                        1 / 2f, 1 / 2f, 1 / 2f, 0, 0,
                        1 / 3f, 1 / 3f, 1 / 3f, 0, 0,
                        1 / 4f, 1 / 4f, 1 / 4f, 0, 0,
                        0, 0, 0, 1, 0,
                });
                break;
            case 7:
                // 颜色通道过滤
                //两个矩阵
                //本身颜色矩阵 A
                //过滤矩阵  c
                //a*c=out color
                colorMartrix = new ColorMatrix(new float[]{
                        1.3F, 0, 0, 0, 0,
                        0, 1.3F, 0, 0, 0,
                        0, 0, 1.3F, 0, 0,
                        0, 0, 0, 1, 0,
                });
                break;
            default:break;
        }

        RectF rectF = new RectF(200, 100, bitmap.getWidth() + 200, bitmap.getHeight());
        if (colorMartrix != null) {
            paint.setColorFilter(new ColorMatrixColorFilter(colorMartrix));
        }
        canvas.drawBitmap(bitmap, null, rectF, paint);
    }
}
