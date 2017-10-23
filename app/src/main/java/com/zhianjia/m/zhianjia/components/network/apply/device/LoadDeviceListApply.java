package com.zhianjia.m.zhianjia.components.network.apply.device;

import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dp on 2016/12/31.
 */

public class LoadDeviceListApply extends BaseApply {
    private String token;

    public LoadDeviceListApply(String token, HttpActionListener listener) {
        super(listener);

        this.token = token;

        doApply();
    }

    @Override
    protected HttpAction.HttpMethod getMethod() {
        return HttpAction.HttpMethod.POST;
    }

    @Override
    protected String getUrl() {
        return Const.NetWork.BASE_URL + "get_device_list";
    }

    @Override
    protected String getHttpContent() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("token", token);
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
