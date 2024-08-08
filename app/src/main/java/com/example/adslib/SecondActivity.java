package com.example.adslib;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mylibrary.AdItems.SimpleBannerAd;
import com.example.mylibrary.AdWrapperViews.SimpleBannerView;
import com.example.mylibrary.AdsInitializer;
import com.example.mylibrary.LibHelpers.AdUnitHelper;

public class SecondActivity extends AppCompatActivity implements AdsInitializer.AdsInitCompleteListener {

    String fileName = "";
    AdsInitializer adsInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileName = getIntent().getStringExtra(MainActivity.FILE_KEY);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!fileName.isEmpty()){
            Log.i("file_data","SecondActivity : file name is : " + fileName);
            adsInitializer = new AdsInitializer(SecondActivity.this,
                    AppConstants.adsUrl, AppConstants.metaLink,fileName);
        }

    }

    @Override
    public void onAdsInitComplete(AdUnitHelper adUnitHelper) {
        SimpleBannerView simpleBannerView = findViewById(R.id.simple_banner_view);
        new SimpleBannerAd(this, adUnitHelper, simpleBannerView);

        SimpleBannerView simpleBannerView_2 = findViewById(R.id.simple_banner_view_2);
        new SimpleBannerAd(this, adUnitHelper, simpleBannerView_2);
    }
}