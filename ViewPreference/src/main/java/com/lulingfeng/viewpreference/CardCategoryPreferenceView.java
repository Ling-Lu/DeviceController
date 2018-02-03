package com.lulingfeng.viewpreference;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/23 0023.
 */

public class CardCategoryPreferenceView extends CardView {
    private final static String TAG = CardCategoryPreferenceView.class.getSimpleName();
    PreferenceCategoryView mContent;
    CardView mCardView;
    List<Integer> mChildren = new ArrayList<>();
    LayoutParams mLayoutParams;
    int mMargins = 0;
    public CardCategoryPreferenceView(Context context) {
        this(context,null);
    }

    public CardCategoryPreferenceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CardCategoryPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mCardView = (CardView) inflate(getContext(), R.layout.preference_cardview_category,null);
        mContent = new PreferenceCategoryView(getContext(),attrs);
        mMargins = (int) getResources().getDimension(R.dimen.preference_card_view_margins);
        mLayoutParams = (LayoutParams) new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mLayoutParams.setMargins(mMargins, mMargins, mMargins, mMargins);
        mChildren.add(mCardView.hashCode());
        mChildren.add(mContent.hashCode());

        addView(mCardView,mLayoutParams);
        mCardView.addView(mContent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        Log.d(TAG, "onViewAdded: ");
        if(child.hashCode() != mContent.hashCode()) {
            if(mChildren.contains(child.hashCode()) == false) {
                removeView(child);
                mContent.addView(child);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: ");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate: ");
    }
}
