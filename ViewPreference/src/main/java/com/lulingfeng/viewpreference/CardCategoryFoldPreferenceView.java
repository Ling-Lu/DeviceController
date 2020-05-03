package com.lulingfeng.viewpreference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CardCategoryFoldPreferenceView extends CardCategoryPreferenceView {
    private ImageView mFold;
    public CardCategoryFoldPreferenceView(Context context) {
        this(context,null);
    }

    public CardCategoryFoldPreferenceView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CardCategoryFoldPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFold = (ImageView) findViewById(R.id.id_pre_category_extend);
        mFold.setVisibility(VISIBLE);
    }
}
