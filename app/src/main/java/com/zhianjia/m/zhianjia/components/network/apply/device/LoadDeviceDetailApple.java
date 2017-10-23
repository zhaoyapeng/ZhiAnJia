package com.zhianjia.m.zhianjia.components.network.apply.device;

import android.util.Log;

import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dp on 2016/12/31.
 */

public class LoadDeviceDetailApple extends BaseApply {

    private String token;
    private String deviceId;

    public LoadDeviceDetailApple(String token, String deviceId, HttpActionListener listener) {
        super(listener);

        this.token = token;
        this.deviceId = deviceId;

        doApply();
    }

    @Override
    protected HttpAction.HttpMethod getMethod() {
        return HttpAction.HttpMethod.POST;
    }

    @Override
    protected String getUrl() {
        return Const.NetWork.BASE_URL + "get_device_info";
    }

    @Override
    protected String getHttpContent() {
        Log.d("LoadDeviceDetailApple", "token: " + token);
        JSONObject obj = new JSONObject();
        try {
            obj.put("token", token);
            obj.put("device_id", deviceId);
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
