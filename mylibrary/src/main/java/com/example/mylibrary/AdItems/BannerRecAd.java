package com.example.mylibrary.AdItems;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chartboost.sdk.ads.Banner;
import com.chartboost.sdk.callbacks.BannerCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.example.mylibrary.AdWrapperViews.RectBannerView;
import com.example.mylibrary.LibHelpers.AdUnitHelper;
import com.example.mylibrary.LibHelpers.ServerAdConstants;
import com.example.mylibrary.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;


public class BannerRecAd {

    private AppCompatActivity mActivity;
    private RectBannerView adContainer;

    private AdView admobAdView;
    private com.facebook.ads.AdView fbAdView;
    private Banner chartBoostBanner;

    private AdUnitHelper adUnitHelper;

    private boolean admobStatus = false;
    private boolean fbStatus = false;
    private boolean cbStatus = false;
    private boolean startAppStatus = false;
    private boolean splashIsGone = false;

    public BannerRecAd(AppCompatActivity mActivity, AdUnitHelper adUnitHelper, RectBannerView adContainer){
        this.mActivity = mActivity;
        this.adUnitHelper = adUnitHelper;

        this.adContainer = adContainer;
        getContainer();
    }

    private void getContainer(){
        // Find the Ad Container

        adRequestCaller();
    }

    public void adRequestCaller(){
        if (adUnitHelper.getAdmobStatus().equals(ServerAdConstants.STATUS_OK)){
            setAdmobRectangleBanner();
        }
        else if (adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){
            setFbRectangleBanner();
        }
        else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
            setChartBoostRecBanner();
        }
        else {
            adContainer.setVisibility(View.GONE);
        }
    }

    private void checkLogValues(){
        Log.e(ServerAdConstants.AD_LOG_TAG, "admob rect banner status : " + adUnitHelper.getAdmobStatus());
        Log.e(ServerAdConstants.AD_LOG_TAG, "fb rect banner status : " + adUnitHelper.getFbStatus());
        Log.e(ServerAdConstants.AD_LOG_TAG, "chartboost rect banner status : " + adUnitHelper.getChartStatus());
    }

    private void setAdmobRectangleBanner(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling admob banner");
        admobAdView = new AdView(mActivity);
        admobAdView.setAdSize(com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE);
        admobAdView.setAdUnitId(adUnitHelper.getAdmobBanner());
        AdRequest adRequest = new AdRequest.Builder().build();
        admobAdView.loadAd(adRequest);

        adContainer.removeAllViews();
        adContainer.addView(admobAdView);

        admobAdView.setAdListener(new com.google.android.gms.ads.AdListener(){

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                admobStatus = true;
                Log.e(ServerAdConstants.AD_LOG_TAG, "admob rec banner loaded");
                checkLogValues();
                showRecBanner(splashIsGone);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                admobStatus = false;
                Log.e(ServerAdConstants.AD_LOG_TAG, "admob rec banner not loaded");
                if (adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){
                    setFbRectangleBanner();
                }
                else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                    setChartBoostRecBanner();
                }
                else {
                    setAdmobRectangleBanner();
                }
            }
        });
    }

    private void setFbRectangleBanner(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling facebook banner");
        fbAdView = new com.facebook.ads.AdView(mActivity,
                adUnitHelper.getFbRec(),//facebook rectangle banner id
                AdSize.RECTANGLE_HEIGHT_250);

        // Request an ad
        fbAdView.loadAd(fbAdView.buildLoadAdConfig()
                .withAdListener(new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        fbStatus = false;
                        Log.e(ServerAdConstants.AD_LOG_TAG, "facebook rec banner not loaded");
                        if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                            setChartBoostRecBanner();
                        }
                        else {
                            setAdmobRectangleBanner();
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        fbStatus = true;
                        Log.e(ServerAdConstants.AD_LOG_TAG, "fb banner loaded");
                        checkLogValues();
                        showRecBanner(splashIsGone);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                })
                .build());
    }

    private void setChartBoostRecBanner(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling chartBoost banner");

        BannerCallback bannerCallback = new BannerCallback() {
            @Override
            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {

                Log.e(ServerAdConstants.AD_LOG_TAG, "chartBoost rec banner loaded");

                if (chartBoostBanner != null) {
                    cbStatus = true;
                    Log.e(ServerAdConstants.AD_LOG_TAG, "chartBoost banner loaded and cached");
                    checkLogValues();
                    showRecBanner(splashIsGone);
                }else {
                    Log.e(ServerAdConstants.AD_LOG_TAG, "chartBoost rec banner not loaded");
                    setAdmobRectangleBanner();
                }

            }

            @Override
            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

            }

            @Override
            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {
                Log.e(ServerAdConstants.AD_LOG_TAG,"on cb rec banner show");

                if (showError!=null){
                    if (showError.getCode().equals(ShowError.Code.NO_CACHED_AD)){
                        Log.e(ServerAdConstants.AD_LOG_TAG,"rec cb banner no cache");
                        cbStatus = false;
                        setAdmobRectangleBanner();
                    }

                }
            }

            @Override
            public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

            }

            @Override
            public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

            }

        };

        chartBoostBanner = new Banner(mActivity, "start", Banner.BannerSize.MEDIUM,
                bannerCallback,null);

    }


    public void showRecBanner(boolean splashEnd){
        splashIsGone = splashEnd;
        adContainer.removeAllViews();

        if (splashIsGone){
            if (admobStatus){
                adContainer.addView(admobAdView);
                Log.e(ServerAdConstants.AD_LOG_TAG,"admob banner showed");
            }
            else if (fbStatus){
                // Add the ad view to your activity layout
                adContainer.addView(fbAdView);
                Log.e(ServerAdConstants.AD_LOG_TAG,"fb banner showed");
            }
            else if (cbStatus){
                Log.e(ServerAdConstants.AD_LOG_TAG,"cb banner showed");
                adContainer.addView(chartBoostBanner);
                chartBoostBanner.cache();
                chartBoostBanner.show();
            }
            else {
                Log.e(ServerAdConstants.AD_LOG_TAG,"banner pending");
            }
        }
    }

}
