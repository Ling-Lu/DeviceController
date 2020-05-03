package com.lulingfeng.viewpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class PreferenceItemView extends RelativeLayout implements View.OnClickListener {
    private final static String TAG = PreferenceItemView.class.getSimpleName();
    Context mContext;
    SharedPreferences.Editor mEditor;
    SharedPreferences mSharedPreferences;
    private String mKey;
    private ViewGroup mPreviewVg;
    private ImageView mIconIv;
    private TextView mTitleTv;
    protected TextView mSummaryTv;
    protected String mTitleStr;
    protected String mSummaryStr;
    protected String mKeyStr;
    protected boolean mIsEnable;
    private boolean mIsFixedKey;
    private int mIconResId;
    private int mTitleColor,mSummaryColor;
    private OnPreferenceChangedListener mOnPreferenceChangedListener;
    public PreferenceItemView(Context context) {
        super(context);
        init(context,null);
    }
    public PreferenceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public PreferenceItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PreferenceItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    private void getAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceItemView);
        mTitleStr = ta.getString(R.styleable.PreferenceItemView_Title);
        mSummaryStr = ta.getString(R.styleable.PreferenceItemView_Summary);
        mKeyStr = ta.getString(R.styleable.PreferenceItemView_Key);
        mIsEnable = ta.getBoolean(R.styleable.PreferenceItemView_Enable,true);
        mIsFixedKey = ta.getBoolean(R.styleable.PreferenceItemView_FixedKey,true);
        mIconResId = ta.getResourceId(R.styleable.PreferenceItemView_Icon,0);
        mTitleColor = ta.getColor(R.styleable.PreferenceItemView_TitleColor, Color.TRANSPARENT);
        mSummaryColor = ta.getColor(R.styleable.PreferenceItemView_SummaryColor, Color.TRANSPARENT);
        ta.recycle();
    }
    private void init(Context context,AttributeSet attrs) {
        mContext = context;
        getAttrs(attrs);
        inflate(mContext, R.layout.preference_item,this);
        if (!TextUtils.isEmpty(mKeyStr) && mIsFixedKey) mKey = mKeyStr;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        mEditor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();

        mIconIv = (ImageView) findViewById(R.id.id_pre_icon);
        mPreviewVg = (ViewGroup) findViewById(R.id.id_pre_preview);
        mTitleTv = (TextView) findViewById(R.id.id_pre_title);
        mSummaryTv = (TextView) findViewById(R.id.id_pre_summary);

        setIcon(mIconResId);
        if (mTitleColor != Color.TRANSPARENT) {
            mTitleTv.setTextColor(mTitleColor);
        }
        if(mSummaryColor != Color.TRANSPARENT) {
            mSummaryTv.setTextColor(mSummaryColor);
        }
        setTitle(mTitleStr);
        setSummary(mSummaryStr);

        this.setOnClickListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setEnabled(mIsEnable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setTitle(int titleId) {
        setTitle(getResources().getString(titleId));
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
        if(TextUtils.isEmpty(title)) {
            mTitleTv.setVisibility(GONE);
        } else {
            mTitleTv.setVisibility(VISIBLE);
        }
    }
    public void setSummary(int summaryId) {
        setSummary(getResources().getString(summaryId));
    }
    public void setSummary(String summary) {
        mSummaryTv.setText(summary);
        if(TextUtils.isEmpty(summary)) {
            mSummaryTv.setVisibility(GONE);
        } else {
            mSummaryTv.setVisibility(VISIBLE);
        }
    }
    public void setPreview(View v) {
        mPreviewVg.removeAllViews();
        if(v != null) {
            mPreviewVg.addView(v);
            mPreviewVg.setVisibility(VISIBLE);
        } else {
            mPreviewVg.setVisibility(GONE);
        }
    }
    public void setIcon(int srcId) {
        if(srcId != 0) {
            mIconIv.setBackgroundResource(srcId);
            mIconIv.setVisibility(VISIBLE);
        } else {
            mIconIv.setVisibility(GONE);
        }
    }
    public void setIcon(Drawable icon) {
        if (icon != null) {
            mIconIv.setVisibility(VISIBLE);
        } else {
            mIconIv.setVisibility(GONE);
        }
        mIconIv.setImageDrawable(icon);
    }
    public void setKey(String key) {
        mKey = key;
        updateKeyValue();
    }
    public void updateKeyValue(){}
    public void tryCommit(SharedPreferences.Editor editor) {
        try {
            editor.apply();
        } catch (AbstractMethodError unused) {
            // The app injected its own pre-Gingerbread
            // SharedPreferences.Editor implementation without
            // an apply method.
            editor.commit();
        }
    }
    public String getKey() {
        return mKey;
    }
    public String getFixedKey() {
        return mKeyStr;
    }
    @Override
    public void onClick(View v) {
        if(this == v) {
            preferenceChange(this,null);
        }
    }
    public void setOnPreferenceChangeListener(OnPreferenceChangedListener onPreferenceChangeListener) {
        mOnPreferenceChangedListener = onPreferenceChangeListener;
    }

    protected void putValue() {
        mEditor.putString(getKey(),mSummaryTv.getText().toString());
        tryCommit(mEditor);
    }
    public boolean preferenceChange(PreferenceItemView preferenceItemView, Object newValue) {
        if(mOnPreferenceChangedListener == null || (mOnPreferenceChangedListener != null && mOnPreferenceChangedListener.onPreferenceChange(preferenceItemView,newValue))) {
            return true;
        }
        return false;
    }

    public interface OnPreferenceChangedListener {
        boolean onPreferenceChange(PreferenceItemView preferenceItemView, Object newValue);
    }
    public interface OnSwitchChangedListener {
        boolean onSwitchChanged(String key, boolean isChecked);
    }
}
