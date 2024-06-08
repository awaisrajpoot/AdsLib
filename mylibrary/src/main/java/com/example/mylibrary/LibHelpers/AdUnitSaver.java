package com.example.mylibrary.LibHelpers;

import android.util.Log;

import com.example.mylibrary.AdsInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class AdUnitSaver {

    AdUnitHelper adUnitHelper;
    AdUnitSaverListener adUnitSaverListener;

    public AdUnitSaver(AdUnitHelper adUnitHelper, AdsInitializer adsInitializer){
        this.adUnitHelper = adUnitHelper;
        adUnitSaverListener = (AdUnitSaverListener) adsInitializer;
    }

    public void saveAdUnits(File jsonFile){

        JsonElement jsonElement = getStrData(jsonFile);

        if (jsonElement != null) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String adsStatus = jsonObject.get("ads_status").getAsString();

            String admobId = jsonObject.get("admob_id").getAsString();
            String admobStatus = jsonObject.get("admob_status").getAsString();
            String admobBanner = jsonObject.get("admob_banner").getAsString();
            String admobInter = jsonObject.get("admob_inter").getAsString();

            String fbStatus = jsonObject.get("fb_status").getAsString();
            String fbRec = jsonObject.get("fb_rec").getAsString();
            String fbBanner = jsonObject.get("fb_banner").getAsString();
            String fbInter = jsonObject.get("fb_inter").getAsString();

            String unityId = jsonObject.get("unity_id").getAsString();
            String unityStatus = jsonObject.get("unity_status").getAsString();
            String unityTestMode = jsonObject.get("unity_test_mode").getAsString();
            String unityBanner = jsonObject.get("unity_banner").getAsString();
            String unityInter = jsonObject.get("unity_inter").getAsString();

            String chartId = jsonObject.get("chart_id").getAsString();
            String chartSignature = jsonObject.get("chart_signature").getAsString();
            String chartSdkStatus = jsonObject.get("chart_sdk_status").getAsString();
            String chartStatus = jsonObject.get("chart_status").getAsString();

            Log.e("app_ads",
                    "ads status : " + adsStatus + "\n" +
                            "admob id : " + admobId + "\n" +
                            "admob status : " + admobStatus + "\n" +
                            "admob banner : " + admobBanner + "\n" +
                            "admob inter : " + admobInter + "\n" +

                            "fb status : " + fbStatus + "\n" +
                            "fb rec : " + fbRec + "\n" +
                            "fb banner : " + fbBanner + "\n" +
                            "fb inter : " + fbInter + "\n" +

                            "unity id : " + unityId + "\n" +
                            "unity status : " + unityStatus + "\n" +
                            "unity test mode : " + unityTestMode + "\n" +
                            "unity banner : " + unityBanner + "\n" +
                            "unity inter : " + unityInter + "\n" +

                            "chat id : " + chartId + "\n" +
                            "chart signature : " + chartSignature + "\n" +
                            "chart SDK status : " + chartSdkStatus + "\n" +
                            "chart status : " + chartStatus + "\n");


            if (adsStatus!=null
                    && admobStatus!=null && admobId!=null && admobBanner!=null && admobInter!=null
                    && fbStatus!=null && fbRec!=null && fbBanner!=null && fbInter!=null
                    && unityStatus!=null && unityId!=null && unityTestMode!=null && unityBanner!=null && unityInter!=null
                    && chartStatus!=null && chartId!=null && chartSignature!=null && chartSdkStatus!=null){

                adUnitHelper.setAdsStatus(adsStatus);

                adUnitHelper.setAdmobStatus(admobStatus);
                adUnitHelper.setAdmobId(admobId);
                adUnitHelper.setAdmobBanner(admobBanner);
                adUnitHelper.setAdmobInter(admobInter);

                adUnitHelper.setFbStatus(fbStatus);
                adUnitHelper.setFbRec(fbRec);
                adUnitHelper.setFbBanner(fbBanner);
                adUnitHelper.setFbInter(fbInter);

                adUnitHelper.setUnityStatus(unityStatus);
                adUnitHelper.setUnityId(unityId);
                adUnitHelper.setUnityTestMode(unityTestMode);
                adUnitHelper.setUnityBanner(unityBanner);
                adUnitHelper.setUnityInter(unityInter);

                adUnitHelper.setChartStatus(chartStatus);
                adUnitHelper.setChartId(chartId);
                adUnitHelper.setChartSignature(chartSignature);
                adUnitHelper.setChartSdkStatus(chartSdkStatus);

                Log.e("app_ads",
                        "ads are saved to : AdUnitHelper ");

                adUnitSaverListener.onAdsUnitSaved(adUnitHelper);

            }
            else {
                Log.e("app_ads",
                        "AdUnitSaver.java : Something wrong with ad units. " +
                                "You may need to check your json ads file on server !");
            }

        }

    }

    public interface AdUnitSaverListener{
        void onAdsUnitSaved(AdUnitHelper adUnitHelper);
    }


    public static JsonElement getStrData(File jsonFile){
        Gson gson = new Gson();
        Reader reader = null;

        try {
            reader = new FileReader(jsonFile);
            JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);

            return jsonElement;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
