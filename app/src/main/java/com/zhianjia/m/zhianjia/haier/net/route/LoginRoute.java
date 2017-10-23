package com.zhianjia.m.zhianjia.haier.net.route;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.haier.data.DeviceListModel;
import com.zhianjia.m.zhianjia.haier.data.LoginDataModel;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceListModel;
import com.zhianjia.m.zhianjia.haier.net.util.UrlUtil;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyErrorUtil;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyPostJsonRequest;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyUtil;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;
import com.zhianjia.m.zhianjia.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoyapeng
 * @version create time:17/8/2910:43
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class LoginRoute {


    public static void login(final Context mContext, String loginId,String password,final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();
//        params.put("loginId", "13264302360");
//        params.put("password", "123456");

        params.put("loginId", loginId);
        params.put("password", password);
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

    public  void registerAndLogin(final Context mContext, String mobile,String password,String uvc,final NetWorkCallBack netWorkCallBack) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password",password);
        params.put("uvc", uvc);
        String url = "/openapi/v2/user/registerAndlogin";

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
     *  重置密码
     * */
    public  void resetPwd(final Context mContext, String mobile,String password,String uvc,final NetWorkCallBack netWorkCallBack) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("loginId", mobile);
        params.put("newPwd",password);
        params.put("uvc", uvc);
        String url = "/openapi/v2/user/reset";

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
     * 注册 获取验证码
     * */
    public  void getCode(final Context mContext,String mobile, final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        String url = "/openapi/v2/user/applyActivationCode";


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
     * 重置 密码 获取验证码
     * */
    public  void getResetCode(final Context mContext,String mobile, final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();
        params.put("loginId", mobile);
        String url = "/openapi/v2/user/applyReset";


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
     * 智安家 登录
     *
     * */
    public  void loginZhiaj(final Context mContext, String usertel,String password,String uplus_token,final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();
        params.put("usertel", usertel);
        params.put("password", Utils.md5(password));
        params.put("uplus_token", uplus_token);

        String url = "/login";
        String  finallyUrl= mContext.getString(R.string.azj_req_url)+url+ UrlUtil.getCommonParam(mContext,mContext.getString(R.string.azj_sign_key),params);

        VolleyPostJsonRequest<LoginDataModel> getRequest = new VolleyPostJsonRequest<LoginDataModel>(finallyUrl, params, LoginDataModel.class
                , new Response.Listener<LoginDataModel>() {
            @Override
            public void onResponse(LoginDataModel response) {
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
     *   修改用户信息接口
     * */
    public static void modifyUserInfo(final Context mContext,String userName, final NetWorkCallBack netWorkCallBack) {

        final HashMap<String, String> params = new HashMap<>();
        params.put("token", SharedPreferencesDevice.getAzjToken(mContext));
        params.put("user_name",userName);
        params.put("user_img", "");

        String url = "/modify_user";
        String  finallyUrl= mContext.getString(R.string.azj_req_url)+url+ UrlUtil.getCommonParam(mContext,mContext.getString(R.string.azj_sign_key),params);

        VolleyPostJsonRequest<LoginModel> getRequest = new VolleyPostJsonRequest<>(finallyUrl, new Gson().toJson(params), LoginModel.class
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
        }, mContext);

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }


    public static void loginOut(final Context mContext,final NetWorkCallBack netWorkCallBack) {


        final HashMap<String, String> params = new HashMap<>();
        params.put("token", SharedPreferencesDevice.getAzjToken(mContext));
        String url = "/logout";
        String  finallyUrl= mContext.getString(R.string.azj_req_url)+url+ UrlUtil.getCommonParam(mContext,mContext.getString(R.string.azj_sign_key),params);

        VolleyPostJsonRequest<LoginModel> getRequest = new VolleyPostJsonRequest<>(finallyUrl, new Gson().toJson(params), LoginModel.class
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
        }, mContext);

        getRequest.setShouldCache(false);
        VolleyUtil.getQueue(mContext).add(getRequest);
    }

}
