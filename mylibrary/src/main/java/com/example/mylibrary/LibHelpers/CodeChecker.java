package com.example.mylibrary.LibHelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.learning.learnhtml.AppHelpers.AppConstants;

public class CodeChecker {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String clearCode = "";
    String savedCode = "";
    CodeUpdateListener codeUpdateListener;

    public CodeChecker(Context context, SharedPreferences sharedPreferences, String clearCode){
        codeUpdateListener = (CodeUpdateListener) context;
        this.sharedPreferences = sharedPreferences;
        this.clearCode = clearCode;

        checkCode();
    }

    public void checkCode(){
        savedCode = sharedPreferences.getString(LibConstants.CODE_PREF_KEY,"");

        if (savedCode.equals("")){
            Log.e("code_dn", "code does not exist : "+savedCode);

            editor = sharedPreferences.edit();
            editor.putString(LibConstants.CODE_PREF_KEY, clearCode);
            editor.apply();

            savedCode = sharedPreferences.getString(LibConstants.CODE_PREF_KEY,"");

            Log.e("code_dn", "Added Code is : "+savedCode);


        }else {

            if (!clearCode.equals(savedCode)){
                editor = sharedPreferences.edit();
                editor.putString(LibConstants.CODE_PREF_KEY, clearCode);
                editor.apply();

                codeUpdateListener.onCodeUpdated();

                savedCode = sharedPreferences.getString(LibConstants.CODE_PREF_KEY,"");

                Log.e("code_dn", "Updated Code is : " + savedCode);

            } else {
                Log.e("code_dn", "Saved Code is : " + savedCode);
            }


        }

    }

    public interface CodeUpdateListener{
        void onCodeUpdated();
    }

}
