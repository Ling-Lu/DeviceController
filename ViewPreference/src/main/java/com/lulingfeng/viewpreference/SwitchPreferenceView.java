package com.lulingfeng.viewpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class SwitchPreferenceView extends PreferenceItemView implements CompoundButton.OnCheckedChangeListener{
    private final static String TAG = PreferenceItemView.class.getSimpleName();
    private Switch mSwitch;
    private boolean mDefaultValue;
    public SwitchPreferenceView(Context context) {
        super(context);
        init(context,null);
    }

    public SwitchPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SwitchPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    private boolean getDefaultValue(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceItemView);
        mDefaultValue = ta.getBoolean(R.styleable.PreferenceItemView_DefaultValue,false);
        ta.recycle();
        return mDefaultValue;
    }
    private void init(Context context,AttributeSet attrs) {
        getDefaultValue(attrs);
        mSwitch = (Switch) findViewById(R.id.id_pre_switch);
        mSwitch.setVisibility(VISIBLE);
        updateKeyValue();
        setSummary(isChecked() ? mContext.getString(R.string.on) : mContext.getString(R.string.off));
        this.setOnClickListener(this);
    }

    @Override
    protected void updateKeyValue() {
        if (mSharedPreferences.contains(getKey())) {
            mDefaultValue = mSharedPreferences.getBoolean(getKey(), false);
            setChecked(mDefaultValue);
        } else {
            if(getKey() != null) {
                onCheckedChanged(mSwitch,mDefaultValue);
                mSwitch.setOnCheckedChangeListener(this);
            }
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView != null && !buttonView.isPressed()) {
            return;
        }
        if (preferenceChange(this,isChecked)) {
            mEditor.putBoolean(getKey(),isChecked);
            tryCommit(mEditor);
            setChecked(isChecked);
        } else {
            setChecked(!isChecked);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mSwitch.setEnabled(enabled);
    }

    public boolean isChecked() {
        return mSwitch.isChecked();
    }
    public void setChecked(boolean isChecked) {
        mSwitch.setChecked(isChecked);
        setSummary(isChecked ? mContext.getString(R.string.on) : mContext.getString(R.string.off));
    }
    @Override
    public void onClick(View v) {
        onCheckedChanged(null,!mSwitch.isChecked());
    }

}
