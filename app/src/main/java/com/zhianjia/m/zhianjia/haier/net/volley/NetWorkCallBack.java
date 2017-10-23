package com.zhianjia.m.zhianjia.haier.net.volley;



/**
 * @author zhaoyapeng
 * @version create time:17/3/1416:22
 * @Email zyp@jusfoun.com
 * @Description ${网络回调}
 */
public interface NetWorkCallBack {

    void onSuccess(Object data);

    void onFail(String error);
}
