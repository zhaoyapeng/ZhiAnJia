package com.zhianjia.m.zhianjia.haier.net.route;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.haier.uhome.usdk.api.uSDKDevice;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.haier.AppDemo;
import com.zhianjia.m.zhianjia.haier.data.DeviceDetailsDataModel;
import com.zhianjia.m.zhianjia.haier.data.DeviceListModel;
import com.zhianjia.m.zhianjia.haier.data.LoginDataModel;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.data.UserAccount;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceListModel;
import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;
import com.zhianjia.m.zhianjia.haier.net.util.UrlUtil;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyErrorUtil;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyGetRequest;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyPostJsonRequest;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyPostJsonSingleArrayRequest;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyUtil;
import com.zhianjia.m.zhianjia.haier.util.HeadUtil;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;
import com.zhianjia.m.zhianjia.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaoyapeng
 * @version create time:17/5/1616:43
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class DeviceDetailRoute {

    public static void login(final Context mContext, final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();
//        params.put("loginId", "13264302360");
//        params.put("password", "123456");

        params.put("loginId", "15910606499");
        params.put("password", "Aaa111222");
        params.put("deviceToken", "");

        String url = "/openapi/v2/user/login";


        VolleyPostJsonRequest<LoginModel> getRequest = new VolleyPostJsonRequest<LoginModel>(mContext.getString(R.string.req_url) + url, params, LoginModel.class
                , new Response.Listener<LoginModel>() {
            @Override
            public void onResponse(LoginModel response) {
                netWorkCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, mContext) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }

    /**
     * 绑定 设备
     * */
    public static void bindDevice(final Context mContext, uSDKDevice ready2AccountDevice, final NetWorkCallBack netWorkCallBack) {

        String url = "/openapi/v2/device/bind";

        String acceptToken = SharedPreferencesDevice.getAccessToken(mContext);

        List<uSDKDevice> ready2AccountDeviceCollection = new ArrayList<>();
        ready2AccountDeviceCollection.add(ready2AccountDevice);

        final String addDevice2AccountJsonStr =
                HeadUtil.buildBindDeviceContent(acceptToken, ready2AccountDeviceCollection);


        String json = "";
        try {
            json = new JSONObject(addDevice2AccountJsonStr).toString();
        } catch (Exception e) {

        }


        VolleyPostJsonSingleArrayRequest<BaseModel> getRequest1 = new VolleyPostJsonSingleArrayRequest<>(mContext.getString(R.string.req_url) + url, json, BaseModel.class
                , new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel response) {
                netWorkCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, mContext);

        getRequest1.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest1);

    }

    /**
     * 解绑 设备
     * */
    public static void unBindDevice(final Context mContext, String  deviceId, final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();

        params.put("deviceId", deviceId);
        String url = "/openapi/v2/device/unbind";
        VolleyPostJsonRequest<LoginModel> getRequest = new VolleyPostJsonRequest<LoginModel>(mContext.getString(R.string.req_url) + url, params, LoginModel.class
                , new Response.Listener<LoginModel>() {
            @Override
            public void onResponse(LoginModel response) {
                netWorkCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, mContext) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }



    /**
     *  从海尔云获取绑定设备列表
     * */
    public static void getBindDeviceList(final Context mContext, final NetWorkCallBack netWorkCallBack) {
        final HashMap<String, String> params = new HashMap<>();
        String url = "/uds/v1/protected/deviceinfos";
        VolleyGetRequest<DeviceListModel> getRequest = new VolleyGetRequest<DeviceListModel>(mContext.getString(R.string.uws_req_url) + url, url,DeviceListModel.class
                , new Response.Listener<DeviceListModel>() {
            @Override
            public void onResponse(DeviceListModel response) {
                sendToServiceBindList(mContext,response,netWorkCallBack);
//                netWorkCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, mContext,false) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }


    /**
     *   将从海尔云获取的设备列表 上传 智安家服务器
     * */
    public static void sendToServiceBindList(final Context mContext, DeviceListModel response,final NetWorkCallBack netWorkCallBack) {
        if(response==null){
            return;
        }

        String bind_list = new Gson().toJson(response);
        final HashMap<String, String> params = new HashMap<>();
        params.put("token", SharedPreferencesDevice.getAzjToken(mContext));
        params.put("bind_list",bind_list);
        String url = "/upload_bind_list";
        String  finallyUrl= mContext.getString(R.string.azj_req_url)+url+ UrlUtil.getCommonParam(mContext,mContext.getString(R.string.azj_sign_key),params);
        VolleyPostJsonRequest<DeviceListModel> getRequest = new VolleyPostJsonRequest<>(finallyUrl, new Gson().toJson(params), DeviceListModel.class
                , new Response.Listener<DeviceListModel>() {
            @Override
            public void onResponse(DeviceListModel response) {
                if(response.getCode()==0){
                    getDeviceListFromSevice(mContext,netWorkCallBack);
                }else{
                    netWorkCallBack.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, mContext);

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }


    /**
     *  从智安家获取设别获取设备
     * */
    public static void getDeviceListFromSevice(final Context mContext, final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();
        params.put("token", SharedPreferencesDevice.getAzjToken(mContext));

        String url = "/get_device_list";
        String  finallyUrl= mContext.getString(R.string.azj_req_url)+url+ UrlUtil.getCommonParam(mContext,mContext.getString(R.string.azj_sign_key),params);

        VolleyPostJsonRequest<ZDeviceListModel> getRequest = new VolleyPostJsonRequest<>(finallyUrl, new Gson().toJson(params), ZDeviceListModel.class
                , new Response.Listener<ZDeviceListModel>() {
            @Override
            public void onResponse(ZDeviceListModel response) {
                netWorkCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, mContext);

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }


    /**
     *  获取设备 检测到的信息
     * */
    public static void getDeviceInfo(final Context mContext, String deviceId,final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();
        params.put("token", SharedPreferencesDevice.getAzjToken(mContext));
        params.put("device_id", deviceId);
        String url = "/get_device_info";

        String  finallyUrl= mContext.getString(R.string.azj_req_url)+url+ UrlUtil.getCommonParam(mContext,mContext.getString(R.string.azj_sign_key),params);

        VolleyPostJsonRequest<DeviceDetailsDataModel> getRequest = new VolleyPostJsonRequest<>(finallyUrl, new Gson().toJson(params), DeviceDetailsDataModel.class
                , new Response.Listener<DeviceDetailsDataModel>() {
            @Override
            public void onResponse(DeviceDetailsDataModel response) {
                netWorkCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, mContext);

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }


    public static void updateDeviceName(final Context mContext,HashMap<String, String> hashMap, final NetWorkCallBack netWorkCallBack) {


        final HashMap<String, String> params = new HashMap<>();
        params.putAll(hashMap);
        String url = "/openapi/v2/device/updateNickname";
        VolleyPostJsonRequest<LoginModel> getRequest = new VolleyPostJsonRequest<LoginModel>(mContext.getString(R.string.req_url) + url, params, LoginModel.class
                , new Response.Listener<LoginModel>() {
            @Override
            public void onResponse(LoginModel response) {
                netWorkCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, mContext) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }

}
