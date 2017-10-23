package com.zhianjia.m.zhianjia.components.network.method;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.zhianjia.m.zhianjia.components.ThreadManager;

/**
 * Created by zizi-PC on 2016/7/1.
 */

public class HttpPostAction extends HttpAction {

    /**
     * 构造函数
     *
     * @param listener  结果监听
     * @param urlString URL
     */
    public HttpPostAction(HttpActionListener listener, String urlString) {
        super(listener, urlString);
        this.httpMethod = HttpMethod.POST;
    }

    /**
     * 申请方法
     *
     * @param content 内容
     */
    @Override
    public void doHttpMethod(final String content) {
        ThreadManager.sharedInstance().runNewThread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = true;
                try {
                    //System.out.println("POST " + url_str);
                    HttpURLConnection_Post(content);
                    InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                    BufferedReader buffer = new BufferedReader(in);
                    String inputLine = null;
                    while (((inputLine = buffer.readLine()) != null)) {
                        resultData += inputLine + "\n";
                    }
                    in.close();

                } catch (Exception e) {
                    isSuccess = false;
                    resultData = e.toString();
                    e.printStackTrace();
                } finally {
                    try {
                        //关闭连接
                        urlConn.disconnect();
                        if (listener != null) {
                            if (isSuccess)
                                listener.success(resultData);
                            else
                                listener.failure(resultData);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 发送数据
     *
     * @param content 内容
     */
    private void HttpURLConnection_Post(String content) {
        try {
            //通过openConnection 连接
            URL url = new java.net.URL(url_str);
            urlConn = (HttpURLConnection) url.openConnection();
            //设置输入和输出流
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);

            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            urlConn.setRequestProperty("Content-Type", "application/json");

            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            urlConn.connect();
            //DataOutputStream流
            urlConn.getOutputStream().write(content.getBytes());

        } catch (Exception e) {
            resultData = "连接超时";
            if (listener != null) {
                listener.failure(resultData);
            }
            e.printStackTrace();
        }
    }
}
