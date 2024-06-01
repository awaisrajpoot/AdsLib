package com.example.mylibrary.LibHelpers;

import android.content.SharedPreferences;
import android.util.Log;

public class FolderNameCreator {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public FolderNameCreator(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public void checkAndCreateFolder(){
        String folderName = sharedPreferences.getString(LibConstants.FOLDER_PREF_KEY,"");

        if (folderName.equals("")){
            Log.e("font_dn", "folder name does not exist : "+folderName);

            String savedFolderName = LibConstants.DATA_FOLDER_NAME + System.currentTimeMillis();

            editor = sharedPreferences.edit();
            editor.putString(LibConstants.FOLDER_PREF_KEY, savedFolderName);
            editor.apply();

            folderName = sharedPreferences.getString(LibConstants.FOLDER_PREF_KEY,"");

            Log.e("font_dn", "folder name is created : "+folderName);

        }else {
            Log.e("font_dn", "folder name is exist : " + folderName);
        }
    }

    public String getFolderName(){

        String folderName = sharedPreferences.getString(LibConstants.FOLDER_PREF_KEY,"");

        Log.e("font_dn", "folder name is : "+folderName);

        if (folderName.equals("")){
            Log.e("font_dn", "folder name does not exist : "+folderName);

            String savedFolderName = LibConstants.DATA_FOLDER_NAME + System.currentTimeMillis();

            editor = sharedPreferences.edit();
            editor.putString(LibConstants.FOLDER_PREF_KEY, savedFolderName);
            editor.apply();

            folderName = sharedPreferences.getString(LibConstants.FOLDER_PREF_KEY,"");

            Log.e("font_dn", "folder name is created : "+folderName);

        }

        return folderName;
    }

}
