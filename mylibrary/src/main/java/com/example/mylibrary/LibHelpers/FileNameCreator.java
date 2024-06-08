package com.example.mylibrary.LibHelpers;

import android.content.SharedPreferences;
import android.util.Log;

public class FileNameCreator {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String jsonExe = ".json";

    public FileNameCreator(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public void checkAndCreateFileName(){
        String fileName = sharedPreferences.getString(LibConstants.FILE_PREF_KEY,"");

        if (fileName.isEmpty()){
            Log.e("font_dn", "file name does not exist : "+fileName);

            String savedFileName = LibConstants.DATA_FILE_NAME + System.currentTimeMillis() + jsonExe;

            editor = sharedPreferences.edit();
            editor.putString(LibConstants.FILE_PREF_KEY, savedFileName);
            editor.apply();

            fileName = sharedPreferences.getString(LibConstants.FILE_PREF_KEY,"");

            Log.e("font_dn", "folder name is created : "+fileName);

        }else {
            Log.e("font_dn", "folder name is exist : " + fileName);
        }
    }

    public String getFileName(){

        String fileName = sharedPreferences.getString(LibConstants.FILE_PREF_KEY,"");

        Log.e("font_dn", "folder name is : "+fileName);

        if (fileName.isEmpty()){
            Log.e("font_dn", "folder name does not exist : "+fileName);

            String savedFileName = LibConstants.DATA_FILE_NAME + System.currentTimeMillis() + jsonExe;

            editor = sharedPreferences.edit();
            editor.putString(LibConstants.FILE_PREF_KEY, savedFileName);
            editor.apply();

            fileName = sharedPreferences.getString(LibConstants.FILE_PREF_KEY,"");

            Log.e("font_dn", "folder name is created : "+fileName);

        }

        return fileName;
    }

}
