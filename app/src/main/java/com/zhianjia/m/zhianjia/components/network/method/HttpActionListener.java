package com.zhianjia.m.zhianjia.components.network.method;

/**
 * Created by zizi-PC on 2016/7/1.
 * 网络交互事件
 */

public interface HttpActionListener {
    /**
     * 成功
     *
     * @param result 结果
     */
    void success(String result);

    /**
     * 失败
     *
     * @param message 原因
     */
    void failure(String message);
}
