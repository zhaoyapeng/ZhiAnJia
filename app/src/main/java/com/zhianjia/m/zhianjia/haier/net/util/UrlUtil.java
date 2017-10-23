package com.zhianjia.m.zhianjia.haier.net.util;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.utils.Utils;

import java.security.MessageDigest;
import java.util.Map;

/**
 * @author zhaoyapeng
 * @version create time:17/8/2915:48
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class UrlUtil {

    public static String getMd5(String str){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes("UTF-8"));
            byte b[] = md5.digest();

            int i;
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();
        }catch (Exception e){

        }
        return "";
    }


    public static String getCommonParam(Context mContext,String flag,Map<String ,String > params){

//        Log.e("tag","parms1="+params);
        String timestamp =  System.currentTimeMillis()+"";
        String sign = getSign(flag,params,timestamp);
        String url = "?appver="+mContext.getString(R.string.appver)+"&sign="+sign+"&devicetype=android"+ Build.VERSION.RELEASE +"&timestamp="+timestamp;
//        Log.e("tag","parms_url="+url);
        return url;
    }


    public static String getSign(String flag,Map<String ,String >params,String timestamp){


         String eNo = flag+"|"+new Gson().toJson(params)+"|"+timestamp;
        String eYes = Utils.md5(eNo);
//        Log.e("tag","加密前="+eNo);
//        Log.e("tag","加密后1="+ eYes  );
        if(!TextUtils.isEmpty(eYes)){
            eYes = eYes.substring(eYes.length()-9,eYes.length());
        }
//        Log.e("tag","加密后2="+ eYes  );
        return eYes;
    }
}
