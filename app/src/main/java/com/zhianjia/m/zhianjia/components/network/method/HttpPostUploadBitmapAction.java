package com.zhianjia.m.zhianjia.components.network.method;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.zhianjia.m.zhianjia.components.ThreadManager;

/**
 * Created by Zibet.K on 2016/8/3.
 * post 上传
 */

public class HttpPostUploadBitmapAction extends HttpAction {

    /**
     * 构造函数
     *
     * @param listener  结果监听
     * @param urlString URL
     */
    public HttpPostUploadBitmapAction(HttpActionListener listener, String urlString) {
        super(listener, urlString);
        this.httpMethod = HttpMethod.POST_UPLOAD_BITMAP;
    }

    /**
     * 申请方法
     *
     * @param content 内容
     */
    @Override
    public void doHttpMethod(String content) {
        ThreadManager.sharedInstance().runNewThread(
                new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            uploadBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                            byte[] buffer1 = out.toByteArray();
                            boolean isSuccess = true;
                            try {
                                //Get方式
                                //HttpURLConnection_Get();
                                //Post方式
                                //System.out.println("POST UPLOAD IMAGE " + url_str);
                                HttpURLConnection_Post(buffer1);
                                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                                BufferedReader buffer = new BufferedReader(in);
                                String inputLine;
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
    }

    /**
     * post 发送数据
     *
     * @param content 数据内容
     */
    private void HttpURLConnection_Post(byte[] content) {
        try {
            //通过openConnection 连接

            String end = "\r\n";
            String Hyphens = "--";
            String boundary = "----WebKitFormBoundarymUHhJDniXQiBHi3r";
            URL url = new java.net.URL(url_str);
            urlConn = (HttpURLConnection) url.openConnection();
            //设置输入和输出流
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Charset", "UTF-8");
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            urlConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            urlConn.connect();
            //DataOutputStream流
            DataOutputStream ds = new DataOutputStream(urlConn.getOutputStream());
            ds.writeBytes(Hyphens + boundary + end);
            ds.writeBytes("Content-Disposition:form-data;"
                    + "name=\"" + serverName + "\";filename=\"" + fileName + "\"" + end);
            ds.writeBytes("Content-Type: image/jpeg" + end);
            ds.writeBytes(end);
            ds.write(content);
            ds.writeBytes(end);
            ds.writeBytes(Hyphens + boundary + Hyphens + end);
            ds.flush();

        } catch (Exception e) {
            resultData = "连接超时";
            if (listener != null) {
                listener.failure(resultData);
            }
            e.printStackTrace();
        }
    }
}

