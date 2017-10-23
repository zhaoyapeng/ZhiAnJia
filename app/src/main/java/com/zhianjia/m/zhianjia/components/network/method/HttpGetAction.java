package com.zhianjia.m.zhianjia.components.network.method;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Zibet.K on 2016/7/29.
 * Get方法
 */

public class HttpGetAction extends HttpAction {
    /**
     * 构造函数
     * @param listener 结果监听
     * @param urlString URL
     */
    public HttpGetAction(HttpActionListener listener, String urlString) {
        super(listener, urlString);
        this.httpMethod = HttpMethod.GET;
    }

    /**
     * 申请方法
     * @param content 内容
     */
    @Override
    public void doHttpMethod(String content) {
        url_str += content;
        /*建立HTTP Get对象*/
        HttpGet httpRequest = new HttpGet(url_str);
        try {
          /*发送请求并等待响应*/
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
          /*若状态码为200 ok*/
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
            /*读*/
                resultData = EntityUtils.toString(httpResponse.getEntity());
                listener.success(resultData);
            } else {
                resultData = httpResponse.getStatusLine().toString();
                if (listener != null) {
                    listener.failure(resultData);
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            resultData = e.toString();
            if (listener != null) {
                listener.failure(resultData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultData = e.toString();
            if (listener != null) {
                listener.failure(resultData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultData = e.toString();
            if (listener != null) {
                listener.failure(resultData);
            }
        }
    }
}
