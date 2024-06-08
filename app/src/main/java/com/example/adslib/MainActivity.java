package com.example.adslib;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.mylibrary.AdItems.AppInterstitialAd;
import com.example.mylibrary.AdItems.BannerRecAd;
import com.example.mylibrary.AdWrapperViews.RectBannerView;
import com.example.mylibrary.AdsInitializer;
import com.example.mylibrary.LibHelpers.AdUnitHelper;
import com.example.mylibrary.LibHelpers.FileNameCreator;

public class MainActivity extends AppCompatActivity implements AdsInitializer.AdsInitCompleteListener, View.OnClickListener {

    AdsInitializer adsInitializer;

    String fileName = "";
    FileNameCreator fileNameCreator;

    TextView simpleBannerBtn, largeBannerBtn;

    public static String FILE_KEY = "ads_file_key";

    AppInterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        EdgeToEdge.enable(this);

        simpleBannerBtn = findViewById(R.id.simpleBannerBtn);
        largeBannerBtn = findViewById(R.id.largeBannerBtn);

        simpleBannerBtn.setOnClickListener(this);
        largeBannerBtn.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fileNameCreator = new FileNameCreator(getPreferences(MODE_PRIVATE));
        fileNameCreator.checkAndCreateFileName();
        fileName = fileNameCreator.getFileName();


        Log.i("file_data","MainActivity : file name is : " + fileName);
        adsInitializer = new AdsInitializer(MainActivity.this,
                AppConstants.adsUrl, AppConstants.metaLink,fileName);
    }

    @Override
    public void onAdsInitComplete(AdUnitHelper adUnitHelper) {
        //now you can your ads

        RectBannerView rectBannerView = findViewById(R.id.rect_banner_view);

        BannerRecAd bannerRecAd = new BannerRecAd(this, adUnitHelper, rectBannerView);
        bannerRecAd.showRecBanner(true);

        interstitialAd = new AppInterstitialAd(MainActivity.this, adUnitHelper);
        interstitialAd.loadAd();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id==R.id.simpleBannerBtn){
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra(FILE_KEY, fileName);
            startActivity(intent);

            interstitialAd.showLoadedInterstitial();
        }
        else if (id==R.id.largeBannerBtn) {
            Intent intent = new Intent(this, LargeBannerActivity.class);
            intent.putExtra(FILE_KEY, fileName);
            startActivity(intent);

            interstitialAd.showLoadedInterstitial();
        }
    }
}