package com.example.mylibrary.AdItems;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.LoggingLevel;
import com.chartboost.sdk.callbacks.StartCallback;
import com.chartboost.sdk.events.StartError;
import com.example.mylibrary.LibHelpers.AdUnitHelper;
import com.example.mylibrary.LibHelpers.ServerAdConstants;
import com.unity3d.ads.UnityAds;

import org.jetbrains.annotations.Nullable;


public class AllSdkInitializer {

    private AppCompatActivity compatActivity;
    private AdUnitHelper adUnitHelper;

    public AllSdkInitializer(AppCompatActivity compatActivity){
        this.compatActivity = compatActivity;
    }

    public void setAdsInitializer(AdUnitHelper adUnitHelper){

        this.adUnitHelper = adUnitHelper;

        if (adUnitHelper.getAdmobStatus().equals(ServerAdConstants.STATUS_OK)){
            new ReplaceAdsMeta(compatActivity).replaceAdmobMeta(adUnitHelper.getAdmobId());
        }

        if (adUnitHelper.getUnityStatus().equals(ServerAdConstants.STATUS_OK)) {
            unityInitializer();
        }

        if (adUnitHelper.getChartSdkStatus().equals(ServerAdConstants.STATUS_OK)){
            chartBoostInitializer();
        }

    }


    private void unityInitializer(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "unity SDK Init Start");
        // Initialize the SDK:
        UnityAds.initialize (compatActivity, adUnitHelper.getUnityId(),
                Boolean.parseBoolean(adUnitHelper.getUnityTestMode()));
    }

    private void chartBoostInitializer(){
        Log.d(ServerAdConstants.AD_LOG_TAG, "chartBoost SDK Init Start");
        Chartboost.setLoggingLevel(LoggingLevel.ALL);

        Chartboost.startWithAppId(compatActivity,
                adUnitHelper.getChartId(), adUnitHelper.getChartSignature(), new StartCallback() {
                    @Override
                    public void onStartCompleted(@Nullable StartError startError) {

                        Log.e(ServerAdConstants.AD_LOG_TAG,"cb initialization completed");

                        if (startError!=null){

                            if (StartError.Code.INVALID_CREDENTIALS.equals(startError.getCode())){
                                Log.e(ServerAdConstants.AD_LOG_TAG,"cb invalid credentials");
                            }
                            else if (StartError.Code.SERVER_ERROR.equals(startError.getCode())){
                                Log.e(ServerAdConstants.AD_LOG_TAG,"cb server error");
                            }
                            else if (StartError.Code.NETWORK_FAILURE.equals(startError.getCode())){
                                Log.e(ServerAdConstants.AD_LOG_TAG,"cb network failure");
                            }

                        }else {
                            Log.e(ServerAdConstants.AD_LOG_TAG,"cb startError is null");
                        }
                    }
                });
    }


}
