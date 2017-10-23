package com.zhianjia.m.zhianjia.components.network.apply.user;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;

/**
 * Created by Zibet.K on 2016/8/3.
 * 更改用户信息申请
 */

public class ModifyUserApply extends BaseApply {

    private String token, username, iconUrl;

    /**
     * 构造函数
     * @param token 应用Token
     * @param username 用户名
     * @param iconUrl 用户头像URL
     * @param listener 更改用户信息结果监听
     */
    public ModifyUserApply(String token, String username, String iconUrl, HttpActionListener listener) {
        super(listener);
        this.token = token;
        this.username = username;
        this.iconUrl = iconUrl;
        doApply();
    }

    /**
     * 获取网络申请方法
     *
     * @return 返回网络申请方法（Post/Get)
     */
    @Override
    protected HttpAction.HttpMethod getMethod() {
        return HttpAction.HttpMethod.POST;
    }

    /**
     * 获取网址
     *
     * @return 返回网址
     */
    @Override
    protected String getUrl() {
        return Const.NetWork.BASE_URL + "modify_user";
    }

    /**
     * 获取申请信息
     *
     * @return 返回申请信息
     */
    @Override
    protected String getHttpContent() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("token", token);
            obj.put("user_name", username);
            obj.put("user_img", iconUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String ret = obj.toString();
        return ret;
    }

    /**
     * 申请成功
     *
     * @param result 成功消息
     * @param data   消息数据
     */
    @Override
    protected void onNetSuccess(JSONObject result, JSONObject data) {
        if (_listener != null) {
            _listener.success(result.toString());
        }
    }

    /**
     * 申请失败
     *
     * @param errorMessage 失败原因
     */
    @Override
    protected void onNetFailure(String errorMessage) {
        if (_listener != null) {
            _listener.failure(errorMessage);
        }
    }
}
