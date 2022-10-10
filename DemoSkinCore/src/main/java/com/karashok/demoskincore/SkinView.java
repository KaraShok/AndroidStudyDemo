package com.karashok.demoskincore;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.karashok.demoskincore.utils.SkinResources;

import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-19-2022
 */
public class SkinView {

    View view;
    List<SkinPair> skinPairs;

    public SkinView(View view, List<SkinPair> skinPairs) {
        this.view = view;
        this.skinPairs = skinPairs;
    }

    public void applySkin(Typeface typeface) {
        applySkinSupport();
        applyTypeface(typeface);
        for (SkinPair skinPair : skinPairs) {
            Drawable left = null, top = null, right = null, bottom = null;
            switch (skinPair.attributeName) {
                case "background":
                    Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                    if (background instanceof Integer) {
                        view.setBackgroundColor((int) background);
                    } else {
                        ViewCompat.setBackground(view,(Drawable) background);
                    }
                    break;
                case "src":
                    background = SkinResources.getInstance().getBackground(skinPair.resId);
                    if (background instanceof Integer) {
                        ((ImageView) view).setImageDrawable(new ColorDrawable((int) background));
                    } else {
                        ((ImageView) view).setImageDrawable((Drawable) background);
                    }
                    break;
                case "textColor":
                    ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId));
                    break;
                case "drawableLeft":
                    left = SkinResources.getInstance().getDrawable(skinPair.resId);
                    break;
                case "drawableTop":
                    top = SkinResources.getInstance().getDrawable(skinPair.resId);
                    break;
                case "drawableRight":
                    right = SkinResources.getInstance().getDrawable(skinPair.resId);
                    break;
                case "drawableBottom":
                    bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                    break;
                case "skinTypeface":
                    applyTypeface(SkinResources.getInstance().getTypeface(skinPair.resId));
                    break;
                default:
                    break;
            }
            if (null != left || null != right || null != top || null != bottom) {
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                        bottom);
            }
        }
    }

    private void applySkinSupport() {
        if (view instanceof SkinViewSupport) {
            ((SkinViewSupport) view).applySkin();
        }
    }

    private void applyTypeface(Typeface typeface) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }
}
