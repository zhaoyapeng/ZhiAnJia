package com.zhianjia.m.zhianjia.components.network.apply.user;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;

/**
 * Created by Zibet.K on 2016/8/3.
 * 上传头像申请
 */

public class UploadAvatarApply extends BaseApply {
    /**
     * 构造函数
     *
     * @param uploadFile 上传头像Bitmap
     * @param listener   上传结果监听
     */
    public UploadAvatarApply(Bitmap uploadFile, HttpActionListener listener) {
        super(listener);
        setUploadFile(uploadFile, "avatarIcon.png", "file");
        doApply();
    }

    /**
     * 获取网络申请方法
     *
     * @return 返回网络申请方法（Post/Get)
     */
    @Override
    protected HttpAction.HttpMethod getMethod() {
        return HttpAction.HttpMethod.POST_UPLOAD_BITMAP;
    }

    /**
     * 获取网址
     *
     * @return 返回网址
     */
    @Override
    protected String getUrl() {
        return Const.NetWork.BASE_URL + "upload_file";
    }

    /**
     * 获取申请信息
     *
     * @return 返回申请信息
     */
    @Override
    protected String getHttpContent() {
        return null;
    }

    /**
     * 申请成功
     *
     * @param result 成功消息
     * @param data   消息数据
     */
    @Override
    protected void onNetSuccess(JSONObject result, JSONObject data) {
        try {
            JSONObject obj = data.getJSONObject("url");
            String url = obj.getString("b");
            if (_listener != null) {
                _listener.success(url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (_listener != null) {
                _listener.failure("JSON解析失败");
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
