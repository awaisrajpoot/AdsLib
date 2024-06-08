package com.example.mylibrary;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.AdItems.AllSdkInitializer;
import com.example.mylibrary.LibHelpers.AdUnitHelper;
import com.example.mylibrary.LibHelpers.AdUnitSaver;
import com.example.mylibrary.LibHelpers.CodeChecker;
import com.example.mylibrary.LibHelpers.LibConstants;
import com.example.mylibrary.LibHelpers.PkgChecker;
import com.example.mylibrary.LibHelpers.SavedFileChecker;
import com.example.mylibrary.ServerHelpers.JsonDataDownloader;
import com.example.mylibrary.ServerHelpers.PkgDownloader;
import com.example.mylibrary.ServerHelpers.UpdateCodeHelper;

import java.io.File;

public class AdsInitializer implements UpdateCodeHelper.ServerListener,
        JsonDataDownloader.JsonDownloadListener, AdUnitSaver.AdUnitSaverListener,
        CodeChecker.CodeUpdateListener, PkgDownloader.PkgDownloadListener{

    AppCompatActivity compatActivity;
    String fileName = "";

    SharedPreferences codePrefs;

    JsonDataDownloader jsonDataDownloader;

    SavedFileChecker savedFileChecker;

    AdUnitHelper adUnitHelper;
    AdUnitSaver adUnitSaver;

    String classTag = "AdsInitializer.java : ";
    AdsInitCompleteListener adsInitCompleteListener;
    String metaInfoLink = "";

    SavedFileChecker pkgFileChecker;


    public AdsInitializer(AppCompatActivity compatActivity, String adsUrl,
                          String metaInfoLink,String fileName){

        this.compatActivity = compatActivity;
        this.adsInitCompleteListener = (AdsInitCompleteListener) compatActivity;

        this.metaInfoLink = metaInfoLink;

        String pkgFileName = "pkg_"+fileName;

        //here we will check all pkg
        if (getPkgFileStatus(pkgFileName)){
            Log.i("file_data",classTag + "lets get pkg saved file");

            PkgChecker pkgChecker = new PkgChecker(pkgFileChecker.getSavedJsonFile());
            if (pkgChecker.checkPkgItems(compatActivity)){
                initItems(adsUrl, fileName);
            }
            getPkgData(pkgFileName);
        }else {//no need to check any pkg just, get the latest pkg data, go for ads initialization
            initItems(adsUrl, fileName);

            Log.i("file_data",classTag + "lets get pkg data file download");
            getPkgData(pkgFileName);//we need to get latest data everytime on boot up
        }
    }

    public void getPkgData(String pkgFileName){
        PkgDownloader pkgDownloader = new PkgDownloader(this,
                compatActivity, LibConstants.PKG_LINK, pkgFileName);
        pkgDownloader.downloadPkgJsonData();
    }

    private boolean getPkgFileStatus(String pkgFileName) {

        Log.i("file_data",classTag + "pkg file name is : "+pkgFileName);

        pkgFileChecker = new SavedFileChecker(compatActivity, pkgFileName);

        if (pkgFileChecker.checkFileExist()){
            Log.i("file_data",classTag + "pkg file exist : "+pkgFileName);
            return true;
        }else {
            Log.i("file_data",classTag + "pkg file does,nt exist : "+pkgFileName);
            return false;
        }
    }

    private void initItems(String adsUrl ,String fileName){
        adUnitHelper = new AdUnitHelper();
        adUnitSaver = new AdUnitSaver(adUnitHelper, this);

        codePrefs = compatActivity.getPreferences(MODE_PRIVATE);

        this.fileName = fileName;

        Log.i("file_data",classTag + "file name is : " + fileName);
        //data_ads_lib1717337568429

        //it will check "jsonAds" file is available or not, if file
        //available so "getSavedJsonFile" function will return the saved file
        savedFileChecker = new SavedFileChecker(compatActivity, fileName);

        if (savedFileChecker.checkFileExist()){
            Log.i("file_data",classTag + "file exist :)");

            //getting json file from "savedFileChecker.getSavedJsonFile()"
            //and sending as parameter to "AdUnitSaver"
            adUnitSaver.saveAdUnits(savedFileChecker.getSavedJsonFile());

            //getClearCode(metaInfoLink);
        }
        //if "jsonAds" file is not available so "JsonDownloader" will download the ads
        //file from server and save it in local cache storage, so we use could use it
        //for later, so no need to send request to server for each time.
        else {
            Log.i("file_data",classTag + "file does not exist :(");

            jsonDataDownloader = new JsonDataDownloader(this, compatActivity,adsUrl, fileName);
            jsonDataDownloader.downloadJsonData();
        }
    }

    //after downloading "jsonAdsFile" from server and saving it into local cache successfully
    @Override
    public void onJsonFileDownloaded(File jsonFile) {
        Log.i("json_tag", classTag + "json file : " + jsonFile.getAbsolutePath());

        //let check if "jsonAds" file is available
        if (savedFileChecker.checkFileExist()){
            //get the saved "jsonAds" file and send it to for runtime use.
            adUnitSaver.saveAdUnits(savedFileChecker.getSavedJsonFile());
        }
    }

    //after ad unit are saved successfully in "AdUnitHelper" with help of "AdUnitSave"
    //now we can use "adUnitHelper" object for runtime user
    @Override
    public void onAdsUnitSaved(AdUnitHelper adUnitHelper) {
        this.adUnitHelper = adUnitHelper;

        AllSdkInitializer allSdkInitializer = new AllSdkInitializer(compatActivity);
        allSdkInitializer.setAdsInitializer(adUnitHelper);

        //now all kind of data collection and initialization completed
        //so now are ready to call ads
        adsInitCompleteListener.onAdsInitComplete(adUnitHelper);

        if (!metaInfoLink.isEmpty()){
            getClearCode(metaInfoLink);
        }
    }

    public void getClearCode(String metaLink){
        new UpdateCodeHelper(compatActivity, metaLink).setServerListener(this);
    }
    @Override
    public void OnConnected(String clearCode) {
        new CodeChecker(this, codePrefs, clearCode);
    }

    @Override
    public void onCodeUpdated() {
        savedFileChecker.delJsonFile();
    }

    @Override
    public void onPkgFileDownloaded(File jsonFile) {
        Log.e("server_response", "pkg added to file" +jsonFile.getName());
    }


    public interface AdsInitCompleteListener{
        void onAdsInitComplete(AdUnitHelper adUnitHelper);
    }

}
