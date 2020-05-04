package com.lulingfeng.viewpreference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CardCategoryFoldPreferenceView extends CardCategoryPreferenceView {
    private ImageView mFold;
    private boolean mIsFoldEnabled;
    public CardCategoryFoldPreferenceView(Context context) {
        this(context,null);
    }

    public CardCategoryFoldPreferenceView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CardCategoryFoldPreferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFold = (ImageView) findViewById(R.id.id_pre_category_extend);
        enableFold(true);
    }
    public void enableFold(boolean enable) {
        mIsFoldEnabled = enable;
        mFold.setVisibility(enable ? VISIBLE : GONE);
    }
    public boolean isFoldEnabled() {
        return mIsFoldEnabled;
    }
}
