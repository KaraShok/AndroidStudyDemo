package com.karashok.demouistudy.widget.xfermode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import com.karashok.demouistudy.R;
import androidx.annotation.Nullable;

/**
 * @author KaraShokZ
 * @since 02-20-2022
 */
public class RoundImageView extends View {


    public RoundImageView(Context context) {
        super(context);
        init(null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                          int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.XfermodeRoundImageView);
            try {
                if (typedArray.hasValue(R.styleable.XfermodeRoundImageView_xriv_round_type)) {
                    type = typedArray.getInt(R.styleable.XfermodeRoundImageView_xriv_round_type,0);
                }
            } catch (Exception e) {

            } finally {
                typedArray.recycle();
            }
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBitPaint = new Paint();
        bmpDST = BitmapFactory.decodeResource(getResources(),R.drawable.xyjy2,null);
        bmpSRC = BitmapFactory.decodeResource(getResources(),R.drawable.shade,null);
    }

    private Paint mBitPaint;
    private Bitmap bmpDST,bmpSRC;
    private int type = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int layerId = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bmpDST,0,0,mBitPaint);
        switch (type) {
            case 1:
                mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            break;
            case 2:
                mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            break;
            case 3:
                mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            break;
        }
        canvas.drawBitmap(bmpSRC,0,0,mBitPaint);

        mBitPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}
