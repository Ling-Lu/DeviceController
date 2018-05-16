package com.lulingfeng.viewpreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import com.xw.repo.BubbleSeekBar;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class SeekPreferenceView extends PreferenceItemView implements BubbleSeekBar.OnProgressChangedListener{
    private final static String TAG = SeekPreferenceView.class.getSimpleName();
    private BubbleSeekBar mBubbleSeekBar;
    private int mDefaultValue;
    public SeekPreferenceView(Context context) {
        super(context);
        init(context,null);
    }

    public SeekPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SeekPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SeekPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    private void getAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceItemView);
        mDefaultValue = ta.getInt(R.styleable.PreferenceItemView_DefaultValue,1);
        ta.recycle();
    }
    private void init(Context context,AttributeSet attrs) {
        getAttrs(attrs);
        mBubbleSeekBar = (BubbleSeekBar) findViewById(R.id.id_pre_seek_bar);
        mBubbleSeekBar.setVisibility(VISIBLE);
        updateKeyValue();
        setSummary(String.valueOf(mDefaultValue));
    }
    @Override
    public void updateKeyValue() {
        mBubbleSeekBar.setOnProgressChangedListener(null);
        if(getKey() != null) {
            if (mSharedPreferences.contains(getKey())) {
                int value = mSharedPreferences.getInt(getKey(), mDefaultValue);
                mBubbleSeekBar.setProgress(value);
            } else {
                mBubbleSeekBar.setProgress(mDefaultValue);
                onProgressChanged(mBubbleSeekBar, mDefaultValue, 0, false);
            }
        }
        mBubbleSeekBar.setOnProgressChangedListener(this);
    }
    public int getValue() {
        return mBubbleSeekBar.getProgress();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mBubbleSeekBar.setEnabled(enabled);
    }

    @Override
    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i, float v, boolean b) {
        Log.d(TAG, "onProgressChanged: ");
        if(preferenceChange(this,i)) {
            setSummary(String.valueOf(i));
            mEditor.putInt(getKey(),i);
            tryCommit(mEditor);
        }
    }

    @Override
    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int i, float v) {
    }

    @Override
    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int i, float v, boolean b) {

    }
}
