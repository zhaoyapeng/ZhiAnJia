package com.zhianjia.m.zhianjia.haier.net.volley;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;
import com.zhianjia.m.zhianjia.haier.util.HeadUtil;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;
import com.zhianjia.m.zhianjia.utils.FileUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoyapeng
 * @version create time:16/11/3019:49
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class VolleyPostJsonRequest<T extends BaseModel> extends JsonRequest<T> {
    private static final int DEFAULT_NET_TYPE = 0;
    private final Response.Listener<T> mListener;
    private Class<T> modelClass;
    private Context mContext;
    private String accessToken;
    private Response.ErrorListener errorListener;

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
        Map<String, String> headerParams = new HashMap<String, String>();
        String acceptToken = SharedPreferencesDevice.getAccessToken(mContext);
//        Log.e("tag", "getSaveDeviceId=" + SharedPreferencesDevice.getAccessToken(mContext));

        headerParams.putAll(HeadUtil.buildCommonHeader(acceptToken, new Gson().toJson(getParams()), mContext));
//        Log.e("tag", "headerParams1=" + headerParams);
//        Log.e("tag", "headerParams2=" + new Gson().toJson(getParams()));
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

    public VolleyPostJsonRequest(String url, Map<String, String> map, Class<T> modelClass, Response.Listener<T> listener,
                                 Response.ErrorListener errorListener, Context mContext) {
        super(Method.POST, url, new JSONObject(map).toString(), listener, errorListener);
        Log.e("tag", "new JSONObject(map).toString()=" + new JSONObject(map).toString());
        mListener = listener;
        this.errorListener=errorListener;
        this.modelClass = modelClass;
        this.mContext = mContext;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(4000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);

        try {
            Log.e("tag", "post-url=" + GetParamsUtil.getParmas(url, map));
        } catch (Exception e) {

        }
    }


    private String json;

    public VolleyPostJsonRequest(String url, String json, Class<T> modelClass, Response.Listener<T> listener,
                                 Response.ErrorListener errorListener, Context mContext) {
        super(Method.POST, url, json, listener, errorListener);
        Log.e("tag", "jsonjson=" + json);
        this.json = json;
        mListener = listener;
        this.errorListener=errorListener;
        this.modelClass = modelClass;
        this.mContext = mContext;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(4000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);

        try {
//            Log.e("tag", "post-url=" + GetParamsUtil.getParmas(url, map));
        } catch (Exception e) {

        }
    }


    private Map<String, Object> map;
    private boolean isObject;

    public VolleyPostJsonRequest(String url, Map<String, Object> map, String accessToken, Class<T> modelClass, Response.Listener<T> listener,
                                 Response.ErrorListener errorListener, Context mContext, boolean isObject) {
        super(Method.POST, url, new JSONObject(map).toString(), listener, errorListener);
        mListener = listener;
        this.errorListener=errorListener;
        this.map = map;
        this.isObject = isObject;
        this.modelClass = modelClass;
        this.accessToken = accessToken;
        this.mContext = mContext;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(4000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);

        try {
//            Log.e("tag", "post-url=" + GetParamsUtil.getParmas(url, map));
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

            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "UTF-8"));
            Log.e("tag", "网络请求结果=" + parsed);
            responseWrapper = Response.success(new Gson().fromJson(parsed, modelClass), VolleyHttpHeaderParser.volleyParseCacheHeaders(response));

            return responseWrapper;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (this.getCacheKey() == null)
            return;
        ((BaseModel) response).cacheKey = this.getCacheKey();
        ((BaseModel) response).isCache = responseWrapper.intermediate;
        ((BaseModel) response).setHeaders(responseWrapper.cacheEntry.responseHeaders);

        if (response.getCode() == 101) {
            Toast.makeText(mContext, "您的账号再别处登录了", Toast.LENGTH_SHORT).show();
            FileUtils.logout();
            SharedPreferencesDevice.clear(mContext);
            Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_LOGIN);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_USER_PROFILE);
            mContext.startActivity(intent);
            return;
        }

        else if (response.getCode() / 1000000 > 0 || response.getCode() / 100000 > 0 || response.getCode() / 1000 > 0 || response.getCode() == 10 || response.getCode() == 11 || response.getCode() == 1) {
            if (!TextUtils.isEmpty(response.getMsg()))
                Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
            response.setCode(0);
        }
        mListener.onResponse(response);
    }
}
