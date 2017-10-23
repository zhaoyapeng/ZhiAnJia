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
import com.haier.uhome.usdk.api.uSDKManager;
import com.zhianjia.m.zhianjia.haier.AppDemo;
import com.zhianjia.m.zhianjia.haier.data.UserAccount;
import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;
import com.zhianjia.m.zhianjia.haier.util.AppUtil;
import com.zhianjia.m.zhianjia.haier.util.HeadUtil;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zhaoyapeng
 * @version create time:15-1-20下午12:15
 * @Email zhaoyp@witmob.com
 * @Description ${自定义 request}
 */
public class VolleyPostRequest<T extends BaseModel> extends Request<T> {

    private static final int DEFAULT_NET_TYPE = 0;
    private final Response.Listener<T> mListener;
    private Class<T> modelClass;
    private Context mContext;

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
    public Map<String, String> getHeaders() throws AuthFailureError {

//        headerParams.put("Version", AppUtil.getVersionName(mContext));
//        headerParams.put("VersionCode", AppUtil.getVersionCode(mContext) + "");
//        headerParams.put("AppType", "0");
//        headerParams.put("Channel", "Jusfoun");
//        headerParams.put("DeviceId", PhoneUtil.getIMEI(mContext));
        Map<String, String> headerParams = new HashMap<String, String>();
        String acceptToken = SharedPreferencesDevice.getAccessToken(mContext);
        Log.e("tag", "getSaveDeviceId=" + SharedPreferencesDevice.getAccessToken(mContext));

        headerParams.putAll(HeadUtil.buildCommonHeader(acceptToken, new Gson().toJson(getParams()), mContext));
        Log.e("tag", "headerParams1=" + headerParams);
        Log.e("tag", "headerParams2=" + new Gson().toJson(getParams()));
        return headerParams;
    }

    /**
     * null 正常逻辑
     * 可以重写此方法，返回json数据
     * TODO
     *
     * @return
     */
    public String getDemoRespose() {
        return null;
    }

    public VolleyPostRequest(String url,String accessToken, Class<T> modelClass, Response.Listener<T> listener,
                             Response.ErrorListener errorListener, Context mContext) {
        super(Method.POST, url, errorListener);
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


    /**
     * uSDK provide clientId generator
     * uSDK提供內置clientId生成方法
     */
    private String getClientId(Context context) {
        uSDKManager uSDKMgr = uSDKManager.getSingleInstance();
        String clientId = uSDKMgr.getClientId(context);
        return clientId;
    }

}
