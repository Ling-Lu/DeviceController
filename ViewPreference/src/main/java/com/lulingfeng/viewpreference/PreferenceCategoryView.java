package com.lulingfeng.viewpreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class PreferenceCategoryView extends LinearLayout {
    private final static String TAG = PreferenceCategoryView.class.getSimpleName();
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
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceItemView);
        mTitleStr = ta.getString(R.styleable.PreferenceItemView_Title);
        mSummaryStr = ta.getString(R.styleable.PreferenceItemView_Summary);
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
    }
    public void setSummary(String summary) {
        mSummaryTextView.setText(summary);
        if(!TextUtils.isEmpty(summary)) {
            mSummaryTextView.setVisibility(VISIBLE);
        } else {
            mSummaryTextView.setVisibility(GONE);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            updateDivider();
        }
    }
    private void updateDivider() {
        if (isDividerShouldShow()) {
            mDivider.setVisibility(VISIBLE);
        } else {

            int marginBottom = (int) getResources().getDimension(R.dimen.preference_category_margin_top);
            if (mSummaryTextView.getVisibility() == VISIBLE || mTitleTextView.getVisibility() == VISIBLE) {
                marginBottom = (int) (getResources().getDimension(R.dimen.preference_category_divider_margin_top) + getResources().getDimension(R.dimen.preference_category_margin_top));
            }
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) getLayoutParams();
            marginLayoutParams.setMargins(marginLayoutParams.leftMargin,marginLayoutParams.topMargin,marginLayoutParams.rightMargin
                    , marginBottom);
            mDivider.setVisibility(GONE);
        }
    }
    public void setChildrenEnable(boolean enable) {
        int childCnt = getChildCount();
        for (int i = 0; i < childCnt; i ++) {
            getChildAt(i).setEnabled(enable);
        }
    }
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setChildrenEnable(enabled);
    }
    private boolean hasVisiblePreferenceItem() {
        int childCnt = getChildCount();
        for (int i = 1; i < childCnt; i ++) {
            if (getChildAt(i).getVisibility() == VISIBLE) {
                return true;
            }
        }
        return false;
    }
    private boolean isDividerShouldShow() {
        return (mSummaryTextView.getVisibility() == VISIBLE || mTitleTextView.getVisibility() == VISIBLE) && hasVisiblePreferenceItem();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
