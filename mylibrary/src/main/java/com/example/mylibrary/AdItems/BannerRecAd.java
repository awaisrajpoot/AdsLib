package com.example.mylibrary.AdItems;

import android.os.Handler;
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
import com.chartboost.sdk.events.ExpirationEvent;
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
import com.unity3d.ads.BannerAd;
import com.unity3d.ads.BannerConfiguration;
import com.unity3d.ads.BannerShowListener;
import com.unity3d.ads.BannerSize;
import com.unity3d.ads.LoadListener;
import com.unity3d.ads.UnityAdsError;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import java.util.logging.LogRecord;


public class BannerRecAd {

    private AppCompatActivity mActivity;
    private RectBannerView adContainer;

    private AdView admobAdView;
    private com.facebook.ads.AdView fbAdView;
    //private BannerView unityRecBanner;
    private BannerAd unityRecBanner;
    private Banner chartBoostBanner;

    private AdUnitHelper adUnitHelper;

    private boolean admobStatus = false;
    private boolean fbStatus = false;
    private boolean unityStatus = false;
    private boolean cbStatus = false;
    private boolean startAppStatus = false;
    private boolean splashIsGone = false;
    private Handler handler;

    public BannerRecAd(AppCompatActivity mActivity, AdUnitHelper adUnitHelper, RectBannerView adContainer){
        this.mActivity = mActivity;
        this.adUnitHelper = adUnitHelper;

        this.handler = new Handler();

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
        else if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
            setUnityRectangleBanner();
        }
        else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
            setChartBoostRecBanner();
        }
        else {
            adContainer.setVisibility(View.GONE);
        }
    }

    public void reloadAd(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(ServerAdConstants.AD_LOG_TAG, "Lets Reload Rec Banner Ad");
                adRequestCaller();
            }
        }, adUnitHelper.getReloadTime());
    }

    private void checkLogValues(){
        Log.e(ServerAdConstants.AD_LOG_TAG, "AdMob rect banner status : " + adUnitHelper.getAdmobStatus());
        Log.e(ServerAdConstants.AD_LOG_TAG, "FB rect banner status : " + adUnitHelper.getFbStatus());
        Log.e(ServerAdConstants.AD_LOG_TAG, "Unity rect banner status : " + adUnitHelper.getUnityStatus());
        Log.e(ServerAdConstants.AD_LOG_TAG, "ChartBoost rect banner status : " + adUnitHelper.getChartStatus());
    }

    private void setAdmobRectangleBanner(){

        Log.d(ServerAdConstants.AD_LOG_TAG, "calling AdMob banner");
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
                Log.e(ServerAdConstants.AD_LOG_TAG, "AdMob rec banner loaded");
                checkLogValues();
                showRecBanner(splashIsGone);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                admobStatus = false;
                Log.e(ServerAdConstants.AD_LOG_TAG, "AdMob rec banner not loaded");
                if (adUnitHelper.getFbStatus().equals(ServerAdConstants.STATUS_OK)){
                    setFbRectangleBanner();
                }
                else if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
                    setUnityRectangleBanner();
                }
                else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                    setChartBoostRecBanner();
                }
                else {
                    reloadAd();
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
                        Log.e(ServerAdConstants.AD_LOG_TAG, "Facebook Error : " + adError.getErrorMessage());

                        if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)){
                            setUnityRectangleBanner();
                        }
                        else if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                            setChartBoostRecBanner();
                        }
                        else {
                            reloadAd();
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

    private void setUnityRectangleBanner() {


        BannerConfiguration.Builder builder = new BannerConfiguration
                .Builder(adUnitHelper.getUnityBanner(),
                new BannerSize(320, 250),
                new BannerShowListener() {
                    @Override
                    public void onImpression(@NonNull BannerAd bannerAd) {
                        Log.e(ServerAdConstants.AD_LOG_TAG, "Unity rec banner loaded");
                        checkLogValues();

                    }

                    @Override
                    public void onClicked(@NonNull BannerAd bannerAd) {

                    }

                    @Override
                    public void onFailedToShow(@NonNull BannerAd bannerAd, @NonNull UnityAdsError unityAdsError) {

                        unityStatus = false;

                        Log.e(ServerAdConstants.AD_LOG_TAG, "Unity banner not loaded");
                        adContainer.removeAllViews();

                        if (adUnitHelper.getChartStatus().equals(ServerAdConstants.STATUS_OK)){
                            setChartBoostRecBanner();
                        }
                        else {
                            reloadAd();
                        }
                    }
                });

        BannerAd.load(builder.build(), new LoadListener<BannerAd>() {
            @Override
            public void onAdLoaded(@Nullable BannerAd bannerAd, @Nullable UnityAdsError unityAdsError) {
                if (unityAdsError == null){
                    unityStatus = true;
                    unityRecBanner = bannerAd;
                    showRecBanner(splashIsGone);
                }
            }
        });

    }

    private void setChartBoostRecBanner(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "calling chartBoost banner");

        BannerCallback bannerCallback = new BannerCallback() {
            @Override
            public void onAdExpired(@NonNull ExpirationEvent expirationEvent) {

            }

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
                    reloadAd();
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
                        reloadAd();
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
                Log.e(ServerAdConstants.AD_LOG_TAG,"AdMob banner showed");
            }
            else if (fbStatus){
                // Add the ad view to your activity layout
                adContainer.addView(fbAdView);
                Log.e(ServerAdConstants.AD_LOG_TAG,"fb banner showed");
            }
            else if (unityStatus){
                // Add the ad view to your activity layout
                if (unityRecBanner != null){ adContainer.addView(unityRecBanner.getView()); }

                Log.e(ServerAdConstants.AD_LOG_TAG,"unity banner showed");
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
