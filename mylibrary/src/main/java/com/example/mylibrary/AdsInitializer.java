package com.example.mylibrary;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.LibHelpers.FolderNameCreator;
import com.example.mylibrary.ServerHelpers.GetServerItems;

public class AdsInitializer implements GetServerItems.ServerListener {

    AppCompatActivity compatActivity;
    String folderName = "";

    SharedPreferences folderPrefs, codePrefs;
    FolderNameCreator folderCreator;

    public AdsInitializer(AppCompatActivity compatActivity){
        this.compatActivity = compatActivity;
    }

    public void getClearCode(String metaLink){
        new GetServerItems(compatActivity, metaLink).setServerListener(this);
    }

    private void setPrefs(){
        codePrefs = compatActivity.getPreferences(MODE_PRIVATE);
        folderPrefs = compatActivity.getPreferences(MODE_PRIVATE);

        folderCreator = new FolderNameCreator(folderPrefs);
        folderCreator.checkAndCreateFolder();
    }

    @Override
    public void OnConnected(String clearCode) {
        Log.i("clear_code", "code : " + clearCode);
    }
}
