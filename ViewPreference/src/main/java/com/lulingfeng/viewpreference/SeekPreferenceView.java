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
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceView);
        mDefaultValue = ta.getInt(R.styleable.PreferenceView_DefaultValue,1);
        ta.recycle();
    }
    private void init(Context context,AttributeSet attrs) {
        getAttrs(attrs);
        mBubbleSeekBar = (BubbleSeekBar) findViewById(R.id.id_pre_seek_bar);
        mBubbleSeekBar.setVisibility(VISIBLE);
        if (mSharedPreferences.contains(getKey())) {
            mDefaultValue = mSharedPreferences.getInt(getKey(),1);
            mBubbleSeekBar.setProgress(mDefaultValue);
            mBubbleSeekBar.setOnProgressChangedListener(this);
        } else {
            mBubbleSeekBar.setOnProgressChangedListener(this);
            mBubbleSeekBar.setProgress(mDefaultValue);
        }
        setSummary(String.valueOf(mDefaultValue));
    }

    @Override
    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i, float v) {
        Log.d(TAG, "onProgressChanged: ");
        if(preferenceChange(this,i)) {
            setSummary(String.valueOf(i));
            mEditor.putInt(getKey(),i);
            tryCommit(mEditor);
        };
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
    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int i, float v) {
    }

    @Override
    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int i, float v) {
    }
}
