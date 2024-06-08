package com.example.mylibrary.LibHelpers;

import static com.example.mylibrary.LibHelpers.AdUnitSaver.getStrData;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;

public class PkgChecker {

    File pkgFile;

    public PkgChecker(File pkgFile){
        this.pkgFile = pkgFile;
    }

    public boolean checkPkgItems(AppCompatActivity compatActivity){

        JsonElement jsonElement = getStrData(pkgFile);

        if (jsonElement != null) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String pkgStatus = jsonObject.get("pkg_status").getAsString();

            //no need to check any pkg
            if (pkgStatus.equals("false")){
                Log.i("file_data","its ok pkg");
                return true;
            }else {//need to be check pkg
                JsonArray pkgList = jsonObject.get("pkg_list").getAsJsonArray();

                for (JsonElement string: pkgList){
                    Log.i("pkg_test", string.getAsString());

                    if (compatActivity.getPackageName().equals(string.getAsString())){
                        Log.i("file_data","its ok pkg");
                        return true;
                    }

                }
            }

        }

        Log.i("file_data","its maybe not pkg");
        return false;

    }

}
