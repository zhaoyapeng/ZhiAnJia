package com.zhianjia.m.zhianjia.haier.net.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;
import com.zhianjia.m.zhianjia.haier.util.HeadUtil;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author henzil
 * @version create time:15/1/21_下午4:21
 * @Description 重写关于volley get请求方法
 */
public class VolleyGetRequest<T extends BaseModel> extends Request<T> {
    private static final int DEFAULT_NET_TYPE = 0;

    private final Response.Listener<T> mListener;
    private Class<T> modelClass;
    private Context mContext;
    private boolean isMd5 = true;
    private String splitUrl;

    /**
     * 重新定义缓存key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        String params = "";
        try {
            params = getParams().toString();
        } catch (Exception e) {

        }
        return getUrl() + params;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public VolleyGetRequest(String url, Class<T> modelClass, Response.Listener<T> listener,
                            Response.ErrorListener errorListener, Context mContext) {
        super(Method.GET, url, errorListener);
        Log.e("TAG", "request_url:========" + url);
        mListener = listener;
        this.mContext = mContext;
        this.modelClass = modelClass;
    }

    public VolleyGetRequest(String url,String splitUrl, Class<T> modelClass, Response.Listener<T> listener,
                            Response.ErrorListener errorListener, Context mContext,boolean isMd5) {
        super(Method.GET, url, errorListener);
        Log.e("TAG", "request_url:========" + url);
        mListener = listener;
        this.mContext = mContext;
        this.modelClass = modelClass;
        this.isMd5=isMd5;
        this.splitUrl=splitUrl;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerParams = new HashMap<String, String>();
        String acceptToken = SharedPreferencesDevice.getAccessToken(mContext);
        headerParams.putAll(HeadUtil.buildCommonHeader(acceptToken, splitUrl, mContext,isMd5));
        return headerParams;
    }

    private Response<T> responseWrapper;

    /**
     * 解析 response
     *
     * @param response
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {

            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "UTF-8"));
            Log.e("tag", "网络请求结果=" + parsed);
            responseWrapper = Response
                    .success(new Gson().fromJson(parsed, modelClass), VolleyHttpHeaderParser.volleyParseCacheHeaders(response));

            return responseWrapper;
        } catch (UnsupportedEncodingException e) {
            Log.e("error", e.toString());
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(T response) {
        ((BaseModel) response).cacheKey = this.getCacheKey();
        ((BaseModel) response).isCache = responseWrapper.intermediate;
        mListener.onResponse(response);
    }

}

