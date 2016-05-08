package com.medtep.network;

/**
 * Created by Alfonso on 08/05/2016.
 */
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class NetworkQueue {

    private static NetworkQueue sInstance;
    private RequestQueue mRequestQueue;
    private static Context sContext;

    private ImageLoader mImageLoader;

    private NetworkQueue(Context context) {
        sContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkQueue getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new NetworkQueue(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
            mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(sContext));
        }
        return  mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().getCache().clear();
        getRequestQueue().add(req);
    }

    public ImageLoader getmImageLoader() { return mImageLoader; }

}
