package com.lulingfeng.viewpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class ColorPickerPreferenceView extends PreferenceItemView implements ColorPickerDialog.OnColorChangedListener{
    private final static String TAG = ColorPickerPreferenceView.class.getSimpleName();
    private ViewGroup mPreviewG;
    private ColorPickerImagePreview mColorPickerImagePreview;
    private int mDefaultValue;
    public ColorPickerPreferenceView(Context context) {
        super(context);
        init(null);
    }

    public ColorPickerPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ColorPickerPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private int getDefaultValue(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceItemView);
        mDefaultValue = ta.getInt(R.styleable.PreferenceItemView_DefaultValue,Color.BLACK);
        Log.d(TAG,"mDefaultValue" + mDefaultValue);
        ta.recycle();
        return mDefaultValue;
    }
    private void init(AttributeSet attrs) {
        getDefaultValue(attrs);
        int color = mSharedPreferences.getInt(getKey(),Color.BLACK);
        if (!mSharedPreferences.contains(getKey())) {
            mColorPickerImagePreview = new ColorPickerImagePreview(getContext(),mDefaultValue);
            if(getKey() != null) {
                onColorChanged(mDefaultValue);
                mColorPickerImagePreview.setOnColorChangedListener(this);
            }
        } else {
            mColorPickerImagePreview = new ColorPickerImagePreview(getContext(),color);
        }
        mColorPickerImagePreview.setAlphaSliderEnabled(true);
        mColorPickerImagePreview.setHexValueEnabled(true);
        mPreviewG = (ViewGroup) findViewById(R.id.id_pre_preview);
        mPreviewG.removeAllViews();
        mPreviewG.addView(mColorPickerImagePreview);
        mPreviewG.setVisibility(VISIBLE);
    }
    @Override
    protected void updateKeyValue() {
        if (!mSharedPreferences.contains(getKey())) {
            mColorPickerImagePreview.updateColor(mDefaultValue);
            if(getKey() != null) {
                onColorChanged(mDefaultValue);
                mColorPickerImagePreview.setOnColorChangedListener(this);
            }
        } else {
            int color = mSharedPreferences.getInt(getKey(),Color.BLACK);
            mColorPickerImagePreview.updateColor(color);
        }
    }
    @Override
    public void onColorChanged(int color) {
        Log.d(TAG,"new Color #" + Integer.toHexString(color));
        if(preferenceChange(this,color)) {
            mEditor.putInt(getKey(), color);
            tryCommit(mEditor);
        } else {
            Log.d(TAG,"should not happen");
        }
    }

    @Override
    public void onClick(View v) {
        mColorPickerImagePreview.showDialog(null);
    }

    protected int getColor(String hexStr) {
        int colorInt;
        String mHexDefaultValue = hexStr;
        if (mHexDefaultValue != null && mHexDefaultValue.startsWith("#")) {
            colorInt = convertToColorInt(mHexDefaultValue);
            return colorInt;
        } else {
            return Color.BLACK;
        }
    }
    /**
     * For custom purposes. Not used by ColorPickerPreferrence
     *
     * @param argb
     * @throws NumberFormatException
     * @author Unknown
     */
    public static int convertToColorInt(String argb) throws IllegalArgumentException {

        if (!argb.startsWith("#")) {
            argb = "#" + argb;
        }

        return Color.parseColor(argb);
    }
}
