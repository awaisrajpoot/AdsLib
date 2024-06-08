package com.example.mylibrary.AdItems;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.mylibrary.LibHelpers.ServerAdConstants;

public class ReplaceAdsMeta {

    Context context;

    public ReplaceAdsMeta(Context context){
        Log.d("app_ads", "metadata replacer started");
        this.context = context;
    }

    public void replaceAdmobMeta(String admobAppId){
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String myApiKey = bundle.getString(ServerAdConstants.ADMOB_PACKAGE_NAME);
            Log.d("app_ads", "Admob Name Found: " + myApiKey);

            ai.metaData.putString(ServerAdConstants.ADMOB_PACKAGE_NAME,
                    admobAppId);//you can replace your key APPLICATION_ID here

            String ApiKey = bundle.getString(ServerAdConstants.ADMOB_PACKAGE_NAME);
            Log.d("app_ads", "Admob Renamed Found: " + ApiKey);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("app_ads", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("app_ads", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }


}
