package com.karashok.demoskincore;

import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.karashok.demoskincore.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-19-2022
 */
public class SkinAttribute {

    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");

        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");

        mAttributes.add("skinTypeface");
    }

    private Typeface typeface;

    private List<SkinView> skinViews = new ArrayList<>();

    public SkinAttribute(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void load(View view, AttributeSet attrs) {
        List<SkinPair> skinPairs = new ArrayList<>();
        for (int i = 0, count = attrs.getAttributeCount(); i < count; i++) {
            String attributeName = attrs.getAttributeName(i);
            Log.d("DemoSkinCoreAttribute", "attributeName: " + attributeName);
            if (mAttributes.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                Log.d("DemoSkinCoreAttribute", "attributeValue: " + attributeValue);
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId;
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(),new int[]{attrId})[0];
                } else {
                    // 正常以 @ 开头
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                skinPairs.add(new SkinPair(attributeName,resId));

            }
        }

        if (!skinPairs.isEmpty()) {
            SkinView skinView = new SkinView(view,skinPairs);
            skinView.applySkin(typeface);
            skinViews.add(skinView);
        } else if (view instanceof TextView || view instanceof SkinViewSupport) {
            // 没有属性满足 但是需要修改字体
            SkinView skinView = new SkinView(view,skinPairs);
            skinView.applySkin(typeface);
            skinViews.add(skinView);
        }
    }

    public void applySkin() {
        for (SkinView skinView : skinViews) {
            skinView.applySkin(typeface);
        }
    }
}
