package com.example.mylibrary.AdItems;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chartboost.sdk.callbacks.BannerCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.example.mylibrary.AdWrapperViews.SimpleBannerView;
import com.example.mylibrary.LibHelpers.AdUnitHelper;
import com.example.mylibrary.LibHelpers.ServerAdConstants;
import com.example.mylibrary.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;


public class SimpleBannerAd {

    private AppCompatActivity mActivity;
    private SimpleBannerView adContainer;

    private AdView admobAdView;
    private com.facebook.ads.AdView fbAdView;
    private BannerView unityBannerView;
    private com.chartboost.sdk.ads.Banner chartBoostBanner;
    private AdUnitHelper adUnitHelper;


    public SimpleBannerAd(AppCompatActivity mActivity, AdUnitHelper adUnitHelper, SimpleBannerView adContainer){
        this.mActivity = mActivity;
        this.adUnitHelper = adUnitHelper;
        this.adContainer = adContainer;

        getContainer();//calling ad container
    }

    private void getContainer(){
        // Find the Ad Container
        adContainer.removeAllViews();

        adRequestCaller();
    }

    private void checkLogValues(){
        Log.e("app_ads", "admob banner status : " + adUnitHelper.getAdmobStatus());
        Log.e("app_ads", "fb banner status : " + adUnitHelper.getFbStatus());
        Log.e("app_ads", "unity banner status : " + adUnitHelper.getUnityStatus());
        Log.e("app_ads", "chartboost banner status : " + adUnitHelper.getChartStatus());
    }

    public void adRequestCaller(){
        if (adUnitHelper.getAdmobStatus().equals(ServerAdConstants.STATUS_OK)){
            setAdmobBanner();
        }
        else if (adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){
            setFbBanner();
        }
        else if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
            setUnityBanner();
        }
        else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
            setChartBoostBanner();
        }
        else {
            adContainer.setVisibility(View.GONE);
        }
    }

    private void setAdmobBanner(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling admob banner loaded");
        admobAdView = new AdView(mActivity);
        admobAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
        admobAdView.setAdUnitId(adUnitHelper.getAdmobBanner());
        AdRequest adRequest = new AdRequest.Builder().build();
        admobAdView.loadAd(adRequest);

        adContainer.removeAllViews();
        adContainer.addView(admobAdView);//adding the adview to ad container

        admobAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e(ServerAdConstants.AD_LOG_TAG,"admob banner loaded");
                checkLogValues();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e(ServerAdConstants.AD_LOG_TAG,"admob banner not loaded");
                if (adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){
                    setFbBanner();
                }
                else if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
                    setUnityBanner();
                }
                else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                    setChartBoostBanner();
                }

                else {
                    setAdmobBanner();
                }
                Log.e("admob_banner",loadAdError.toString());
            }
        });
    }

    private void setFbBanner(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling facebook banner");
        fbAdView = new com.facebook.ads.AdView(mActivity,
                adUnitHelper.getFbBanner(),//facebook rectangle banner id
                AdSize.BANNER_HEIGHT_50);

        // Add the ad view to your activity layout
        adContainer.addView(fbAdView);
        // Request an ad
        fbAdView.loadAd(fbAdView.buildLoadAdConfig()
                .withAdListener(new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        Log.e(ServerAdConstants.AD_LOG_TAG,"fb banner not loaded");
                        if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
                            setUnityBanner();
                        }
                        else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                            setChartBoostBanner();
                        }
                        else {
                            setAdmobBanner();
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        Log.e(ServerAdConstants.AD_LOG_TAG,"fb banner loaded");
                        checkLogValues();
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

    private void setUnityBanner() {
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling unity banner");
        unityBannerView = new BannerView(mActivity,
                adUnitHelper.getUnityBanner(),
                new UnityBannerSize(320,50));

        unityBannerView.setListener(new BannerView.IListener() {
            @Override
            public void onBannerLoaded(BannerView bannerView) {
                Log.e(ServerAdConstants.AD_LOG_TAG, "unity banner loaded");
                checkLogValues();
                //Toast.makeText(context,"loaded unity",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerClick(BannerView bannerView) {

            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                Log.e(ServerAdConstants.AD_LOG_TAG, "unity banner not loaded");
                adContainer.removeAllViews();
                if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                    setChartBoostBanner();
                }
                else {
                    setAdmobBanner();
                }
            }

            @Override
            public void onBannerLeftApplication(BannerView bannerView) {

            }
        });

        unityBannerView.load();
        adContainer.removeAllViews();
        adContainer.addView(unityBannerView);
    }

    private void setChartBoostBanner(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling chartBoost banner loaded");
        BannerCallback bannerCallback = new BannerCallback() {
            @Override
            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
                Log.e(ServerAdConstants.AD_LOG_TAG, "chartBoost banner loaded");
                adContainer.removeAllViews();

                if (chartBoostBanner != null) {
                    adContainer.addView(chartBoostBanner);
                    chartBoostBanner.cache();
                    chartBoostBanner.show();

                    Log.e(ServerAdConstants.AD_LOG_TAG, "chartBoost banner loaded and not null ");
                    checkLogValues();
                }else {
                    Log.e(ServerAdConstants.AD_LOG_TAG, "chartBoost banner not loaded");
                    setAdmobBanner();
                }

            }

            @Override
            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

            }

            @Override
            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {
                Log.e("cb_test","on rec banner show");

                if (showError!=null){
                    if (showError.getCode().equals(ShowError.Code.NO_CACHED_AD)){
                        Log.e("cb_test","rec banner no cache");
                        setAdmobBanner();
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

        chartBoostBanner = new com.chartboost.sdk.ads.Banner(mActivity, "start", com.chartboost.sdk.ads.Banner.BannerSize.MEDIUM,
                bannerCallback,null);

    }

}
