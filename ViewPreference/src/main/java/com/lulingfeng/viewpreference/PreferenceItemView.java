package com.lulingfeng.viewpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lulingfeng.viewpreference.R;


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
    private TextView mSummaryTv;
    protected String mTitleStr;
    protected String mSummaryStr;
    protected String mKeyStr;
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
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceView);
        mTitleStr = ta.getString(R.styleable.PreferenceView_Title);
        mSummaryStr = ta.getString(R.styleable.PreferenceView_Summary);
        mKeyStr = ta.getString(R.styleable.PreferenceView_Key);
        ta.recycle();
    }
    private void init(Context context,AttributeSet attrs) {
        mContext = context;
        getAttrs(attrs);
        inflate(mContext, R.layout.preference_item,this);
        mKey = (String) getTag();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        mEditor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();

        mIconIv = (ImageView) findViewById(R.id.id_pre_icon);
        mPreviewVg = (ViewGroup) findViewById(R.id.id_pre_preview);
        mTitleTv = (TextView) findViewById(R.id.id_pre_title);
        mSummaryTv = (TextView) findViewById(R.id.id_pre_summary);

        if (!TextUtils.isEmpty(mKeyStr)) mKey = mKeyStr;
        setTitle(mTitleStr);
        setSummary(mSummaryStr);
        this.setOnClickListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
    public void setmIcon(int srcId) {
        if(srcId != 0) {
            mIconIv.setBackgroundResource(srcId);
            mIconIv.setVisibility(VISIBLE);
        } else {
            mIconIv.setVisibility(GONE);
        }
    }
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
}
