package com.zhianjia.m.zhianjia.components.network.apply.user;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/8/2.
 * 更改密码申请
 */

public class ChangePasswordApply extends BaseApply {

    private String token;
    private String password, orgPassword;

    /**
     * 构造函数
     *
     * @param token       应用Token
     * @param orgPassword 旧密码
     * @param password    新密码
     * @param listener    更改结果监听
     */
    public ChangePasswordApply(String token, String orgPassword, String password, HttpActionListener listener) {
        super(listener);
        this.token = token;
        this.orgPassword = Utils.md5(orgPassword);
        this.password = Utils.md5(password);
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
        return Const.NetWork.BASE_URL + "modify_pwd";
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
            obj.put("user_ori_pwd", orgPassword);
            obj.put("user_now_pwd", password);
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
            try {
                _listener.success(result.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
