package com.example.mylibrary.AdWrapperViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.example.mylibrary.R;

public class RectBannerView extends RelativeLayout {
    public RectBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(R.drawable.adloading);
        setGravity(Gravity.CENTER);
    }
}
