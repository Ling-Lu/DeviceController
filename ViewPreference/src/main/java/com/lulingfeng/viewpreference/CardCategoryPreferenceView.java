package com.lulingfeng.viewpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
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
    float mMargins = 0, mCardCornerRadius = 0;
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
        initCardView(attrs);
        mChildren.add(mCardView.hashCode());
        mChildren.add(mContent.hashCode());

        addView(mCardView,mLayoutParams);
        mCardView.addView(mContent);
    }
    private void initCardView(AttributeSet attrs) {
        mMargins = getResources().getDimension(R.dimen.preference_card_view_margins);
        mCardCornerRadius = getResources().getDisplayMetrics().density * 16;
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PreferenceItemView);
        mMargins = ta.getDimension(R.styleable.PreferenceItemView_Margins,mMargins);
        mCardCornerRadius = ta.getDimension(R.styleable.PreferenceItemView_CardCornerRadius, mCardCornerRadius);
        ta.recycle();

        int margins = (int) mMargins;
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mLayoutParams.setMargins(margins, margins, margins, margins);
        mCardView.setRadius(mCardCornerRadius);
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
        if(child.hashCode() != mContent.hashCode()) {
            if(mChildren.contains(child.hashCode()) == false) {
                removeView(child);
                mContent.addView(child);
            }
        }
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        mContent.removeView(view);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mContent.setEnabled(enabled);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
