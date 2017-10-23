package com.zhianjia.m.zhianjia.components.network.apply.device;

import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 7 on 2017/1/11.
 */

public class ModifyDeviceNameApply extends BaseApply {
    private String token;
    private String deviceId;
    private String newDeviceName;

    public ModifyDeviceNameApply(String token, String deviceId, String newDeviceName, HttpActionListener listener) {
        super(listener);

        this.token = token;
        this.deviceId = deviceId;
        this.newDeviceName = newDeviceName;

        doApply();
    }

    @Override
    protected HttpAction.HttpMethod getMethod() {
        return HttpAction.HttpMethod.POST;
    }

    @Override
    protected String getUrl() {
        return Const.NetWork.BASE_URL + "modify_device_name";
    }

    @Override
    protected String getHttpContent() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("token", token);
            obj.put("device_id", deviceId);
            obj.put("device_name", newDeviceName);
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
