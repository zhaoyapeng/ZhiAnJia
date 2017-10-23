package com.zhianjia.m.zhianjia.haier;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.haier.uhome.usdk.api.ConfigurableDevice;
import com.haier.uhome.usdk.api.interfaces.IDeviceScanListener;
import com.haier.uhome.usdk.api.uSDKDeviceManager;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import com.haier.uhome.usdk.api.uSDKManager;
import com.zhianjia.m.zhianjia.haier.data.UserAccount;
import com.zhianjia.m.zhianjia.utils.FileUtils;

public class AppDemo extends Application {

    private Handler mainActivityHandler = null;

    public void setMainActivityMsgHandler(Handler handler) {
        mainActivityHandler = handler;
    }

    private String[] permissionStringArray = null;

    public String[] getPermissionStringArray() {
        return permissionStringArray;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uSDKManager sdkMgr = uSDKManager.getSingleInstance();
        sdkMgr.init(this);
    }

    private class HelpSmartDeviceWaiting2JoinWifiUtil implements IDeviceScanListener {

        private static final long serialVersionUID = 3899276926320512661L;

        @Override
        public void onDeviceRemoved(ConfigurableDevice candidater) {
            System.out.println(candidater.getDevIdSuffix() +
                    " has been disappeared!");
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(100);

        }

        /**
         * 扫描WIFI被拒绝时，回调此方法
         */
        @Override
        public void onPermissionDenied(String[] notGrantedPermission, String[] failedGrantedPermission) {
            permissionStringArray = failedGrantedPermission;
            if (mainActivityHandler != null) {
                mainActivityHandler.obtainMessage().sendToTarget();
            }

        }

        @Override
        public void onDeviceScanned(ConfigurableDevice candidater) {
            System.out.println("uSDK3.x Demo find:" + candidater.getDevIdSuffix());
            popNotiFoundunConfigedSmartDevice(candidater);

        }
    }

    private HelpSmartDeviceWaiting2JoinWifiUtil helperUtil =
            new HelpSmartDeviceWaiting2JoinWifiUtil();

    /**
     * <pre>
     * Some U+ smart devices support wifi discovery mode,
     * we could find a wifi that it's name is uplus-xxxx-xxxx.
     * It will join your home, if you smartlink it.
     * uSDK3.x support this function, steps are:
     *
     * 一些未入網的智能設備上電后會對外廣播自己，如果此時用手機查看可以
     * 看到一個WIFI，名稱是uplus-xxxx-xxxx，這時只要執行smartlink，
     * 就可以把它配置入網。按如下步驟編程，實現發現待配置設備：
     *
     * Step:
     * 1.registerDeviceScanListener 注冊
     * 2.startScanConfigurableDevice 開始掃描
     * 3.stopScan 結束掃描，退出掃描自發現設備
     * </pre>
     */
    public void helpSmartDeviceAutoJoinWifi(uSDKDeviceManager uSDKDeviceMgr) {
        uSDKDeviceMgr.setDeviceScanListener(helperUtil);
        uSDKErrorConst result = uSDKDeviceMgr.startScanConfigurableDevice(this);

        //return error if wifi is off
        if (result == uSDKErrorConst.RET_USDK_OK) {

        }
    }

    /**
     * <pre>
     * stop uSDK3.x scanning smart devices waiting join wifi.
     * 停止uSDK繼續掃描自發現智能設備。
     * </pre>
     */
    public void endScanWaitingSmartDevice(uSDKDeviceManager uSDKDeviceMgr) {
        uSDKDeviceMgr.stopScanConfigurableDevice();
    }

    private void popNotiFoundunConfigedSmartDevice(ConfigurableDevice candidater) {
//		String contentTitle = candidater.getDevIdSuffix() + getString(R.string.tip_smartdevice_noti_appear);
//		String contentText = getString(R.string.tip_click_2_smartlink);
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//						.setSmallIcon(R.drawable.app_icon)
//						.setContentTitle(contentTitle)
//						.setContentText(contentText);
//
//		Intent resultIntent = new Intent(this, MakeDevice2NetSmartLink.class);
//		resultIntent.putExtra("mac", candidater.getDevIdSuffix());
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//		stackBuilder.addParentStack(MakeDevice2NetSmartLink.class);
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent =
//				stackBuilder.getPendingIntent(
//						0,
//						PendingIntent.FLAG_ONE_SHOT
//				);
//		mBuilder.setContentIntent(resultPendingIntent);
//		NotificationManager mNotificationManager =
//				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		mNotificationManager.notify(100, mBuilder.build());

    }


    private UserAccount userAccount = new UserAccount();

//    public UserAccount getUserAccount() {
//        if (userAccount == null || TextUtils.isEmpty(userAccount.getSession())) {
//            if (FileUtils.getDefaultHaierUser() != null)
//                userAccount = FileUtils.getDefaultHaierUser();
//        }
//
//        return userAccount;
//    }
}
