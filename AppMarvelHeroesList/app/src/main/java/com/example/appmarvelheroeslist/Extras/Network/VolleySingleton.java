package com.example.appmarvelheroeslist.Extras.Network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


//---- Handles de the context of the requests in a singleton ----
public class VolleySingleton {

    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;

    private static Context mContext;

    private VolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    //---- Synchronize the request to the API ----
    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    //---- Ensure all requests were done ----
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    //---- Make a queue of request to be done ----
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
