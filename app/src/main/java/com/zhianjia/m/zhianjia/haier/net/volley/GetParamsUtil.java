package com.zhianjia.m.zhianjia.haier.net.volley;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author zhaoyapeng
 * @version create time:15/11/10上午9:09
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class GetParamsUtil {

    public static String getParmas(String url, Map<String, String> params) {
        String tempURL = url+"?";
        try{

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                tempURL = tempURL + key + "=" + URLEncoder.encode(params.get(key),"UTF-8") + "&";
            }
        }

        }catch (Exception e){

        }
        return tempURL;
    }

    public static String getObjParmas(String url, Map<String, Object> params) {
        String tempURL = url+"?";
        try{

            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    if (params.get(key)!=null){
                        tempURL = tempURL + key + "=" + URLEncoder.encode(params.get(key).toString(),"UTF-8") + "&";
                    }else {
                        tempURL = tempURL + key + "=" + "&";
                    }

                }
            }

        }catch (Exception e){

        }
        return tempURL;
    }
}
