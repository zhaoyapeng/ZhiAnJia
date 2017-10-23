package com.zhianjia.m.zhianjia.haier.net.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author zhaoyapeng
 * @version create time:15/11/2上午11:36
 * @Email zyp@jusfoun.com
 * @Description ${网络请求工具类}
 */
public class VolleyUtil {
    private static RequestQueue requestQueue;
    private static ImageLoader imageLoader;

    private static PriorityBlockingQueue<Request<?>> mNetworkQueue ;

    public static RequestQueue getQueue(Context mContext) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }

        return requestQueue;
    }

    public static PriorityBlockingQueue getPriorityBlockingQueue(Context mContext) {
        if (mNetworkQueue == null) {
            mNetworkQueue = new PriorityBlockingQueue<Request<?>>();
        }
        return mNetworkQueue;
    }

}
