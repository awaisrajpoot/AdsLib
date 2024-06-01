package com.example.mylibrary.ServerHelpers;

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


    public GetServerItems(Context context, String metaLink){
        this.mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
        jsonParse(metaLink);
    }

    private void jsonParse(String metaLink) {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                metaLink, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("server_response",
                                    "server response is : " + response.toString());

                            String clearCode = response.getString("clear_code");

                            Log.e("sec_items",
                                    ", clear code : " + clearCode);

                            serverListener.OnConnected(clearCode);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public interface ServerListener{
        void OnConnected(String clearCode);
    }
}
