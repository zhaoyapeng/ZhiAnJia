package com.zhianjia.m.zhianjia.components.network.apply.register;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/8/3.
 * 重置密码申请
 */

public class ResetPasswordApply extends BaseApply {

    private long userTel;
    private String userPWD;

    /**
     * 构造函数
     * @param userTel 用户电话
     * @param userPWD 用户密码
     * @param listener 重置密码申请结果监听
     */
    public ResetPasswordApply(String userTel, String userPWD, HttpActionListener listener) {
        super(listener);
        this.userTel = Long.parseLong(userTel);
        this.userPWD = Utils.md5(userPWD);
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
        return Const.NetWork.BASE_URL + "modify_forget_pwd";
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
            obj.put("user_tel", userTel);
            obj.put("user_pwd", userPWD);
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
