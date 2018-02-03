package com.lulingfeng.viewpreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class PreferenceCategoryView extends LinearLayout {
    private final static String TAG = PreferenceItemView.class.getSimpleName();
    private String mTitleStr;
    private String mSummaryStr;
    private TextView mTitleTextView;
    private TextView mSummaryTextView;
    private View mDivider;
    public PreferenceCategoryView(Context context) {
        super(context);
        init();
    }

    public PreferenceCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        init();
    }

    public PreferenceCategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PreferenceCategoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceView);
        mTitleStr = ta.getString(R.styleable.PreferenceView_Title);
        mSummaryStr = ta.getString(R.styleable.PreferenceView_Summary);
        ta.recycle();
    }
    private void init () {
        setOrientation(LinearLayout.VERTICAL);
        inflate(getContext(), R.layout.preference_category,this);
        mTitleTextView = (TextView) findViewById(R.id.id_pre_category_title);
        mSummaryTextView = (TextView) findViewById(R.id.id_pre_category_summary);
        mDivider = findViewById(R.id.id_pre_category_divider);

        setTitle(mTitleStr);
        setSummary(mSummaryStr);
    }
    public void setTitle(String title) {
        mTitleTextView.setText(title);
        if(!TextUtils.isEmpty(title)) {
            mTitleTextView.setVisibility(VISIBLE);
        } else {
            mTitleTextView.setVisibility(GONE);
        }
        updateDivider();
    }
    public void setSummary(String summary) {
        mSummaryTextView.setText(summary);
        if(!TextUtils.isEmpty(summary)) {
            mSummaryTextView.setVisibility(VISIBLE);
        } else {
            mSummaryTextView.setVisibility(GONE);
        }
        updateDivider();
    }

    private void updateDivider() {
        if (isCategoryShouldShow()) {
            mDivider.setVisibility(VISIBLE);
        } else {
            mDivider.setVisibility(GONE);
        }
    }
    private boolean isCategoryShouldShow() {
        return mSummaryTextView.getVisibility() == VISIBLE || mTitleTextView.getVisibility() == VISIBLE;
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }
}
