package com.medtep.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alfonso on 08/05/2016.
 */
public class NetworkRequest extends StringRequest {

    private Map<String, String> mParams;

    public NetworkRequest(int method, String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mParams = params;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        String httpPostBody = "";
        if(mParams != null) {
            try {
                StringBuilder st = new StringBuilder();
                for (Map.Entry<String, String> param : mParams.entrySet()) {
                    st.append(param.getKey() + "=" + param.getValue() + "&");
                }
                httpPostBody = st.toString();
            } catch (Exception exception) {
                Log.v("tag", "exception", exception);
                // return null and don't pass any POST string if you encounter encoding error
                return null;
            }
        }
        return httpPostBody.getBytes();
    }

    public static class Builder {

        private Context mContext;
        private int mMethod = Method.GET;
        private String mUrl = "";
        private Map<String, String> mParams;
        private Response.Listener<String> mListener;
        private Response.ErrorListener mErrorListener;
        private RequestFuture<String> mFuture;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setMethod(int method) {
            mMethod = method;
            return this;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setParams(Map<String, String> params) {
            mParams = params;
            return this;
        }

        public Builder addParam(String name, String value) {
            if(mParams == null) mParams = new HashMap<>();

            mParams.put(name, value);

            return this;
        }

        public Builder setListener(Response.Listener<String> listener) {
            mListener = listener;
            return this;
        }

        public Builder setErrorListener(Response.ErrorListener errorListener) {
            mErrorListener = errorListener;
            return this;
        }

        public Builder setSynchronous(RequestFuture<String> future) {
            mFuture = future;
            return this;
        }

        public void build() {
            NetworkQueue.getInstance(mContext).addToRequestQueue(
                    new NetworkRequest(mMethod, mUrl, mParams,
                            mFuture == null ? mListener : mFuture,
                            mFuture == null ? mErrorListener : mFuture)
            );
        }

    }
}