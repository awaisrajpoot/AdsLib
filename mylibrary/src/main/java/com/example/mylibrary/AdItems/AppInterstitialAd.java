package com.example.mylibrary.AdItems;

import static com.facebook.ads.CacheFlag.ALL;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chartboost.sdk.ads.Interstitial;
import com.chartboost.sdk.callbacks.InterstitialCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.DismissEvent;
import com.chartboost.sdk.events.ExpirationEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.example.mylibrary.LibHelpers.AdUnitHelper;
import com.example.mylibrary.LibHelpers.ServerAdConstants;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.InterstitialShowListener;
import com.unity3d.ads.LoadConfiguration;
import com.unity3d.ads.ShowConfiguration;
import com.unity3d.ads.ShowFinishState;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsError;
import com.unity3d.ads.UnityAdsShowOptions;


public class AppInterstitialAd {


    private AppCompatActivity mActivity;

    //admob ad
    private com.google.android.gms.ads.interstitial.InterstitialAd admobInterstitial;
    //facebook ad
    InterstitialAdListener fbInterstitialAdListener;
    private InterstitialAd fbInterstitialAd;
    //Unity ad
    com.unity3d.ads.InterstitialAd unityInterstitial;
    //chartBoost ad
    Interstitial chartboostInterstitial = null;

    private AdUnitHelper adUnitHelper;


    public AppInterstitialAd(AppCompatActivity mActivity, AdUnitHelper adUnitHelper) {
        this.mActivity = mActivity;
        this.adUnitHelper = adUnitHelper;
    }

    private void checkLogValues(){
        Log.e("app_ads", "AdMob inter status : " + adUnitHelper.getAdmobStatus());
        Log.e("app_ads", "fb inter status : " + adUnitHelper.getFbStatus());
        Log.e("app_ads", "unity inter status : " + adUnitHelper.getUnityStatus());
        Log.e("app_ads", "ChartBoost inter status : " + adUnitHelper.getChartStatus());
    }

