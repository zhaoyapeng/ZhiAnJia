package com.zhianjia.m.zhianjia.components.network.apply;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.components.network.method.HttpGetAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpPostAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpPostUploadBitmapAction;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/7/8.
 * 申请模板
 */

public abstract class BaseApply implements HttpActionListener {


    protected Handler _handler = null;
    protected HttpAction _action = null;
    protected HttpActionListener _listener;
    protected String netContent;
    protected HttpAction.HttpMethod httpMethod;

    /**
     * 构造函数
     *
     * @param listener 结果监听
     */
    public BaseApply(HttpActionListener listener) {
        HttpAction.HttpMethod method = getMethod();
        String url = getUrl();

        System.out.println("URL: " + url);

        httpMethod = method;
        if (method == HttpAction.HttpMethod.POST) {
            _action = new HttpPostAction(this, url);
        } else if (method == HttpAction.HttpMethod.GET) {
            _action = new HttpGetAction(this, url);
        } else if (method == HttpAction.HttpMethod.POST_UPLOAD_BITMAP) {
            _action = new HttpPostUploadBitmapAction(this, url);
        }
        _listener = listener;
        buildHandler();
    }

    /**
     * 设定上传图片
     *
     * @param uploadFile 上传的Bitmap
     * @param fileName   文件名
     * @param serverName 文件上传后服务器上的名字
     */
    public void setUploadFile(Bitmap uploadFile, String fileName, String serverName) {
        _action.setUploadBitmap(uploadFile, fileName, serverName);
    }

    /**
     * 开始申请
     */
    public void doApply() {
        if (_action != null) {
            netContent = getHttpContent();
            if (netContent == null) {
                netContent = "";
            }
            _action.doHttpRequest(netContent);
        }
    }

    /**
     * 构建主线程Handler
     */
    private void buildHandler() {
        _handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                if (what == Const.NetWork.HTTP_FAILURE) {
                    onNetFailure((String) msg.obj);
                } else {
                    String m1 = (String) msg.obj;
//                    System.out.println("net message back\n" + m1);
                    JSONTokener jsonParser = new JSONTokener(m1);
                    try {
                        JSONObject info = (JSONObject) jsonParser.nextValue();
                        String jsonMSG = info.getString("msg");
                        int code = info.getInt("code");
                        if (code == Const.NetWork.NET_SUCCESS_CODE && (info.get("data") instanceof JSONObject)) {
                            JSONObject data = info.getJSONObject("data");
                            onNetSuccess(info, data);
                        } else if (code == Const.NetWork.NET_SUCCESS_CODE && (info.get("data") instanceof JSONArray)) {
                            onNetSuccess(info, null);
                        } else if (code == Const.NetWork.NET_SUCCESS_CODE && (info.get("data") instanceof String)) {
                            onNetSuccess(info, null);
                        } else {
                            onNetFailureOperation(info, code);
                            onNetFailure(jsonMSG);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onNetFailure("Json解析错误");
                    }
                }
            }
        };

    }

    /**
     * 失败特殊操作
     *
     * @param info 信息
     * @param code 错误码
     */
    public void onNetFailureOperation(JSONObject info, int code) {
//        if (code == Const.NetWork.NET_NOT_LOGIN) {
//            UserDataManager.sharedInstance().doLogout();
//        }
        System.out.println(info.toString());
    }

    /**
     * 成功回调
     *
     * @param result 结果
     */
    @Override
    public void success(String result) {
        Message msg = _handler.obtainMessage();
        msg.what = Const.NetWork.HTTP_SUCCESS;
        msg.obj = result;
        _handler.sendMessage(msg);
    }

    /**
     * 失败回调
     *
     * @param message 失败原因
     */
    @Override
    public void failure(String message) {
        Message msg = _handler.obtainMessage();
        msg.what = Const.NetWork.HTTP_FAILURE;
        msg.obj = message;
        _handler.sendMessage(msg);
    }

    /**
     * 获取申请方法
     *
     * @return 返回申请方法
     */
    protected abstract HttpAction.HttpMethod getMethod();

    /**
     * 获取申请URL
     *
     * @return 返回申请URL
     */
    protected abstract String getUrl();

    /**
     * 获取申请内容
     *
     * @return 返回申请内容
     */
    protected abstract String getHttpContent();

    /**
     * 处理成功
     *
     * @param result  结果
     * @param dataObj 数据
     */
    protected abstract void onNetSuccess(JSONObject result, JSONObject dataObj);

    /**
     * 处理失败
     *
     * @param errorMessage 失败原因
     */
    protected abstract void onNetFailure(String errorMessage);
}
