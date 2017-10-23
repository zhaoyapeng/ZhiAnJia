package com.zhianjia.m.zhianjia.haier.net.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author  wangchenchen
 * CreateDate 2017-4-11.
 * Email wcc@jusfoun.com
 * Description delete request
 */
public class VolleyDelRequest<T extends BaseModel> extends Request<T> {

    private Response.Listener<T> mListener;
    private Class<T> modelClass;
    private Context mContext;

    public VolleyDelRequest(int method, String url, Response.ErrorListener listener) {
        super(Method.DELETE, url, listener);
    }

    public VolleyDelRequest(String url, Class<T> modelClass, Response.Listener<T> listener,
                            Response.ErrorListener errorListener, Context mContext){
        super(Method.DELETE, url, errorListener);
        mListener = listener;
        this.modelClass = modelClass;
        this.mContext = mContext;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(4000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);

        try {
            Log.e("tag", "post-url=" + GetParamsUtil.getParmas(url, getPostParams()));
        } catch (Exception e) {

        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerParams = new HashMap<String, String>();

//        headerParams.put("Version", AppUtil.getVersionName(mContext));
//        headerParams.put("VersionCode", AppUtil.getVersionCode(mContext) + "");
//        headerParams.put("AppType", "0");
//        headerParams.put("Channel", "Jusfoun");
//        headerParams.put("DeviceId", PhoneUtil.getIMEI(mContext));
//        headerParams.put("Content-Type","application/json");


        return headerParams;
    }
    /**
     * 解析 response
     *
     * @param response
     * @return
     */
    private Response<T> responseWrapper;


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {


            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers,"UTF-8"));
            Log.e("tag", "网络请求结果=" + parsed);
            responseWrapper = Response.success(new Gson().fromJson(parsed, modelClass), VolleyHttpHeaderParser.volleyParseCacheHeaders(response));

            return responseWrapper;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (this.getCacheKey()==null)
            return;
        ((BaseModel) response).cacheKey = this.getCacheKey();
        ((BaseModel) response).isCache = responseWrapper.intermediate;
        mListener.onResponse(response);
    }

    @Override
    public String getCacheKey() {
        String params = "";
        try {
            params = getParams().toString();
        } catch (Exception e) {
        }
        return getUrl() + params;
    }
}
