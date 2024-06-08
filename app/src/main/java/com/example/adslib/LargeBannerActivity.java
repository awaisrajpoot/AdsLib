package com.example.adslib;

import android.annotation.SuppressLint;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;

import com.example.adslib.databinding.ActivityLargeBannerBinding;
import com.example.mylibrary.AdItems.LargeBannerAd;
import com.example.mylibrary.AdItems.SimpleBannerAd;
import com.example.mylibrary.AdWrapperViews.LargeBannerView;
import com.example.mylibrary.AdsInitializer;
import com.example.mylibrary.LibHelpers.AdUnitHelper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LargeBannerActivity extends AppCompatActivity implements AdsInitializer.AdsInitCompleteListener {

    String fileName = "";
    AdsInitializer adsInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileName = getIntent().getStringExtra(MainActivity.FILE_KEY);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_large_banner);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!fileName.isEmpty()){
            Log.i("file_data","SecondActivity : file name is : " + fileName);
            adsInitializer = new AdsInitializer(LargeBannerActivity.this,
                    AppConstants.adsUrl, AppConstants.metaLink,fileName);
        }

    }


    @Override
    public void onAdsInitComplete(AdUnitHelper adUnitHelper) {
        LargeBannerView largeBannerView_1 = findViewById(R.id.br1_large_banner);
        new LargeBannerAd(this, adUnitHelper, largeBannerView_1);

        LargeBannerView largeBannerView_2 = findViewById(R.id.br2_large_banner);
        new LargeBannerAd(this, adUnitHelper, largeBannerView_2);
    }
}