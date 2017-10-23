package com.zhianjia.m.zhianjia.components.network.apply;

import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 7 on 2017/1/4.
 */

public class AppUpdateApply extends BaseApply {
    /**
     * 构造函数
     *
     * @param listener 结果监听
     */
    public AppUpdateApply(HttpActionListener listener) {
        super(listener);

        doApply();
    }

    @Override
    protected HttpAction.HttpMethod getMethod() {
        return HttpAction.HttpMethod.POST;
    }

    @Override
    protected String getUrl() {
        return Const.NetWork.BASE_URL + "check_update";
    }

    @Override
    protected String getHttpContent() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("device_type", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String ret = obj.toString();
        return ret;
    }

    @Override
    protected void onNetSuccess(JSONObject result, JSONObject dataObj) {
        if (_listener != null) {
            _listener.success(result.toString());
        }
    }

    @Override
    protected void onNetFailure(String errorMessage) {
        if (_listener != null) {
            _listener.failure(errorMessage);
        }
    }
}
