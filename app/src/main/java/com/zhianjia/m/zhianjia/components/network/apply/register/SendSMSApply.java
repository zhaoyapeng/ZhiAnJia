package com.zhianjia.m.zhianjia.components.network.apply.register;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;

/**
 * Created by zizi-PC on 2016/8/2.
 * 发送验证码
 */

public class SendSMSApply extends BaseApply {
    private String mobile;
    private int verifyType;

    /**
     * 构造函数
     *
     * @param mobile     用户手机号
     * @param verifyType 验证码类型（1 注册验证码 ；2 重置密码验证码）
     * @param listener   发送验证码结果监听
     */
    public SendSMSApply(String mobile, int verifyType, HttpActionListener listener) {
        super(listener);
        this.mobile = mobile;
        this.verifyType = verifyType;
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
        return Const.NetWork.BASE_URL + "send_sms";
    }

    /**
     * 获取申请信息
     *
     * @return 返回申请信息
     */
    @Override
    protected String getHttpContent() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_tel", mobile);
            jsonObject.put("type", verifyType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String ret = jsonObject.toString();
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
