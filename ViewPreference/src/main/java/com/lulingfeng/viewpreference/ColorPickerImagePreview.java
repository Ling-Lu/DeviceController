package com.lulingfeng.viewpreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.margaritov.preference.colorpicker.AlphaPatternDrawable;
import net.margaritov.preference.colorpicker.ColorPickerDialog;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class ColorPickerImagePreview extends android.support.v7.widget.AppCompatImageView implements ColorPickerDialog.OnColorChangedListener{
    private int mValue = Color.BLACK;
    private float mDensity = 0;
    ColorPickerDialog mDialog;
    private boolean mAlphaSliderEnabled = false;
    private boolean mHexValueEnabled = false;
    ColorPickerDialog.OnColorChangedListener mOnColorChangedListener = null;
    public ColorPickerImagePreview(Context context,int color) {
        super(context);
        init(color);
    }

    public ColorPickerImagePreview(Context context, AttributeSet attrs,int color) {
        super(context, attrs);
        init(color);
    }

    public ColorPickerImagePreview(Context context, AttributeSet attrs, int defStyleAttr,int color) {
        super(context, attrs, defStyleAttr);
        init(color);
    }
    private void init(int color) {
        mValue = color;
        mDensity = getContext().getResources().getDisplayMetrics().density;
        setBackgroundDrawable(new AlphaPatternDrawable((int) (5 * mDensity)));
        setImageBitmap(getPreviewBitmap());
    }

    private Bitmap getPreviewBitmap() {
        int d = (int) (mDensity * 31); //30dip
        int color = mValue;
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        int w = bm.getWidth();
        int h = bm.getHeight();
        int c = color;
        for (int i = 0; i < w; i++) {
            for (int j = i; j < h; j++) {
                c = (i <= 1 || j <= 1 || i >= w - 2 || j >= h - 2) ? Color.GRAY : color;
                bm.setPixel(i, j, c);
                if (i != j) {
                    bm.setPixel(j, i, c);
                }
            }
        }

        return bm;
    }
    public ColorPickerImagePreview updateColor (int color) {
        mValue = color;
        setImageBitmap(getPreviewBitmap());
        return this;
    }
    public int getColor() {
        return mValue;
    }
    /**
     * Toggle Alpha Slider visibility (by default it's disabled)
     *
     * @param enable
     */
    public void setAlphaSliderEnabled(boolean enable) {
        mAlphaSliderEnabled = enable;
    }

    /**
     * Toggle Hex Value visibility (by default it's disabled)
     *
     * @param enable
     */
    public void setHexValueEnabled(boolean enable) {
        mHexValueEnabled = enable;
    }

    public void showDialog(Bundle state) {
        mDialog = new ColorPickerDialog(getContext(), mValue);
        mDialog.setOnColorChangedListener(this);
        if (mAlphaSliderEnabled) {
            mDialog.setAlphaSliderVisible(true);
        }
        if (mHexValueEnabled) {
            mDialog.setHexValueEnabled(true);
        }
        if (state != null) {
            mDialog.onRestoreInstanceState(state);
        }
        mDialog.show();
    }

    @Override
    public void onColorChanged(int color) {
        updateColor(color);
        if(mOnColorChangedListener != null) {
            mOnColorChangedListener.onColorChanged(color);
        }

    }
    public void setOnColorChangedListener(ColorPickerDialog.OnColorChangedListener listener) {
        mOnColorChangedListener = listener;
    }
}
