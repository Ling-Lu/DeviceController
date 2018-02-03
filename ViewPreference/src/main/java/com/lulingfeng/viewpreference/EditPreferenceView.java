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
    private Button mButton;
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
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceView);
        mDefaultValue = ta.getString(R.styleable.PreferenceView_DefaultValue);
        ta.recycle();
        return mDefaultValue;
    }
    private void init(Context context, AttributeSet attrs) {
        getDefaultValue(attrs);
        setClickable(false);
        mEditText = (EditText) findViewById(R.id.id_pre_edit);
        mButton = (Button) findViewById(R.id.id_pre_btn_set);
        mButton.setOnClickListener(this);
        mEditText.setOnEditorActionListener(this);
        mText = mSharedPreferences.getString(getKey(),null);
        if(!mSharedPreferences.contains(getKey())) {
            mEditText.setText(mDefaultValue);
        } else {
            mEditText.setText(mText);
        }
        mEditText.setVisibility(VISIBLE);
        mButton.setVisibility(VISIBLE);
//        mEditText.addTextChangedListener(this);
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
                if(TextUtils.equals(mEditText.getText().toString(),mText) == false) {
                    mText = mEditText.getText().toString();
                    if (preferenceChange(this,mText)) {
                        mEditor.putString(getKey(),mText);
                        tryCommit(mEditor);
                    }
                } else {
                    mEditText.setText(mText);
                }
            }
        }
        return false;
    }
    private void onEditTextChanged(String text) {
        mText = text;
        if (preferenceChange(this,mText)) {
            mEditor.putString(getKey(),mText);
            tryCommit(mEditor);
        }
    }
    public String getValue() {
        onClick(null);
        return mText;
    }
    @Override
    public void onClick(View v) {
        String text = mEditText.getText().toString();
        if(TextUtils.isEmpty(mText)) {
            if(!TextUtils.isEmpty(text)) {
                onEditTextChanged(text);
            }
        } else {
            if(!mText.equals(text)) {
                onEditTextChanged(text);
            }
        }
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
        };
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged: ");
    }
}
