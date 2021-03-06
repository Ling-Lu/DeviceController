package com.lulingfeng.viewpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class EditPreferenceView extends PreferenceItemView implements TextView.OnEditorActionListener,TextWatcher,View.OnClickListener{
    private final static String TAG = EditPreferenceView.class.getSimpleName();
    private EditText mEditText;
    protected Button mButton;
    private String mText;
    private String mDefaultValue;
    public EditPreferenceView(Context context) {
        super(context);
        init(context,null);
    }

    public EditPreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public EditPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EditPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    private String getDefaultValue(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceItemView);
        mDefaultValue = ta.getString(R.styleable.PreferenceItemView_DefaultValue);
        ta.recycle();
        return mDefaultValue;
    }
    private void init(Context context, AttributeSet attrs) {
        getDefaultValue(attrs);
        setClickable(false);
        mEditText = (EditText) findViewById(R.id.id_pre_edit);
        mButton = (Button) findViewById(R.id.id_pre_btn_set);
        updateKeyValue();
        mButton.setOnClickListener(this);
        mEditText.setOnEditorActionListener(this);
        mEditText.setVisibility(VISIBLE);
        mButton.setVisibility(VISIBLE);
//        mEditText.addTextChangedListener(this);
    }
    @Override
    public void updateKeyValue() {
        mText = mSharedPreferences.getString(getKey(),null);
        if(!mSharedPreferences.contains(getKey())) {
            mEditText.setText(mDefaultValue);
            if(getKey() != null) {
                onEditTextChanged();
            }
        } else {
            mEditText.setText(mText);
        }
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP) {
            if(actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                onEditTextChanged();
            }
        }
        return true;
    }
    protected boolean onEditTextChanged() {
        String text = mEditText.getText().toString();
        boolean isEditTextChanged = false;
        if(TextUtils.isEmpty(mText)) {
            if(!TextUtils.isEmpty(text)) {
                isEditTextChanged = true;
            }
        } else {
            if(!mText.equals(text)) {
                isEditTextChanged = true;
            }
        }
        if (isEditTextChanged && preferenceChange(this,text)) {
            mText = text;
            mEditor.putString(getKey(),mText);
            tryCommit(mEditor);
            return true;
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onEditTextChanged();
    }

    public String getValue() {
        return mText;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mEditText.setEnabled(enabled);
        mButton.setEnabled(enabled);
    }

    @Override
    public void onClick(View v) {
        onEditTextChanged();
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged: ");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String value = s.toString();
        Log.d(TAG, "onTextChanged: " + value);
        if(preferenceChange(this,value)) {
            Log.d(TAG,"PUT EDIT value" + value);
            mEditor.putString(getKey(),value);
            tryCommit(mEditor);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged: ");
    }
}
