package com.zhianjia.m.zhianjia.haier.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zhaoyapeng
 * @version create time:17/5/1900:29
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class SharedPreferencesDevice  {

    public static  void saveDevice(Context mContext,String deviceId){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("deviceSharedPreferencesDevice", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("deviceId", deviceId);
        editor.commit();//提交修改
    }

    public static String getSaveDeviceId(Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("deviceSharedPreferencesDevice", Context.MODE_PRIVATE); //私有数据
        return  sharedPreferences.getString("deviceId","");
    }


    public static  void saveAccessToken(Context mContext,String accessToken){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("accessTokenName", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("accessToken", accessToken);
        editor.commit();//提交修改
    }

    public static String getAccessToken(Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("accessTokenName", Context.MODE_PRIVATE); //私有数据
        return  sharedPreferences.getString("accessToken","");
    }

    public static  void saveZajToken(Context mContext,String accessToken){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("zajTokenName", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("token", accessToken);
        editor.commit();//提交修改
    }

    public static String getAzjToken(Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("zajTokenName", Context.MODE_PRIVATE); //私有数据
        return  sharedPreferences.getString("token","");
    }

    public static void clear(Context mContext){
        SharedPreferences sharedPreferences1 = mContext.getSharedPreferences("deviceSharedPreferencesDevice", Context.MODE_PRIVATE); //私有数据
        sharedPreferences1.edit().clear().commit();

        SharedPreferences sharedPreferences2 = mContext.getSharedPreferences("accessTokenName", Context.MODE_PRIVATE); //私有数据
        sharedPreferences2.edit().clear().commit();

        SharedPreferences sharedPreferences3 = mContext.getSharedPreferences("zajTokenName", Context.MODE_PRIVATE); //私有数据
        sharedPreferences3.edit().clear().commit();

    }

}
