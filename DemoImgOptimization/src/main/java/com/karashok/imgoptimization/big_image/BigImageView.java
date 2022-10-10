package com.karashok.imgoptimization.big_image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.InputStream;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-25-2022
 */
public class BigImageView extends View {

    private Rect mRect;
    private BitmapFactory.Options mOptions;
    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    private int mImageWidth;
    private int mImageHeight;
    private BitmapRegionDecoder mBitmapDecoder;
    private int mViewWidth;
    private int mViewHeight;
    private float mScale;
    private Bitmap mBitmap;

    public BigImageView(Context context) {
        super(context);
        init();
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        /**
         * 手按下的回调
         * @param e
         * @return
         */
        @Override
        public boolean onDown(MotionEvent e) {
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            return true;
        }

        /**
         *
         * @param e1   接下
         * @param e2   移动
         * @param distanceX    左右移动时的距离
         * @param distanceY   上下移动时的距离
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mRect.offset(0,(int)distanceY);
            if (mRect.bottom > mImageHeight) {
                mRect.bottom = mImageHeight;
                mRect.top = mImageHeight - (int)(mViewHeight / mScale);
            }
            if (mRect.top < 0) {
                mRect.bottom = (int)(mViewHeight / mScale);
                mRect.top = 0;
            }
            invalidate();
            return false;
        }

        /**
         * 处理惯性问题
         * @param e1
         * @param e2
         * @param velocityX   每秒移动的x点
         * @param velocityY
         * @return
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mScroller.fling(0,mRect.top,0,(int)-velocityY,0,0,0,mImageHeight - (int)(mViewHeight / mScale));
            return false;
        }

    };

    private void init() {
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();
        // 手势识别类
        mGestureDetector = new GestureDetector(getContext(),mGestureListener);
        // 滑动帮助
        mScroller = new Scroller(getContext());
    }

    public void setImage(InputStream is) {
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null,mOptions);
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        mOptions.inMutable = true;
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = false;

        try{
            // 创建一个区域解码器
            mBitmapDecoder = BitmapRegionDecoder.newInstance(is,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();

        mScale = mViewWidth / (float)mImageWidth;
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mImageWidth;
        mRect.bottom = (int) (mImageHeight / mScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmapDecoder == null) {
            return;
        }
        mOptions.inBitmap = mBitmap;
        mBitmap = mBitmapDecoder.decodeRegion(mRect,mOptions);
        Matrix matrix = new Matrix();
        matrix.setScale(mScale,mScale);
        canvas.drawBitmap(mBitmap,matrix,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 使用上一个接口的计算结果
     */
    @Override
    public void computeScroll() {
        if (mScroller.isFinished()) {
            return;
        }
        // true 表示当前滑动还没有结束
        if (mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int)(mViewHeight/mScale);
            invalidate();
        }
    }
}
