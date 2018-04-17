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
 * Created by Administrator on 2018/2/8 0008.
 */

public class SwitchEditPreferenceView extends EditPreferenceView implements CompoundButton.OnCheckedChangeListener{
    private Switch mSwitch;
    private String mSwitch_Key;
    private String SWITCH_SUFFIX_KEY = "_preference_view_switch";
    private OnSwitchChangedListener mOnSwitchChangeListener;
    private boolean mDefaultValue;
    public SwitchEditPreferenceView(Context context) {
        this(context,null);
    }

    public SwitchEditPreferenceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwitchEditPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchEditPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private boolean getDefaultValue(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceItemView);
        mDefaultValue = ta.getBoolean(R.styleable.PreferenceItemView_DefaultValue,false);
        ta.recycle();
        return mDefaultValue;
    }
    private void init(AttributeSet attributeSet) {
        getDefaultValue(attributeSet);
        mSwitch_Key = getKey() + SWITCH_SUFFIX_KEY;
        mSwitch = (Switch) findViewById(R.id.id_pre_switch);
        mSwitch.setVisibility(VISIBLE);
        mButton.setVisibility(GONE);
        updateKeyValue();

        this.setOnClickListener(this);
    }

    @Override
    protected void updateKeyValue() {
        super.updateKeyValue();
        mSwitch.setOnCheckedChangeListener(null);
        if(getKey() != null) {
            if (!mSharedPreferences.contains(mSwitch_Key)) {
                setChecked(mDefaultValue);
                onCheckedChanged(mSwitch,mDefaultValue);
            } else {
                setChecked(mSharedPreferences.getBoolean(mSwitch_Key, false));
            }
        }
        mSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView != null && !buttonView.isPressed()) {
            return;
        }
        if(mOnSwitchChangeListener == null
                || (mOnSwitchChangeListener != null && mOnSwitchChangeListener.onSwitchChanged(mSwitch_Key,isChecked))) {
            mEditor.putBoolean(mSwitch_Key,isChecked);
            tryCommit(mEditor);
        } else {
            mSwitch.setChecked(!isChecked);
        }
        onEditTextChanged();
    }
    public void setOnCheckedChangeListener(OnSwitchChangedListener checkedChangeListener) {
        mOnSwitchChangeListener = checkedChangeListener;
    }
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mSwitch.setEnabled(enabled);
    }
    public String getSwitch_Key () {
        return mSwitch_Key;
    }

    public boolean isChecked() {
        return mSwitch.isChecked();
    }
    public void setChecked(boolean isChecked) {
        if(mSwitch != null) {
            mSwitch.setChecked(isChecked);
        }
    }
    @Override
    public void onClick(View v) {
        onCheckedChanged(null,!mSwitch.isChecked());
    }
}
