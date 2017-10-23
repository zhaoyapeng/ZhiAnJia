package com.zhianjia.m.zhianjia.haier.util;

import android.content.Context;

import com.haier.uhome.account.model.UacDevice;
import com.zhianjia.m.zhianjia.haier.AppDemo;

/**
 * @author zhaoyapeng
 * @version create time:17/5/1900:53
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class GetDataByIdUtil {
    public static UacDevice getUacDeviceById(Context mContext, String deviceId) {
        AppDemo appDemo = (AppDemo) mContext.getApplicationContext();

//        if (appDemo.getUserAccount() != null && appDemo.getUserAccount().getDevicesOfAccount() != null) {
//            UacDevice[] devices = appDemo.getUserAccount().getDevicesOfAccount();
//
//            for (int i = 0; i < devices.length; i++) {
//                if (devices[i].getId().equals(deviceId)) {
//                    return devices[i];
//                }
//            }
//        }

        return new UacDevice();
    }
}
