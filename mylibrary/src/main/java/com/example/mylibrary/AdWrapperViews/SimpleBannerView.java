package com.example.mylibrary.AdWrapperViews;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.mylibrary.R;

public class SimpleBannerView extends RelativeLayout {

    public SimpleBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(R.drawable.adloading);
        setGravity(Gravity.CENTER);
    }


}
