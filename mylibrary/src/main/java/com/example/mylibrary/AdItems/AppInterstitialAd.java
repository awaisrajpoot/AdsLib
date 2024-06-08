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
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;


public class AppInterstitialAd implements IUnityAdsInitializationListener {


    private AppCompatActivity mActivity;

    //admob ad
    private com.google.android.gms.ads.interstitial.InterstitialAd admobInterstitial;
    //facebook ad
    InterstitialAdListener fbInterstitialAdListener;
    private InterstitialAd fbInterstitialAd;
    //chartBoost ad
    Interstitial chartboostInterstitial = null;

    //unity deprecate its "onReady"
    //so set the value true of "unityAdLoaded", it help to show the unity ads on desire time
    public boolean unityAdLoaded = false;
    private AdUnitHelper adUnitHelper;


    public AppInterstitialAd(AppCompatActivity mActivity, AdUnitHelper adUnitHelper) {
        this.mActivity = mActivity;
        this.adUnitHelper = adUnitHelper;
    }

    private void checkLogValues(){
        Log.e("app_ads", "admob inter status : " + adUnitHelper.getAdmobStatus());
        Log.e("app_ads", "fb inter status : " + adUnitHelper.getFbStatus());
        Log.e("app_ads", "unity inter status : " + adUnitHelper.getUnityStatus());
        Log.e("app_ads", "chartboost inter status : " + adUnitHelper.getChartStatus());
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
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling admob interstitial");
        AdRequest adRequest = new AdRequest.Builder().build();

        com.google.android.gms.ads.interstitial.InterstitialAd
                .load(mActivity, adUnitHelper.getAdmobInter(),
                        adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                admobInterstitial = interstitialAd;
                Log.d(ServerAdConstants.AD_LOG_TAG, "admob interstitial loaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(ServerAdConstants.AD_LOG_TAG, "admob interstitial not loaded");
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
                loadAdmobAd();
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
        UnityAds.initialize (mActivity.getApplicationContext()
                //unity app id
                , adUnitHelper.getUnityId()
                //unity ads status in test mode or not
                , Boolean.parseBoolean(adUnitHelper.getUnityTestMode()),
                this);
    }

    private void loadChartBoostAd(){
        chartboostInterstitial = new Interstitial("location", new InterstitialCallback() {
            @Override
            public void onAdDismiss(@NonNull DismissEvent dismissEvent) {

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
        else if(fbInterstitialAd!=null && fbInterstitialAd.isAdLoaded() &&
                adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){
            fbInterstitialAd.show();
        }
        else if(unityAdLoaded && adUnitHelper.getUnityStatus()
                .equals(ServerAdConstants.STATUS_OK)){
            UnityAds.show(mActivity,
                    //unity interstitial ad id
                    adUnitHelper.getUnityInter(), new UnityAdsShowOptions(), showListener);
        }
        else if(chartboostInterstitial != null && adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)) {
            chartboostInterstitial.show();
        }
        else {
            loadAd();
        }
    }


    //After unity initialization we can load the unity ad, it,s the best way
    @Override
    public void onInitializationComplete() {
        UnityAds.load(
                //unity interstitial ad id
                adUnitHelper.getUnityInter(), new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String s) {
                Log.v("UnityAdsExample", "unity inter ad loaded");
                unityAdLoaded = true;
            }

            @Override
            public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                Log.v("UnityAdsExample", "unity inter ad not loaded : " + s1);
                unityAdLoaded = false;
            }
        });
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {

    }

    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
            if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                loadChartBoostAd();
            }
            else {
                Log.d(ServerAdConstants.AD_LOG_TAG, "unity interstitial not loaded");
                checkLogValues();
                loadAd();
            }
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
            if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                loadChartBoostAd();
            }
            else {
                loadAd();
            }
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);

        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);

        }
    };

}
