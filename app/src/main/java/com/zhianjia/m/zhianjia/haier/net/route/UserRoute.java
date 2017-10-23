package com.zhianjia.m.zhianjia.haier.net.route;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.haier.AppDemo;
import com.zhianjia.m.zhianjia.haier.data.LoginDataModel;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.data.UserAccount;
import com.zhianjia.m.zhianjia.haier.net.util.UrlUtil;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyErrorUtil;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyPostJsonRequest;
import com.zhianjia.m.zhianjia.haier.net.volley.VolleyUtil;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoyapeng
 * @version create time:17/8/2917:35
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class UserRoute  {

    /**
     * 获取用户信息
     *
     * */
    public  void getUserInfo(final Context mContext,final NetWorkCallBack netWorkCallBack) {

        User user = FileUtils.getDefaultUser();
        if(user==null)
            return;
        final HashMap<String, String> params = new HashMap<>();
        params.put("token",user.getToken());

        String url = "/get_self_info";
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
}
