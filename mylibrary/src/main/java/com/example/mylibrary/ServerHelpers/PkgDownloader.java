package com.example.mylibrary.ServerHelpers;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mylibrary.AdsInitializer;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PkgDownloader {

    String url;
    AdsInitializer adsInitializer;
    AppCompatActivity compatActivity;
    String fileName;
    private RequestQueue mQueue;
    PkgDownloadListener pkgDownloadListener;


    public PkgDownloader(AdsInitializer adsInitializer, AppCompatActivity compatActivity, String url,
                              String fileName){
        this.url = url;
        this.adsInitializer = adsInitializer;
        this.fileName = fileName;
        this.compatActivity = compatActivity;

        pkgDownloadListener = (PkgDownloadListener) adsInitializer;

        mQueue = Volley.newRequestQueue(compatActivity);
    }

    public void downloadPkgJsonData() {

        Log.e("server_response",
                "pkg : server call started");

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("server_response",
                                "pkg server response is : " + response.toString());

                        jsonFileSaver(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        //settingAdapter(firstName[0],Integer.parseInt(firstLength[0]));
    }

    private void jsonFileSaver(String strData){
        saveFileInCache(strData);
    }

    private void saveFileInCache(String strData){

        File file = new File(compatActivity.getFilesDir(), fileName);

        try {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(strData.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pkgDownloadListener.onPkgFileDownloaded(file);
    }

    public interface PkgDownloadListener{
        void onPkgFileDownloaded(File jsonFile);
    }


}
