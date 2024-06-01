package com.example.mylibrary;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GetServerItems {

    private RequestQueue mQueue;
    private Context mContext;

    ServerListener serverListener;

    public GetServerItems(Context context, String adsUrl){
        this.mContext = context;
        serverListener = (ServerListener) context;

        Log.e("app_ads", "server request added");
        mQueue = Volley.newRequestQueue(mContext);
        jsonParse(adsUrl);
    }

    private void jsonParse(String adsUrl) {

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, adsUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String adsStatus = response.getString("ads_status");

                            String admobId = response.getString("admob_id");
                            String admobStatus = response.getString("admob_status");
                            String admobBanner = response.getString("admob_banner");
                            String admobInter = response.getString("admob_inter");

                            String fbStatus = response.getString("fb_status");
                            String fbRec = response.getString("fb_rec");
                            String fbBanner = response.getString("fb_banner");
                            String fbInter = response.getString("fb_inter");

                            String unityId = response.getString("unity_id");
                            String unityStatus = response.getString("unity_status");
                            String unityTestMode = response.getString("unity_test_mode");
                            String unityBanner = response.getString("unity_banner");
                            String unityInter = response.getString("unity_inter");

                            String chartId = response.getString("chart_id");
                            String chartSignature = response.getString("chart_signature");
                            String chartSdkStatus = response.getString("chart_sdk_status");
                            String chartStatus = response.getString("chart_status");

                            String startAppId = response.getString("start_app_id");
                            String startAppStatus = response.getString("start_app_status");

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
                                            "chart status : " + chartStatus + "\n" +

                                            "startApp id : " + startAppId + "\n" +
                                            "startApp status : " + startAppStatus + "\n");

                            Log.e("app_ads", "setting up values");
                            serverListener.OnConnected(adsStatus,
                                    admobId,admobStatus, admobBanner, admobInter,
                                    fbStatus,fbRec, fbBanner, fbInter,
                                    unityId, unityStatus, unityTestMode,unityBanner, unityInter,
                                    chartId, chartSignature, chartSdkStatus, chartStatus,
                                    startAppId, startAppStatus);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                Log.e("app_ads", "server error : " + error.toString());

            }
        });

        mQueue.add(request);
        //settingAdapter(firstName[0],Integer.parseInt(firstLength[0]));
    }

    public interface ServerListener{
        void OnServerError();
        void OnConnected(String adsStatus,
                         String admobId, String admobStatus, String admobBanner, String admobInter,
                         String fbStatus, String fbRec, String fbBanner, String fbInter,
                         String unityId, String unityStatus, String unityTestMode,String unityBanner, String unityInter,
                         String chartId, String chartSignature, String chartSdkStatus, String chartStatus,
                         String startAppId, String startAppStatus);
    }
}
