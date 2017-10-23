package com.zhianjia.m.zhianjia.components.network.method;

import android.graphics.Bitmap;
import android.os.Build;

import java.net.HttpURLConnection;

import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by zizi-PC on 2016/7/1.
 * 网络交互方法
 */

public abstract class HttpAction {
    /**
     * 网络交互类型
     */
    public enum HttpMethod {
        POST,
        POST_UPLOAD_BITMAP,
        GET
    }

    protected String url_str;
    protected Bitmap uploadBitmap;
    protected String resultData = "", fileName, serverName;
    protected HttpMethod httpMethod;
    protected HttpActionListener listener;
    protected HttpURLConnection urlConn = null;

    /**
     * 构造函数
     *
     * @param listener  结果监听
     * @param urlString URL
     */
    public HttpAction(HttpActionListener listener, String urlString) {
        this.listener = listener;
        this.url_str = urlString;
    }

    /**
     * 获取URL
     *
     * @return 返回URL
     */
    public String getUrlString() {
        return this.url_str;
    }

    /**
     * 获取申请方式
     *
     * @return 返回申请方式
     */
    public HttpMethod getMethod() {
        return this.httpMethod;
    }

    /**
     * 获取签名
     *
     * @param content 交互数据内容
     * @return 返回签名信息
     */
    private String getSing(String content) {
        String timestampStr = System.currentTimeMillis() + "";
        String md5 = Utils.md5("sWD3wd#d5|" + content.trim() + "|" + timestampStr);
        String sign = "&sign=" + md5.substring(md5.length() - 9).toUpperCase();
        return "timestamp=" + timestampStr + sign;
    }

    /**
     * 获取手机、应用信息
     *
     * @return 返回手机、应用信息
     */
    private String getPhoneInfo() {
        int currentApiVersion = Build.VERSION.SDK_INT;
        String appVer = "&appver=" + Utils.getAppVersionName();
        String sysVer = "devicetype=android" + currentApiVersion;
        return sysVer + appVer;
    }

    /**
     * 发送申请
     *
     * @param content 申请内容
     */
    public void doHttpRequest(String content) {
        if (httpMethod == HttpMethod.POST || httpMethod == httpMethod.POST_UPLOAD_BITMAP) {
            //System.out.println("sign content = " + content);
            String signStr = getSing(content);
            if (!url_str.endsWith("?")) {
                url_str += "?";
            }
            url_str += getPhoneInfo() + "&" + signStr;
        }
        doHttpMethod(content);
    }

    /**
     * 设定上传图像
     *
     * @param uploadBitmap 上传的BITMAP
     * @param fileName     文件名
     * @param serverName   上传名称
     */
    public void setUploadBitmap(Bitmap uploadBitmap, String fileName, String serverName) {
        this.uploadBitmap = uploadBitmap;
        this.fileName = fileName;
        this.serverName = serverName;
    }

    /**
     * 上传操作
     *
     * @param content 内容
     */
    protected abstract void doHttpMethod(String content);

}

