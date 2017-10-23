package com.zhianjia.m.zhianjia.haier.net.volley;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * @author henzil
 * @version create time:15/1/21_上午11:01
 * @Description TODO
 */
public class VolleyHttpHeaderParser extends HttpHeaderParser {

    /**
     *
     * @param response
     * @return
     */
    public static Cache.Entry volleyParseCacheHeaders(NetworkResponse response) {
        long now = System.currentTimeMillis();
        Map<String, String> headers = response.headers;

        long serverDate = 0;

        String serverEtag = null;

        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = now + 1000 * 60 * 60 * 24 * 365;
        entry.ttl = entry.softTtl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }

}
