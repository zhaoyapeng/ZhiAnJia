package com.zhianjia.m.zhianjia.config;

/**
 * Created by Zibet.K on 2016/7/3.
 */
public class Const {

    public static class NetWork {
        public static String netConfig = "[Config]\r\nIsDebug=1\r\nLocalBasePort=8200\r\nIsCaptureDev=1\r\nIsPlayDev=1\r\nUdpSendInterval=2\r\nConnectTimeout=10000\r\nTransferTimeout=10000\r\n[Tracker]\r\nCount=3\r\nIP1=121.42.156.148\r\nPort1=80\r\nIP2=61.55.189.131\r\nPort2=80\r\nIP3=223.202.103.130\r\nPort3=80\r\n[LogServer]\r\nCount=1\r\nIP1=223.202.103.147\r\nPort1=80\r\n";
        public static int HTTP_SUCCESS = 1;
        public static int HTTP_FAILURE = 2;

        public static int NET_SUCCESS_CODE = 0;
        public static int NET_NOT_LOGIN = 101;

        public static String BASE_URL = "http://api.bjzhianjia.com/";
        public static String BASE_WEB_URL = "http://home.bjzhianjia.com/";

        public static String SOCKET_ADDRESS = "192.168.4.1";
        public static int SOCKET_PORT = 8080;
    }

    public static class Sounds {
        public static final int INCOME_RING = 1;
        public static final int FINISH_RING = 2;
    }

    public static class DataBase {
        public static final String CALL_RECORD_DATABASE_NAME = "Record";
        public static final int CALL_RECORD_DATABASE_VERSION = 1;
    }

    public static class Actions {
        private static final String packageName = "com.zhianjia.m.zhianjia.";
        public static final String ACTION_ACTIVITY_LOGIN = packageName + "LoginActivity";
        public static final String ACTION_ACTIVITY_MAIN = packageName + "DeviceDetailActivity";
        public static final String ACTION_ACTIVITY_CUT_AVATAR = packageName + "CutAvatarActivity";
        public static final String ACTION_ACTIVITY_FIND_PASSWORD = packageName + "FindPasswordActivity";
        public static final String ACTION_ACTIVITY_REGISTER_MOBILE = packageName + "RegisterMobileActivity";
        public static final String ACTION_ACTIVITY_REGISTER_INFORMATION = packageName + "RegisterInfoActivity";
        public static final String ACTION_ACTIVITY_USER_PROFILE = packageName + "UserProfile";
        public static final String ACTION_ACTIVITY_DEVICE_HISTORY = packageName + "DeviceHistoryActivity";
        public static final String ACTION_ACTIVITY_DEVICE_LIST = packageName + "DeviceListActivity";
        public static final String ACTION_ACTIVITY_WIFI_DETECT = packageName + "WifiDetectActivity";
        public static final String ACTION_ACTIVITY_DEVICE_RENAME = packageName + "DeviceRenameActivity";
        public static final String ACTION_ACTIVITY_CONNECTING = packageName + "ConnectingActivity";
        public static final String ACTION_ACTIVITY_OPTION = packageName + "OptionActivity";
        public static final String ACTION_ACTIVITY_ABOUT = packageName + "AboutActivity";
        public static final String ACTION_ACTIVITY_MODIFYPWD = packageName + "ModifyPasswordActivity";
        public static final String ACTION_ACTIVITY_WIFI_UPLOAD = packageName + "WifiUploadActivity";
        public static final String ACTION_ACTIVITY_DEVICE_ADD = packageName + "DeviceAddActivity";
        public static final String ACTION_ACTIVITY_WIFI_LIST = packageName + "WifiListActivity";
    }

    public static class FileKey {
        public static String ICON_KEY = "icon";
        public static String USER_LIST_KEY = "userList";
        public static String CURRENT_USER_KEY = "currentUser";
    }

    public static class Update {

        public final static String VERSION_FILE = "AppVersion";
        public final static int BUFFER_SIZE = 102400;
        /* 下载中 */
        public static final int DOWNLOADING = 1;
        /* 下载结束 */
        public static final int DOWNLOAD_FINISH = 2;
        public static final int DOWNLOAD_CANCEL = 3;
        public static final int DOWNLOAD_ERROR = 4;

    }

    public static class IntentKeyConst {
        public static final String KEY_UID = "UID";
        public static final String KEY_PUSH_ID = "PUSH_ID";
        public static final String KEY_NICKNAME = "NICKNAME";
        public static final String KEY_FROM_WHERE = "REFRESH_FROM_WHERE";
        public static final String KEY_P2P_URL = "KEY_P2P_URL";
        public static final String KEY_FIND_PASSWORD = "KEY_FIND_PASSWORD";
        public static final String KEY_MOBILE = "KEY_MOBILE";
        public static final String KEY_CODE = "KEY_CODE";
        public static final String KEY_AVATAR_URI = "KEY_AVATAR_URI";
    }
}