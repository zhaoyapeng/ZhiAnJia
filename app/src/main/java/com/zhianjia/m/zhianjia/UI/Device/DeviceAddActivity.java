package com.zhianjia.m.zhianjia.UI.Device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haier.uhome.usdk.api.interfaces.IuSDKSmartLinkCallback;
import com.haier.uhome.usdk.api.uSDKDevice;
import com.haier.uhome.usdk.api.uSDKDeviceManager;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.components.data.Device;
import com.zhianjia.m.zhianjia.components.network.SocketManager;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.events.RefreshDevicesEvent;
import com.zhianjia.m.zhianjia.events.SelectWifiEvent;
import com.zhianjia.m.zhianjia.haier.dialog.SmartConfigDialog;
import com.zhianjia.m.zhianjia.haier.net.route.DeviceDetailRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.util.WifiUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DeviceAddActivity extends BaseAppCompatActivity {

    private static final String TAG = "DeviceAddActivity";

    private NavigationBar navigationBar;
    private EditText ssidEditText, passwordEditText;
    private Button commitButton;
    private RelativeLayout connectingLayout, wifiDetectLayout, wifiUploadLayout;
    private TextView countDownTextView;
    private ImageButton wifiDetectImageButton;
    private TextView wifiDetectTextView;
    ImageButton ssidImageButton;

    private String deviceId;

    private int currentStatus = Status.CONNECTING;

    private int countDown = 30;

    public static final  String UNBIND ="UNBIND";
    private boolean unBind = false;

    private static class Status {
        public static int CONNECTING = 1000;
        public static int WIFIDETECT = 1001;
        public static int WIFIUPLOAD = 1002;
    }



    private uSDKDeviceManager uSDKDeviceMgr = uSDKDeviceManager.getSingleInstance();
    private SmartConfigDialog configProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unBind = getIntent().getBooleanExtra(UNBIND,false);
//        registerReceiver();
//        tryToConnectSocket();
        EventBus.getDefault().register(this);


        configProgressDialog = new SmartConfigDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        unregisterReceiver();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeDeviceEventHandler(SelectWifiEvent event) {
        ssidEditText.setText(event.wifi.getSsid());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshDevicesEventHandler(RefreshDevicesEvent event) {
        if (deviceId == null || "".equals(deviceId)) {
            return;
        }

        for (Device device:event.devices) {
            if (deviceId.toLowerCase().equals(device.getId().toLowerCase())) {
                Utils.showToast("设备添加成功");
                finish();
                break;
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_device_add;
    }

    @Override
    protected void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        ssidEditText = (EditText) findViewById(R.id.et_ssid);
        passwordEditText = (EditText) findViewById(R.id.et_pwd);
        commitButton = (Button) findViewById(R.id.commit_button);
        connectingLayout = (RelativeLayout) findViewById(R.id.rl_connecting);
        wifiDetectLayout = (RelativeLayout) findViewById(R.id.rl_wifidetect);
        wifiUploadLayout = (RelativeLayout) findViewById(R.id.rl_wifiupload);
        countDownTextView = (TextView) findViewById(R.id.tv_countdown);
        wifiDetectImageButton = (ImageButton) findViewById(R.id.ib_wifidetect);
        wifiDetectTextView = (TextView) findViewById(R.id.tv_wifidetect);
        ssidImageButton = (ImageButton) findViewById(R.id.ib_ssid);

        ssidEditText.setText(WifiUtils.getWifiTitle(this));
    }

    @Override
    protected void initView() {
        navigationBar.setNavigationLoginStyle("添加设备");

        setCurrentStatus(Status.CONNECTING);
        countDownTextView.setText("");

//        passwordEditText.setText("gktfude5");
    }

    @Override
    protected void setListener() {
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });

        wifiDetectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        wifiDetectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        ssidImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_WIFI_LIST);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                DeviceAddActivity.this.startActivity(intent);
            }
        });
    }

    private void commit() {
        String ssid = ssidEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (ssid == null || "".equals(ssid)) {
            Utils.showToast("网络名称不能为空");
            return;
        }

        if (password == null || "".equals(password)) {
            Utils.showToast("密码不能为空");
            return;
        }
        configProgressDialog.show();

        /**
         * exec smartlink with wifi(name、pass、mac), then set timeout 60s
         * set timeout 90s when device type is air condition or air cleaner
         * false:support safety and common uPlug, true:safety only
         *
         * 執行智能設備配置入網：空調、空氣凈化器類設備使用90s超時
         * false：支持安全產品和非安全產品（upLug)，true：僅支持安全uPlug
         */
        uSDKDeviceMgr.configDeviceBySmartLink(
                ssid, password, null, 60, false, new IuSDKSmartLinkCallback() {

                    @Override
                    public void onSmartLinkCallback(
                            uSDKDevice deviceJustJoined, uSDKErrorConst reason) {
                        if (reason == uSDKErrorConst.RET_USDK_OK) {
                            System.out.println(getString(R.string.code_config_ok_device) + deviceJustJoined);
                            if(!unBind){
                                bindDevices(deviceJustJoined);
                            }


                        } else {
                            configProgressDialog.dismiss();
                            Toast.makeText(DeviceAddActivity.this,"配置失败,请重试",Toast.LENGTH_SHORT).show();
                            System.err.println(getString(R.string.code_config_failture)
                                    + reason.getErrorId());
//                            showConfigFailedInfo(reason);

                        }

                    }
                });


//        SocketManager.sharedInstance().sendWiFiConfig(FileUtils.getDefaultUser().getToken(), ssid, password);
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;

        renderInterface();
    }

    private void renderInterface() {
        connectingLayout.setVisibility(View.GONE);
        wifiUploadLayout.setVisibility(View.VISIBLE);
//        if (currentStatus == Status.WIFIDETECT) {
//            wifiDetectLayout.setVisibility(View.VISIBLE);
//            wifiUploadLayout.setVisibility(View.GONE);
//            connectingLayout.setVisibility(View.GONE);
//        } else if (currentStatus == Status.WIFIUPLOAD) {
//            wifiDetectLayout.setVisibility(View.GONE);
//            wifiUploadLayout.setVisibility(View.VISIBLE);
//            connectingLayout.setVisibility(View.GONE);
//        } else {
//            wifiDetectLayout.setVisibility(View.GONE);
//            wifiUploadLayout.setVisibility(View.GONE);
//            connectingLayout.setVisibility(View.VISIBLE);
//        }
    }

    private void tryToConnectSocket() {
        SocketManager.sharedInstance().connect(Const.NetWork.SOCKET_ADDRESS, Const.NetWork.SOCKET_PORT);
    }

    private int tryCount = 0;
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if (tryCount > 10) {
                return;
            }

            tryCount++;

            tryToConnectSocket();
        }
    };

    Runnable countDownRunnable = new Runnable() {
        @Override
        public void run() {
            if (countDown < 0) {
                return;
            }

            setCountDown(--countDown);

            handler.postDelayed(this, 1000);
        }
    };

    public void setCountDown(int countDown) {
        this.countDown = countDown;

        countDownTextView.setText("请稍后, " + countDown + "s...");
        Log.d(TAG, "setCountDown: " + countDown);
        if (this.countDown <= 0) {
            handler.removeCallbacks(countDownRunnable);
            countDownTextView.setText("");

            finish();
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketManager.BroadcastAction.CONNECTED);
        intentFilter.addAction(SocketManager.BroadcastAction.DISCONNECTED);
        intentFilter.addAction(SocketManager.BroadcastAction.SEND_DATA);
        intentFilter.addAction(SocketManager.BroadcastAction.RECEIVED_DATA);
        intentFilter.addAction(SocketManager.BroadcastAction.TIMEOUT);
        LocalBroadcastManager.getInstance(Utils.getContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(Utils.getContext()).unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SocketManager.BroadcastAction.CONNECTED.equals(action)) { // 已连接
                Utils.showToast("设备已连接");
                setCurrentStatus(Status.WIFIUPLOAD);
            } else if (SocketManager.BroadcastAction.DISCONNECTED.equals(action )) { // 断开连接

            } else if (SocketManager.BroadcastAction.SEND_DATA.equals(action)) { // 发送信息

            } else if (SocketManager.BroadcastAction.RECEIVED_DATA.equals(action)) { // 接收到信息
                String receivedData = intent.getStringExtra("data");

                Log.d(TAG, "broadcastReceiver receivedData: " + receivedData);

                if (receivedData.toLowerCase().substring(0, 10).equals("bbbbbbbb01")) {
                    if (receivedData.toLowerCase().substring(receivedData.length()-4-4, receivedData.length()-4).equals("eeee")) {
                        Utils.showToast("设备已连接网络，稍后会显示在设备列表中");

                        ssidEditText.clearFocus();
                        passwordEditText.clearFocus();

                        setCurrentStatus(Status.CONNECTING);
                        handler.postDelayed(countDownRunnable, 1000);
                    } else {
                        Utils.showToast("WiFi配置信息上报失败");
                    }
                } else if (receivedData.toLowerCase().substring(0, 10).equals("bbbbbbbb00")) {
                    deviceId = receivedData.toLowerCase().substring(10, receivedData.length()-4);
                }
            } else if (SocketManager.BroadcastAction.TIMEOUT.equals(action)) { // 连接超时
                if (tryCount == 0) {
                    Utils.showToast("设备连接失败");
                }

                setCurrentStatus(Status.WIFIDETECT);

                handler.postDelayed(runnable, 10 * 1000); // 10秒
            }
        }

    };


    private void bindDevices(uSDKDevice device){
        DeviceDetailRoute.bindDevice(this, device, new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                configProgressDialog.dismiss();
                Toast.makeText(DeviceAddActivity.this,"配置成功",Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFail(String error) {
                configProgressDialog.dismiss();
                Toast.makeText(DeviceAddActivity.this,"配置失败,请重试",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
