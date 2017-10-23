package com.zhianjia.m.zhianjia.haier.util;

import android.content.Context;
import android.util.Log;

import com.haier.uhome.usdk.api.uSDKDevice;
import com.haier.uhome.usdk.api.uSDKManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaoyapeng
 * @version create time:17/5/1616:59
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class HeadUtil {
    public static final String APP_ID = "MB-ZAJ-0000";
    public static final String APP_KEY = "2ca745967cfcae8a43a71c4a2aef0936";
//public static final String APP_KEY = "e2e0dfaa94f868a23bb1e61f5bce2466";
    private static int sequenceIndex = 1;

    public static Map<String, String> buildCommonHeader(java.lang.String accessToken, java.lang.String bodyJson, Context context,boolean isMd5) {
        java.util.Map<java.lang.String, java.lang.String> headerMap = new HashMap();
        headerMap.put("appId", APP_ID);
        headerMap.put("appVersion", AppUtil.getVersionName(context));
        headerMap.put("clientId", getClientId(context));
        headerMap.put("sequenceId", genSequenceId());
        headerMap.put("accessToken", accessToken);

        java.lang.String timeStamp = getTimestamplong();
        headerMap.put("sign", genSign(bodyJson, timeStamp,isMd5));
        headerMap.put("timestamp", timeStamp);
        headerMap.put("language", "zh-cn");
        headerMap.put("timezone", "8");
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        return headerMap;
    }

    public static Map<String, String> buildCommonHeader(java.lang.String accessToken, java.lang.String bodyJson, Context context) {
        java.util.Map<java.lang.String, java.lang.String> headerMap = new HashMap();
        headerMap.put("appId", APP_ID);
        headerMap.put("appVersion", AppUtil.getVersionName(context));
        headerMap.put("clientId", getClientId(context));
        headerMap.put("sequenceId", genSequenceId());
        headerMap.put("accessToken", accessToken);

        java.lang.String timeStamp = getTimestamplong();
        headerMap.put("sign", genSign(bodyJson, timeStamp,true));
        headerMap.put("timestamp", timeStamp);
        headerMap.put("language", "zh-cn");
        headerMap.put("timezone", "8");
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        return headerMap;
    }


    /**
     * uSDK provide clientId generator
     * uSDK提供內置clientId生成方法
     */
    private static String getClientId(Context context) {
        uSDKManager uSDKMgr = uSDKManager.getSingleInstance();
        String clientId = uSDKMgr.getClientId(context);
        return clientId;
    }

    public static String genSequenceId() {
        String timestamp = genTimestamp();
        String sequenceId = timestamp + String.format("%06d", sequenceIndex++);
        return sequenceId;
    }

    public static String genTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        return timestamp;
    }

    public static String getTimestamplong() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 生成Sign字段值
     * make sign value
     */
    public static String genSign(String bodyJSON, String timeStamp,boolean isMD5) {
        StringBuffer signStringBuffer = new StringBuffer();


        bodyJSON = bodyJSON.replaceAll(" ", "");
        bodyJSON = bodyJSON.replaceAll("\t", "");
        bodyJSON = bodyJSON.replaceAll("\r", "");
        bodyJSON = bodyJSON.replaceAll("\n", "");
        Log.e("tag","bodyJSON="+bodyJSON);

        signStringBuffer = signStringBuffer.append(bodyJSON)
                .append(APP_ID)
                .append(APP_KEY)
                .append(timeStamp);

        String signOrgData = signStringBuffer.toString();


        byte[] signByteArray = null;
        byte[] md5ByteArray = null;

        try {
            signByteArray = signOrgData.getBytes("utf-8");
            MessageDigest messageDigest;

            if(isMD5){
                Log.e("tag","messageDigest1");
                messageDigest = MessageDigest.getInstance("MD5");
            }else{
                Log.e("tag","messageDigest2");
                messageDigest = MessageDigest.getInstance("SHA-256");
            }
            md5ByteArray = messageDigest.digest(signByteArray);

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        StringBuffer signHexBuffer = new StringBuffer();
        for (int i = 0; i < md5ByteArray.length; i++) {
            String hexStr = Integer.toHexString(0xff & md5ByteArray[i]);
            if(hexStr.length() == 1) {
                signHexBuffer.append('0');
            }
            signHexBuffer.append(hexStr);
        }


//
//        StringBuilder hex = new StringBuilder();
//        String hexStr = "0123456789abcdef";
//        for (int i = 0; i < md5ByteArray.length; i++) {
//            hex.append(String.valueOf(hexStr.charAt((md5ByteArray[i] & 0xF0) >> 4)));
//            hex.append(String.valueOf(hexStr.charAt(md5ByteArray[i] & 0x0F)));
//        }

        Log.e("tag","StringBuilder1="+signHexBuffer);
        Log.e("tag","StringBuilder2="+signHexBuffer.toString().toLowerCase());
//        Log.e("tag","StringBuilder2="+hex);

        return signHexBuffer.toString().toLowerCase();
    }

    /*
     * 绑定时参数贫瘠
	 * Add SmartDevice Info to U+ Account
	 */
    public static  String buildBindDeviceContent(String session, List<uSDKDevice> devices) {
        JSONObject job = new JSONObject();
        JSONArray jArray = new JSONArray();
        try {
            for (uSDKDevice smartDevice : devices) {
                JSONObject deviceJob = new JSONObject();

                //添加id
                deviceJob.put("id", smartDevice.getDeviceId());

                //添加name
                deviceJob.put("name", "smartdevice");

                //添加typeInfo
                JSONObject typeInfoJson = new JSONObject();
                typeInfoJson.put("typeId", smartDevice.getUplusId());
                deviceJob.put("typeInfo", typeInfoJson);

                //添加deviceModules
                JSONArray deviceModuleArray = new JSONArray();
                deviceJob.put("deviceModules", deviceModuleArray);

                //添加Wifi的模組信息
                JSONObject wifimoduleJson = new JSONObject();
                deviceModuleArray.put(wifimoduleJson);

                //wifimodule有三個小項目：
                wifimoduleJson.put("moduleId", smartDevice.getDeviceId());
                wifimoduleJson.put("moduleType", "wifimodule");
                JSONArray wifimoduleInfoArray = new JSONArray();
                wifimoduleJson.put("moduleInfos", wifimoduleInfoArray);

                //拼裝moduleInfos信息wifimoduleJson
                //ip
                JSONObject info_ip = new JSONObject();
                info_ip.put("key", "ip");
                info_ip.put("value", smartDevice.getIp());
                wifimoduleInfoArray.put(info_ip);

                //hardwareVers
                JSONObject info_hardwareVers = new JSONObject();
                info_hardwareVers.put("key", "hardwareVers");
                info_hardwareVers.put("value", smartDevice.getSmartLinkHardwareVersion());
                wifimoduleInfoArray.put(info_hardwareVers);

                //softwareVers
                JSONObject info_softwareVers = new JSONObject();
                info_softwareVers.put("key", "softwareVers");
                info_softwareVers.put("value", smartDevice.getSmartLinkSoftwareVersion());
                wifimoduleInfoArray.put(info_softwareVers);

                //configVers
                JSONObject info_configVers = new JSONObject();
                info_configVers.put("key", "configVers");
                info_configVers.put("value", smartDevice.getSmartLinkDevfileVersion());
                wifimoduleInfoArray.put(info_configVers);

                //protocolVers
                JSONObject info_protocolVers = new JSONObject();
                info_protocolVers.put("key", "protocolVers");
                info_protocolVers.put("value", smartDevice.getEProtocolVer());
                wifimoduleInfoArray.put(info_protocolVers);

                //platform
                JSONObject info_platform = new JSONObject();
                info_platform.put("key", "platform");
                info_platform.put("value", smartDevice.getSmartlinkPlatform());
                wifimoduleInfoArray.put(info_platform);

                //supportUpgrade
                JSONObject info_supportUpgrade = new JSONObject();
                info_supportUpgrade.put("key", "supportUpgrade");
                info_supportUpgrade.put("value", "1");
                wifimoduleInfoArray.put(info_supportUpgrade);

                //
                jArray.put(deviceJob);

            }
            job.put("devices", jArray);
            Log.e("tag","job="+job);
            return job.toString();

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }
}