    public void loadAd(){
        if (adUnitHelper.getAdmobStatus().equals(ServerAdConstants.STATUS_OK)){
            loadAdmobAd();
        }
        else if (adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){
            loadFbInterstitial();
        }
        else if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
            loadUnityAd();
        }
        else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
            loadChartBoostAd();
        }
    }

    private void loadAdmobAd(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling AdMob interstitial");
        AdRequest adRequest = new AdRequest.Builder().build();

        com.google.android.gms.ads.interstitial.InterstitialAd
                .load(mActivity, adUnitHelper.getAdmobInter(),
                        adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                admobInterstitial = interstitialAd;
                Log.d(ServerAdConstants.AD_LOG_TAG, "AdMob interstitial loaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(ServerAdConstants.AD_LOG_TAG, "AdMob interstitial not loaded");
                checkLogValues();
                // Handle the error
                admobInterstitial = null;
                if (adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){
                    loadFbInterstitial();
                }
                else if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
                    loadUnityAd();
                }
                else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                    loadChartBoostAd();
                }
                else {
                    loadAd();
                }
                Log.e("admob_banner",loadAdError.toString());
            }

        });

    }

    private void loadFbInterstitial(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling facebook interstitial");
        //making new object of facebook interstitial ad and setting the ad unit
        fbInterstitialAd = new InterstitialAd(mActivity.getApplicationContext(), adUnitHelper.getFbInter());

        fbInterstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(ServerAdConstants.AD_LOG_TAG, "facebook interstitial not loaded");
                checkLogValues();
                if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
                    loadUnityAd();
                }
                else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                    loadChartBoostAd();
                }
                else {
                    loadAd();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(ServerAdConstants.AD_LOG_TAG, "facebook interstitial loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        fbInterstitialAd.loadAd(fbInterstitialAd.buildLoadAdConfig()
                .withAdListener(fbInterstitialAdListener)
                .withCacheFlags(ALL)
                .build());

    }

    //unity loads it ad while its initialization.
    //because we have to pass it,s ad load listener as parameter to it "initialize" function
    //and don,t forget to check "showListener". just for reloading purpose, for other company ads
    private void loadUnityAd(){

        LoadConfiguration configuration = new LoadConfiguration
                .Builder(adUnitHelper.getUnityInter()).build();

        com.unity3d.ads.InterstitialAd
                .load(configuration, (interstitialAd, error) -> {

                    if (interstitialAd != null) {
                        // Ad loaded successfully, ready to be shown
                        Log.d(ServerAdConstants.AD_LOG_TAG, "Unity interstitial loaded");

                        this.unityInterstitial = interstitialAd;

                        interstitialAd.setOnAdExpired(expiredAd -> {
                            Log.e(ServerAdConstants.AD_LOG_TAG, "Unity interstitial expired");
                            this.unityInterstitial = null;
                            this.loadAd();
                        });
                    } else {
                        Log.e(ServerAdConstants.AD_LOG_TAG, "Unity interstitial not loaded");

                        if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                            loadChartBoostAd();
                        }
                        else {
                            loadAd();
                        }
                    }
        });

    }

    private void loadChartBoostAd(){
        chartboostInterstitial = new Interstitial("location", new InterstitialCallback() {
            @Override
            public void onAdExpired(@NonNull ExpirationEvent expirationEvent) {
                loadAd();
            }

            @Override
            public void onAdDismiss(@NonNull DismissEvent dismissEvent) {
                loadAd();
            }

            @Override
            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
                // after this is successful ad can be shown
                if(chartboostInterstitial != null) {
                    Log.d(ServerAdConstants.AD_LOG_TAG, "chartBoost interstitial loaded");
                    chartboostInterstitial.cache();
                }else {
                    Log.e(ServerAdConstants.AD_LOG_TAG, "chartBoost not loaded");
                    checkLogValues();
                    loadAd();
                }
            }

            @Override
            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

            }

            @Override
            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

            }

            @Override
            public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

            }

            @Override
            public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

            }
        }, null);
    }

    public void showLoadedInterstitial(){

        if(admobInterstitial !=null &&
                adUnitHelper.getAdmobStatus().equals(ServerAdConstants.STATUS_OK)){//showing admob ad if loaded

            admobInterstitial.show(mActivity);
        }
        else if(fbInterstitialAd!=null &&
                fbInterstitialAd.isAdLoaded() &&
                adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){

            fbInterstitialAd.show();
        }
        else if(unityInterstitial!=null &&
                adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){

            showUnityInterstitial(unityInterstitial);
        }
        else if(chartboostInterstitial != null &&
                adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)) {

            chartboostInterstitial.show();
        }
        else {
            loadAd();
        }
    }

    private void showUnityInterstitial(com.unity3d.ads.InterstitialAd interstitialAd){

        Log.v(ServerAdConstants.AD_LOG_TAG, "Unity Interstitial lets show");

        interstitialAd.show(mActivity,
                new ShowConfiguration.Builder().build(),
                new InterstitialShowListener() {
                    @Override
                    public void onStarted(com.unity3d.ads.InterstitialAd interstitialAd) {
                        Log.v(ServerAdConstants.AD_LOG_TAG, "Unity Interstitial start showing");
                        loadAd();
                    }

                    @Override
                    public void onClicked(com.unity3d.ads.InterstitialAd interstitialAd) {
                        Log.v(ServerAdConstants.AD_LOG_TAG, "Unity Interstitial clicked");
                    }

                    @Override
                    public void onCompleted(com.unity3d.ads.InterstitialAd interstitialAd, @NonNull ShowFinishState showFinishState) {
                        Log.v(ServerAdConstants.AD_LOG_TAG, "Unity Interstitial completed");

                    }

                    @Override
                    public void onFailed(com.unity3d.ads.InterstitialAd interstitialAd, @NonNull UnityAdsError unityAdsError) {
                        Log.v(ServerAdConstants.AD_LOG_TAG, "Unity Interstitial failed to show");
                        loadAd();
                    }
                });

    }

}
