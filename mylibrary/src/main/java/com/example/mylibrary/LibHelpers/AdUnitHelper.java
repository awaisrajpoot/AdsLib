package com.example.mylibrary.LibHelpers;

import android.util.Log;

import java.io.Serializable;

public class AdUnitHelper implements Serializable {

    private String adsStatus;

    private String admobStatus, admobId, admobBanner, admobInter;
    private String fbStatus, fbRec, fbBanner, fbInter;
    private String unityStatus, unityId, unityTestMode, unityBanner, unityInter;
    private String chartStatus, chartId, chartSignature, chartSdkStatus;

    public AdUnitHelper(){
        Log.d("app_ads", "ad units helper started.");
    }

    //setter and getter for ads status
    public void setAdsStatus(String adsStatus) {
        this.adsStatus = adsStatus;
    }
    public String getAdsStatus() {
        return adsStatus;
    }

    /*************************
     *
     *   setter,s for admob
     *
     *************************/
    public void setAdmobStatus(String admobStatus) {
        this.admobStatus = admobStatus;
    }
    public void setAdmobId(String admobId) {
        this.admobId = admobId;
    }
    public void setAdmobBanner(String admobBanner) {
        this.admobBanner = admobBanner;
    }
    public void setAdmobInter(String admobInter) {
        this.admobInter = admobInter;
    }

    /*************************
     *
     *   getter,s for admob
     *
     *************************/
    public String getAdmobStatus() {
        return admobStatus;
    }
    public String getAdmobId() {
        return admobId;
    }
    public String getAdmobBanner() {
        return admobBanner;
    }
    public String getAdmobInter() {
        return admobInter;
    }

    /**********************************
     *
     *      setter,s for facebook
     *
     **********************************/
    public void setFbStatus(String fbStatus) {
        this.fbStatus = fbStatus;
    }
    public void setFbRec(String fbRec) {
        this.fbRec = fbRec;
    }
    public void setFbBanner(String fbBanner) {
        this.fbBanner = fbBanner;
    }
    public void setFbInter(String fbInter) {
        this.fbInter = fbInter;
    }

    /**********************************
     *
     *       getter,s for facebook
     *
     **********************************/
    public String getFbStatus() {
        return fbStatus;
    }
    public String getFbRec() {
        return fbRec;
    }
    public String getFbBanner() {
        return fbBanner;
    }
    public String getFbInter() {
        return fbInter;
    }

    /**********************************
     *
     *       setter,s for unity
     *
     **********************************/
    public void setUnityStatus(String unityStatus) {
        this.unityStatus = unityStatus;
    }
    public void setUnityId(String unityId) {
        this.unityId = unityId;
    }
    public void setUnityTestMode(String unityTestMode) {
        this.unityTestMode = unityTestMode;
    }
    public void setUnityBanner(String unityBanner) {
        this.unityBanner = unityBanner;
    }
    public void setUnityInter(String unityInter) {
        this.unityInter = unityInter;
    }

    /**********************************
     *
     *       getter,s for unity
     *
     **********************************/
    public String getUnityStatus() {
        return unityStatus;
    }
    public String getUnityId() {
        return unityId;
    }
    public String getUnityTestMode() {
        return unityTestMode;
    }
    public String getUnityBanner() {
        return unityBanner;
    }
    public String getUnityInter() {
        return unityInter;
    }

    /**********************************
     *
     *     setter,s for chartBoost
     *
     **********************************/
    public void setChartStatus(String chartStatus) {
        this.chartStatus = chartStatus;
    }
    public void setChartId(String chartId) {
        this.chartId = chartId;
    }
    public void setChartSdkStatus(String chartSdkStatus) {
        this.chartSdkStatus = chartSdkStatus;
    }
    public void setChartSignature(String chartSignature) {
        this.chartSignature = chartSignature;
    }

    /**********************************
     *
     *     getter,s for chartBoost
     *
     **********************************/
    public String getChartStatus() {
        return chartStatus;
    }
    public String getChartId() {
        return chartId;
    }
    public String getChartSdkStatus() {
        return chartSdkStatus;
    }

    public String getChartSignature() {
        return chartSignature;
    }
}

