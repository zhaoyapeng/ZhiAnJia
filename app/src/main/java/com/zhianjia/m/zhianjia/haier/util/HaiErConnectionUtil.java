package com.zhianjia.m.zhianjia.haier.util;

import android.content.Context;
import android.util.Log;

import com.haier.uhome.account.model.UacDevice;
import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.uSDKCloudConnectionState;
import com.haier.uhome.usdk.api.uSDKDeviceManager;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import com.zhianjia.m.zhianjia.haier.AppDemo;
import com.zhianjia.m.zhianjia.haier.data.UserAccount;

import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/5/1816:28
 * @Email zyp@jusfoun.com
 * @Description ${海尔 工具类}
 */
public class HaiErConnectionUtil {
    private Context mContext;
    private uSDKDeviceManager uSDKDeviceMgr;


    public HaiErConnectionUtil(Context mContext) {
        this.mContext = mContext;
        uSDKDeviceMgr  =uSDKDeviceManager.getSingleInstance();
    }

    /**
     * 开始接入网关
     * <p>
     * 获取到 设备列表之后 接入网关 一次
     * 绑定设备成功之后 接入网关一次
     */
    public void startGetWay() {
        uSDKCloudConnectionState cloudConnectionState =
                uSDKDeviceMgr.getCloudConnectionState();
        Log.e("tag", "connect2RemoteServerWhenLogin1");
        if (cloudConnectionState !=
                uSDKCloudConnectionState.CLOUD_CONNECTION_STATE_CONNECTED) {

            Log.e("tag", "connect2RemoteServerWhenLogin2");
            connect2RemoteServerWhenLogin();

        }
    }


    /**
     * 接入网关
     */
    private void connect2RemoteServerWhenLogin() {
//        AppDemo appDemo = (AppDemo)mContext.getApplicationContext();
//        UserAccount userAccount = appDemo.getUserAccount();
//
//        UacDevice[] devicesOfAccount = userAccount.getDevicesOfAccount();
//        String token = userAccount.getSession();
//
////		VT􏴸SOI􏰣V􏴺usermg.uopendev.haier.net􏴸P􏵋􏴸􏴹􏰣􏴫DK􏴸S􏰣P􏴸U
//
//        uSDKDeviceManager deviceManager = uSDKDeviceManager.getSingleInstance();
//        List remotedCtrledDeviceCollection =
//                Util.fillDeviceInfoForRemoteControl(devicesOfAccount);
//
//
////        usermg.uopendev.haier.net 56821
//        deviceManager.connectToGateway(token, "gw.haier.net", 56811,
//                remotedCtrledDeviceCollection,
//                new IuSDKCallback() {
//
//                    @Override
//                    public void onCallback(uSDKErrorConst result) {
//                        if (result != uSDKErrorConst.RET_USDK_OK) {
//                            System.err.println("uSDK3XDemo exec connectToGateway method failed! " + result);
//                        } else {
//                            Log.e("tag", "网关接入成功");
//                        }
//                    }
//                });

    }


}
