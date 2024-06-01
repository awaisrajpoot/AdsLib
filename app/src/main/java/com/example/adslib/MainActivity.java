package com.example.adslib;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mylibrary.DemoCls;
import com.example.mylibrary.GetServerItems;

public class MainActivity extends AppCompatActivity implements GetServerItems.ServerListener {

    String adsUrl = "http://androidlayouts.com/test_ads.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new GetServerItems(this, adsUrl);
    }

    @Override
    public void OnServerError() {

    }

    @Override
    public void OnConnected(String adsStatus, String admobId, String admobStatus, String admobBanner, String admobInter, String fbStatus, String fbRec, String fbBanner, String fbInter, String unityId, String unityStatus, String unityTestMode, String unityBanner, String unityInter, String chartId, String chartSignature, String chartSdkStatus, String chartStatus, String startAppId, String startAppStatus) {

    }
}