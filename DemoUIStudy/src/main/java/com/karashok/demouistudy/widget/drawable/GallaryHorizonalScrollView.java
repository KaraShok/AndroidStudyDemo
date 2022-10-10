package com.karashok.demouistudy.widget.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author KaraShokZ
 * @since 02-26-2022
 */
public class GallaryHorizonalScrollView extends HorizontalScrollView {


    public GallaryHorizonalScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public GallaryHorizonalScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GallaryHorizonalScrollView(@NonNull Context context, @Nullable AttributeSet attrs,
                                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private LinearLayout container;
    private int centerX;
    private int icon_width;

    private void init() {
        LinearLayout.LayoutParams lLLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(lLLp);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    //渐变图片
                    reveal();
                }
                return false;
            }
        });
    }

    private void reveal() {
        float scrollX = getScrollX();
        int index_left = (int)(scrollX/icon_width);
        int index_right = index_left + 1;

        for (int i = 0,size = container.getChildCount(); i < size; i++) {
            // scrollX%icon_width:代表滑出去的距离
            // 滑出去了icon_width/2  icon_width/2%icon_width
            float ratio = 5000f/icon_width;
            if(i == index_left){

                ImageView leftIv = (ImageView) container.getChildAt(index_left);
                leftIv.setImageLevel((int)(5000-scrollX%icon_width*ratio));
                ImageView rightIv = (ImageView) container.getChildAt(index_right);
            } else if (index_right < size && i == index_right) {
                ImageView iv_right = (ImageView) container.getChildAt(index_right);
                // scrollX%icon_width:代表滑出去的距离
                // 滑出去了icon_width/2  icon_width/2%icon_width
                iv_right.setImageLevel((int)(10000-scrollX%icon_width*ratio));
            } else {
                ImageView iv = (ImageView) container.getChildAt(i);
                iv.setImageLevel(0);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View childAt = container.getChildAt(0);
        icon_width = childAt.getMeasuredWidth();
        centerX = getMeasuredWidth() / 2 - icon_width / 2;
        container.setPadding(centerX,0,centerX,0);
    }

    //添加图片的方法
    public void addImageViews(Drawable[] revealDrawables){
        for (int i = 0; i < revealDrawables.length; i++) {
            ImageView img = new ImageView(getContext());
            img.setImageDrawable(revealDrawables[i]);
            container.addView(img);
            if(i == 0){
                img.setImageLevel(5000);
            }
        }
        addView(container);
    }
}
