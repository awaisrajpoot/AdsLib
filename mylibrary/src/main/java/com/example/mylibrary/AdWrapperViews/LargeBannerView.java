package com.example.mylibrary.AdWrapperViews;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mylibrary.R;

public class LargeBannerView extends RelativeLayout {

    public LargeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(R.drawable.adloading);
        setGravity(Gravity.CENTER);
    }

}
