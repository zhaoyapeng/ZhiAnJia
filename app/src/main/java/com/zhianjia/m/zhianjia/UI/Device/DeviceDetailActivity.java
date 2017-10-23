package com.zhianjia.m.zhianjia.UI.Device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haier.uhome.account.api.uAccount;
import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.interfaces.IuSDKDeviceListener;
import com.haier.uhome.usdk.api.interfaces.IuSDKDeviceManagerListener;
import com.haier.uhome.usdk.api.uSDKCloudConnectionState;
import com.haier.uhome.usdk.api.uSDKDevice;
import com.haier.uhome.usdk.api.uSDKDeviceAlarm;
import com.haier.uhome.usdk.api.uSDKDeviceAttribute;
import com.haier.uhome.usdk.api.uSDKDeviceManager;
import com.haier.uhome.usdk.api.uSDKDeviceStatusConst;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import com.haier.uhome.usdk.api.uSDKLogLevelConst;
import com.haier.uhome.usdk.api.uSDKManager;
import com.umeng.analytics.MobclickAgent;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.widget.NatureHomeButton;
import com.zhianjia.m.zhianjia.ZhiAnJiaApplication;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.components.network.SocketManager;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.events.ChangeDeviceEvent;
import com.zhianjia.m.zhianjia.events.DeviceRenameEvent;
import com.zhianjia.m.zhianjia.haier.activity.PermissionTipActivity;
import com.zhianjia.m.zhianjia.haier.data.DeviceDetailsDataModel;
import com.zhianjia.m.zhianjia.haier.data.DeviceDetailsModel;
import com.zhianjia.m.zhianjia.haier.data.LoginDataModel;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceItemModel;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceListModel;
import com.zhianjia.m.zhianjia.haier.dialog.DeviceControlElecHeaterDialog;
import com.zhianjia.m.zhianjia.haier.dialog.DeviceControlUDevKitDialog;
import com.zhianjia.m.zhianjia.haier.net.route.DeviceDetailRoute;
import com.zhianjia.m.zhianjia.haier.net.route.UserRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.util.GetDataByIdUtil;
import com.zhianjia.m.zhianjia.haier.util.HaiErConnectionUtil;
import com.zhianjia.m.zhianjia.haier.util.Util;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceDetailActivity extends BaseAppCompatActivity {

    private static final String TAG = "DeviceDetailActivity";

    //    private static final int LIST_REQ_CODE = 1001;
    private static final int FLING_MIN_DISTANCE = 20;// 移动最小距离
    private static final int FLING_MIN_VELOCITY = 200;// 移动最大速度


    private RelativeLayout noDeviceLayout;
    private LinearLayout hasDeviceLayout;

    private NatureHomeButton pmHomeButton;
    private NatureHomeButton jiaquanButton;
    private NatureHomeButton wenduButton;
    private NatureHomeButton shiduButton;
    private NatureHomeButton wuyanButton;
    private NatureHomeButton tovButton;

//    private ImageView halfCircleImageView;

    private Button leftNavButton;
    private Button rightNavButton;

    private Animation rotateAnimation;

    private TextView valueTextView;
    private TextView unitTextView;
    private TextView descTextView;
    private TextView timeTextView;
    private TextView deviceNameTextView;
    private TextView noViewTipTextView;
    RelativeLayout mainLayout;
    private String currentNatureIdentifier = "pm";
    private ImageButton promptImg;

    /*
    *
    * 海尔相关
    * */

    private HaiErConnectionUtil haiErConnectionUtil;


    private UserRoute userRoute;


    // 当前展示 的设备model
    private ZDeviceItemModel selectDeviceModel;
    private int POST_DELAYED_TIME = 10 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        haiErConnectionUtil = new HaiErConnectionUtil(this);
        userRoute = new UserRoute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(loadListRunnable);

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deviceRenameEventHandler(DeviceRenameEvent event) {
//        selectuSDKDevice.setName(event.device.getName());
        if (event.device != null && selectDeviceModel != null) {
            if (!selectDeviceModel.getDevice_name().equals(event.device.getDevice_name())&&!TextUtils.isEmpty(event.device.getDevice_name())) {
                selectDeviceModel.setDevice_name(event.device.getDevice_name());
                deviceNameTextView.setText(event.device.getDevice_name() + " ▾");
            }
        }
//        if (allSightedDevices != null && allSightedDevices.size() > 0) {
//            for (int i = 0; i < allSightedDevices.size(); i++) {
//                if (allSightedDevices.get(i).getDeviceId().equals(selectDeviceId)) {
//                    selectuSDKDevice = allSightedDevices.get(i);
//                    SharedPreferencesDevice.saveDevice(DeviceDetailActivity.this, selectDeviceId);
//                    connectDevices(allSightedDevices.get(i));
//                    hideNoDeviceView();
//                }
//            }
//        }
//        renderInterface();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeDeviceEventHandler(ChangeDeviceEvent event) {
        Log.d(TAG, "deviceRenameEventHandler:");
        selectDeviceModel = event.device;
        updateDeviceInfo();
    }

    private static boolean mBackKeyPressed = false; // 记录是否有首次按键

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() { // 延时两秒，如果超出则擦除第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
            this.finish();
            MobclickAgent.onKillProcess(this);
            System.exit(0);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected void getComponents() {
        pmHomeButton = (NatureHomeButton) findViewById(R.id.home_pm_button);
        jiaquanButton = (NatureHomeButton) findViewById(R.id.home_jiaquan_button);
        wenduButton = (NatureHomeButton) findViewById(R.id.home_wendu_button);
        shiduButton = (NatureHomeButton) findViewById(R.id.home_shidu_button);
        wuyanButton = (NatureHomeButton) findViewById(R.id.home_wuyan_button);
        tovButton = (NatureHomeButton) findViewById(R.id.home_tov_button);
//        halfCircleImageView = (ImageView) findViewById(R.id.home_circle_half);
        leftNavButton = (Button) findViewById(R.id.home_nav_left_button);
        rightNavButton = (Button) findViewById(R.id.home_nav_right_button);
        valueTextView = (TextView) findViewById(R.id.value_textview);
        unitTextView = (TextView) findViewById(R.id.unit_textview);
        descTextView = (TextView) findViewById(R.id.desc_textview);
        timeTextView = (TextView) findViewById(R.id.time_textview);
        deviceNameTextView = (TextView) findViewById(R.id.devicename_textview);
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        noDeviceLayout = (RelativeLayout) findViewById(R.id.rl_noview);
        hasDeviceLayout = (LinearLayout) findViewById(R.id.ll_hasdevice);
        noViewTipTextView = (TextView) findViewById(R.id.tv_noviewtip);
        promptImg=  (ImageButton)findViewById(R.id.img_prompt);
    }

    @Override
    protected void initView() {
        pmHomeButton.makeStyle(true, R.drawable.pm_icon_select, R.drawable.om_icon_no, "PM2.5", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNatureIdentifier = "pm";
                renderInterface();
                selectNatureHomeButton(pmHomeButton);
            }
        });
        jiaquanButton.makeStyle(false, R.drawable.jiaquan_icon_selelct, R.drawable.jiaquan_icon_no, "甲醛", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNatureIdentifier = "jq";
                renderInterface();
                selectNatureHomeButton(jiaquanButton);
            }
        });
        wenduButton.makeStyle(false, R.drawable.icon_wendu_select, R.drawable.icon_wendu_no, "温度", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNatureIdentifier = "wd";
                renderInterface();
                selectNatureHomeButton(wenduButton);
            }
        });
        shiduButton.makeStyle(false, R.drawable.shidu_icon_select, R.drawable.shidu_icon_no, "湿度", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNatureIdentifier = "sd";
                renderInterface();
                selectNatureHomeButton(shiduButton);
            }
        });
        wuyanButton.makeStyle(false, R.drawable.yanwu_icon_select, R.drawable.yanwu_icon_no, "湿度", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNatureIdentifier = "wy";
                renderInterface();
                selectNatureHomeButton(wuyanButton);
            }
        });

        tovButton.makeStyle(false, R.drawable.tov_icon_select, R.drawable.tov_icon_no, "湿度", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNatureIdentifier = "tov";
                renderInterface();
                selectNatureHomeButton(tovButton);
            }
        });

        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.home_rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);

//        if (rotateAnimation != null) {
//            halfCircleImageView.startAnimation(rotateAnimation);
//        }

        initHaier();
    }

    private void selectNatureHomeButton(NatureHomeButton homeButton) {
        pmHomeButton.setSelected(homeButton == pmHomeButton);
        jiaquanButton.setSelected(homeButton == jiaquanButton);
        wenduButton.setSelected(homeButton == wenduButton);
        shiduButton.setSelected(homeButton == shiduButton);
        wuyanButton.setSelected(homeButton == wuyanButton);
        tovButton.setSelected(homeButton == tovButton);
    }

    private void renderInterface() {
//        NatureInfo natureInfo;
        String unit = "";
        String valueString = "";
        String states = "";
        if (currentNatureIdentifier.equals("jq")) {
//            natureInfo = device.getJqInfo();
            unit = "mg/m³";
            valueString = jiaquanButton.getValue();
            if (jiaquanButton.getModel() != null)
                states = jiaquanButton.getModel().getDesc();
            if (jiaquanButton.getIntValue() > 0.12) {
                mainLayout.setBackgroundResource(R.drawable.home_bg_red);
            } else { // 低
                mainLayout.setBackgroundResource(R.drawable.home_bg_green);
            }
        } else if (currentNatureIdentifier.equals("wd")) {
            unit = "℃";
            valueString = wenduButton.getValue();
            if (wenduButton.getModel() != null)
                states = wenduButton.getModel().getDesc();
            if (wenduButton.getIntValue() > 24) { // 高
                mainLayout.setBackgroundResource(R.drawable.home_bg_red);
            } else {
                mainLayout.setBackgroundResource(R.drawable.home_bg_green);
            }

        } else if (currentNatureIdentifier.equals("sd")) {
//            natureInfo = device.getSdInfo();
            unit = "RH";
            valueString = shiduButton.getValue();
            if (shiduButton.getModel() != null)
                states = shiduButton.getModel().getDesc();

            if (shiduButton.getIntValue() > 60) { // 中
                mainLayout.setBackgroundResource(R.drawable.home_bg_red);
            } else { // 低
                mainLayout.setBackgroundResource(R.drawable.home_bg_green);
            }
        } else if (currentNatureIdentifier.equals("pm")) {
//            natureInfo = device.getPmInfo();
            unit = "mg/m³";
            valueString = pmHomeButton.getValue() + "";
            if (pmHomeButton.getModel() != null)
                states = pmHomeButton.getModel().getDesc();

            if (pmHomeButton.getIntValue() > 150) {
                mainLayout.setBackgroundResource(R.drawable.home_bg_red);
            } else { // 低
                mainLayout.setBackgroundResource(R.drawable.home_bg_green);
            }
        } else if (currentNatureIdentifier.equals("wy")) {
            unit = "-";
            valueString = wuyanButton.getValue() + "";
            if (wuyanButton.getModel() != null)
                states = wuyanButton.getModel().getDesc();

            if (wuyanButton.getIntValue() > 1) {
                mainLayout.setBackgroundResource(R.drawable.home_bg_red);
            } else { // 低
                mainLayout.setBackgroundResource(R.drawable.home_bg_green);
            }
        } else if (currentNatureIdentifier.equals("tov")) {
            unit = "mg/m³";
            valueString = tovButton.getValue() + "";
            if (tovButton.getModel() != null)
                states = tovButton.getModel().getDesc();
            if (tovButton.getIntValue() > 0.5) {
                mainLayout.setBackgroundResource(R.drawable.home_bg_red);
            } else { // 低
                mainLayout.setBackgroundResource(R.drawable.home_bg_green);
            }
        }

        valueTextView.setText(valueString);
        unitTextView.setText("单位：" + unit);
        descTextView.setText(states);

//        timeTextView.setText("最近更新：" + device.getUpdateTime());
//        if (selectuSDKDevice != null) {
//            deviceNameTextView.setText(selectuSDKDevice.getDeviceId() + " ▾");
//        }
        deviceNameTextView.setVisibility(View.VISIBLE);

//        if (natureInfo.getLevel() == 3) { // 高
//            mainLayout.setBackgroundResource(R.drawable.home_bg_red);
//        } else if (natureInfo.getLevel() == 2) { // 中
//            mainLayout.setBackgroundResource(R.drawable.home_bg_yellow);
//        } else { // 低
//            mainLayout.setBackgroundResource(R.drawable.home_bg_green);
//        }

    }

    @Override
    protected void setListener() {
        rightNavButton.setTag("ADD");

        leftNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_OPTION);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                DeviceDetailActivity.this.startActivity(intent);
            }
        });

        rightNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() == "ADD") {
                    registerReceiver();
                    Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_DEVICE_ADD);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                    DeviceDetailActivity.this.startActivity(intent);
                } else if (view.getTag() == "CHANGE") {
                    Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_DEVICE_LIST);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                    DeviceDetailActivity.this.startActivity(intent);
                } else if (view.getTag() == "DETAIL") {
                    Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_DEVICE_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
//                    intent.putExtra("name", intent.putExtra("selectDeviceModel",selectDeviceModel););
                    Bundle bundle = new Bundle();
                    Log.e("tag", "selectDeviceModel1=" + selectDeviceModel);
                    bundle.putSerializable("selectDeviceModel", selectDeviceModel);
                    intent.putExtras(bundle);
                    DeviceDetailActivity.this.startActivity(intent);
                }
            }
        });

        deviceNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_DEVICE_LIST);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                intent.putExtra("name", GetDataByIdUtil.getUacDeviceById(DeviceDetailActivity.this, selectDeviceModel.getDevice_id()).getName());
                ;
                DeviceDetailActivity.this.startActivity(intent);
            }
        });
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                showLoading("");
                updateDeviceInfo();
            }
        });

        hasDeviceLayout.setLongClickable(true);
        showLoading("");
        loadDeviceList();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == LIST_REQ_CODE) {
//            if (resultCode == RESULT_OK) {
//                device = (Device) data.getSerializableExtra("P_DEVICE");
//                deviceNameTextView.setText(device.getName() + " >");
//
//                FileUtils.saveObject(FileUtils.DEFAULT_DEVICE, device);
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkLoginState()) {
            loadUserInfo();
//            selectuSDKDevice = getDeviceById(SharedPreferencesDevice.getSaveDeviceId(this));
//            if (selectuSDKDevice == null) { // 没有DefaultDevice
//                loadDeviceList();
//            } else {
//                loadDeviceDetail();
//                hideNoDeviceView();
//                handler.postDelayed(runnable, 10 * 1000); // 10秒
//            }
            handler.postDelayed(runnable, POST_DELAYED_TIME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(loadListRunnable);
    }

    private void showNoDeviceView() {
        promptImg.setVisibility(View.VISIBLE);
        noViewTipTextView.setText("目前没有设备\n点击右上添加");
        rightNavButton.setText("添加设备");
        rightNavButton.setTag("ADD");
        if (noDeviceLayout.getVisibility() == View.VISIBLE) {
            return;
        }

        noDeviceLayout.setVisibility(View.VISIBLE);
        hasDeviceLayout.setVisibility(View.GONE);
    }

    private void hideNoDeviceView() {
        if (noDeviceLayout.getVisibility() != View.VISIBLE) {
            return;
        }

        noDeviceLayout.setVisibility(View.GONE);
        hasDeviceLayout.setVisibility(View.VISIBLE);
    }

    private void showNoDataView() {
        promptImg.setVisibility(View.VISIBLE);
        noViewTipTextView.setText("该设备暂无信息\n点击右上切换");
        rightNavButton.setText("切换设备");
        rightNavButton.setTag("CHANGE");
        if (noDeviceLayout.getVisibility() == View.VISIBLE) {
            return;
        }

        noDeviceLayout.setVisibility(View.VISIBLE);
        hasDeviceLayout.setVisibility(View.GONE);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateDeviceInfo();
        }
    };

    Runnable loadListRunnable = new Runnable() {
        @Override
        public void run() {
            loadDeviceList();
        }
    };

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketManager.BroadcastAction.RECEIVED_DATA);
        LocalBroadcastManager.getInstance(Utils.getContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(Utils.getContext()).unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SocketManager.BroadcastAction.RECEIVED_DATA.equals(action)) { // 接收到信息
                String receivedData = intent.getStringExtra("data");

                if (receivedData.toLowerCase().substring(0, 10).equals("bbbbbbbb01")) {
                    if (receivedData.toLowerCase().substring(receivedData.length() - 4 - 4, receivedData.length() - 4).equals("eeee")) {
                        loadDeviceList();

                        handler.postDelayed(loadListRunnable, 5 * 1000);

                        unregisterReceiver();
                    }
                }
            }
        }

    };

    /**
     * 检查登录状态
     *
     * @return 返回是否登录
     */
    private boolean checkLoginState() {
        if (FileUtils.getDefaultUser() == null) {
            Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_LOGIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
            DeviceDetailActivity.this.startActivity(intent);
            return false;
        }

        return true;
    }

    private void loadUserInfo() {
        final User localUser = FileUtils.getDefaultUser();
        userRoute.getUserInfo(this, new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                LoginDataModel model = (LoginDataModel) data;
                User user = model.getData();
                if (model.getCode() == 0) {
                    localUser.setAddress(user.getAddress());
                    localUser.setArea(user.getArea());
                    localUser.setIconUrl(user.getIconUrl());
                    localUser.setPinyin(user.getPinyin());
                    localUser.setCity(user.getCity());
                    localUser.setMobile(user.getMobile());
                    localUser.setName(user.getName());
                    localUser.setUid(user.getUid());


                    FileUtils.saveObject(FileUtils.DEFAULT_USER, localUser);
                    Log.e("tag", "userAccount=" + new Gson().toJson(localUser));
                }
            }

            @Override
            public void onFail(String error) {

            }
        });

    }

    /**
     * 获取设备列表
     */
    private void loadDeviceList() {
        DeviceDetailRoute.getBindDeviceList(this, new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof ZDeviceListModel) {
                    ZDeviceListModel model = (ZDeviceListModel) data;
                    if (model.getCode() == 0) {
                        if (model.getData() != null && model.getData().size() > 0) {
                            selectDeviceModel = filterDeivce(model.getData());
                            if (selectDeviceModel != null) {
                                updateDeviceInfo();
                            } else {
                                hideLoading();
                                showNoDeviceView();
                                handler.postDelayed(loadListRunnable, POST_DELAYED_TIME);
                            }
                        } else {
                            handler.postDelayed(loadListRunnable, POST_DELAYED_TIME);
                            hideLoading();
                            showNoDeviceView();
                        }
                    } else {
                        handler.postDelayed(loadListRunnable, POST_DELAYED_TIME);
                        hideLoading();
                        showNoDeviceView();
                    }
                } else {
                    handler.postDelayed(loadListRunnable, POST_DELAYED_TIME);
                    hideLoading();
                    showNoDeviceView();
                }

            }

            @Override
            public void onFail(String error) {
                handler.postDelayed(loadListRunnable, POST_DELAYED_TIME);
                hideLoading();
                showNoDeviceView();
                Toast.makeText(DeviceDetailActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 海尔相关 start
     */
    private uSDKManager uSDKMgr = uSDKManager.getSingleInstance();
    private uSDKDeviceManager uSDKDeviceMgr = uSDKDeviceManager.getSingleInstance();
    private ZhiAnJiaApplication appDemo;
    private uAccount accountHelper = uAccount.getSingleInstance();
    private List<uSDKDevice> allSightedDevices = null;

    private DeviceControlUDevKitDialog deviceControlUDevKitDialog;
    private DeviceControlElecHeaterDialog deviceControlElecHeaterDialog;
    private uSDKDevice selectuSDKDevice;

    private void initHaier() {

        appDemo = (ZhiAnJiaApplication) this.getApplicationContext();
        initHomeButton();

        deviceControlUDevKitDialog =
                new DeviceControlUDevKitDialog(DeviceDetailActivity.this);
        deviceControlElecHeaterDialog =
                new DeviceControlElecHeaterDialog(DeviceDetailActivity.this);

        setHandler2ApplicationForPermissionDenied();
//        initUIEvent();
        initDeviceInteractive();

    }

    private void setHandler2ApplicationForPermissionDenied() {
        appDemo.setMainActivityMsgHandler(this.permissionLackHandler);
    }

    private Handler permissionLackHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(
                    DeviceDetailActivity.this, PermissionTipActivity.class);
            startActivity(intent);
        }

    };

    /**
     * 注册设备 启动uSDK
     */
    private void initDeviceInteractive() {
        registerDevicesManageLogic();
        this.startuSDK();
        refreshCloudIP();
    }


    /**
     * 启动uSDK
     */
    private void startuSDK() {
        uSDKMgr.initLog(uSDKLogLevelConst.USDK_LOG_DEBUG, true, new IuSDKCallback() {
            @Override
            public void onCallback(uSDKErrorConst uSDKErrorConst) {

            }
        });

        uSDKMgr.startSDK(new IuSDKCallback() {

            @Override
            public void onCallback(uSDKErrorConst uSDKStartResult) {
                //使用此API查询uSDK启动状态
                //Is uSDK started?
                //boolean appQueryuSDKisStatus = uSDKMgr.isSDKStart();
                if (uSDKStartResult == uSDKErrorConst.RET_USDK_OK) {
                    initAutoFoundUnjoinedSmartDevice();
                    refreshSightedDeviceList(null);

                    String usdkver = uSDKMgr.getuSDKVersion();

//                    uSDKRunStatusTextView.setText(R.string.tip_uSDK_status_start);

                } else {
//                    uSDKRunStatusTextView.setText(R.string.tip_uSDK_status_notstarted);

                }

            }

        });

    }

    private void refreshSightedDeviceList(List<uSDKDevice> devicesChanged) {
        //直接刷新uSDK可发现的所有设备
        if (devicesChanged != null)
            allSightedDevices = uSDKDeviceMgr.getDeviceList();

//        appManagedDevicesListViewAdapter.setDataSource(allSightedDevices);
//        appManagedDevicesListViewAdapter.notifyDataSetChanged();

//        updateDevice();

    }


    /**
     * 发现 设备 并使之入网
     * begin to search unjoined smart device
     */
    private void initAutoFoundUnjoinedSmartDevice() {
        appDemo.helpSmartDeviceAutoJoinWifi(uSDKDeviceMgr);
    }


    private void refreshCloudIP() {
        CloudEnvironmentIPTask cloudEnvironmentIPTask = new CloudEnvironmentIPTask();
        cloudEnvironmentIPTask.execute();
    }

    private class CloudEnvironmentIPTask extends AsyncTask<String, Void, String> {
        /**
         * The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute()
         */
        protected String doInBackground(String... urls) {
            String environmentDesc = null;
            try {
                InetAddress environmentInetAddress =
                        InetAddress.getByName("uhome.haier.net");
                environmentDesc = environmentInetAddress.getHostAddress();
                System.out.println("Current Cloud Environment:" + environmentDesc);

            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "Cloud IP Get Failed!";

            }

            return environmentDesc;
        }

        /**
         * The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground()
         */
        protected void onPostExecute(String result) {
//            cloudIPTextView.setText("uhome.haier.net:" + result);
        }
    }


    private void registerDevicesManageLogic() {

        uSDKDeviceMgr.setDeviceManagerListener(new IuSDKDeviceManagerListener() {

            /**
             * 設備管理業務-uSDK剔除无法交互的设备
             * smart devices become unsighted
             */
            @Override
            public void onDevicesRemove(List<uSDKDevice> devicesChanged) {
                refreshSightedDeviceList(devicesChanged);
                System.out.println("smart device has been invalid....");


            }

            /**
             * 設備管理業務-uSDK發現一臺可用設備
             * Device Management-uSDK device report event:
             */
            @Override
            public void onDevicesAdd(List<uSDKDevice> devicesChanged) {
                refreshSightedDeviceList(devicesChanged);
                System.out.println("smart device added....");

            }

            /**
             * <pre>
             * 遠程服務器推送設備解綁定消息
             * remote server push a msg when register a smart device on user's account
             * app need to connect remote server first.
             * </pre>
             */
            @Override
            public void onDeviceUnBind(String devicesChanged) {
                System.out.println("Remote Server push msg:"
                        + devicesChanged + " has been removed from accout");

            }

            /**
             * <pre>
             * 遠程服務器推送設備綁定消息
             * remote server push a msg when unregister a smart device from user's account
             * app need to connect remote server first.
             * </pre>
             */
            @Override
            public void onDeviceBind(String devicesChanged) {
                System.out.println("Remote Server push msg:"
                        + devicesChanged + " has been added to accout");

            }

            /**
             * <pre>
             * App在此可以得到遠程服務器連接狀態
             * App get connection status of remote server here
             * </pre>
             */
            @Override
            public void onCloudConnectionStateChange(uSDKCloudConnectionState status) {
                //遠程服務器已連接
                //remote server connected
                if (status == uSDKCloudConnectionState.CLOUD_CONNECTION_STATE_CONNECTED) {
                    System.out.println("uSDK3XDemo connect to Remote Server");
                    DeviceDetailActivity.this.setTitle("接入网关已连接");

                }

            }
        });

    }


    /**
     * 設置接口獲得接收智能設備的屬性上報、報警等
     * 執行連接智能設備
     * <p>
     * register, wait for getting property, alarm from smart device
     * exec connect to smart device
     */
    private void connectDevices(uSDKDevice selecteduSDKDevice) {
        receiveSmartDevciesProperties(selecteduSDKDevice);
        selecteduSDKDevice.connect(new IuSDKCallback() {

            @Override
            public void onCallback(uSDKErrorConst result) {
                if (result != uSDKErrorConst.RET_USDK_OK) {
                    System.out.println("this method exec failed!");
                }
            }

        });

    }


    /**
     * <pre>
     * 實現、設置listener
     * 準備接收智能設備屬性、報警、連接狀態變化等
     *
     * Impl,set listener
     * be ready to receive smart device property, alaram, connect status
     * </pre>
     */
    private void receiveSmartDevciesProperties(final uSDKDevice selecteduSDKDevice) {
        selecteduSDKDevice.setDeviceListener(new IuSDKDeviceListener() {

            /**
             * callback when connect status change
             * 智能設備的連接狀態發生改變時
             */
            @Override
            public void onDeviceOnlineStatusChange(uSDKDevice device,
                                                   uSDKDeviceStatusConst status, int statusCode) {
                String deviceStatus = Util.getuSDKDeviceStatus(
                        DeviceDetailActivity.this, selecteduSDKDevice);
//                deviceStatusTextView.setText(deviceStatus + "-" + statusCode);

                /**
                 * uSDKDeviceStatusConst.STATUS_CONNECTED時我們成功連接智能設備
                 * ，然后發送一個查詢指令
                 *
                 * when uSDKDeviceStatusConst.STATUS_CONNECTED
                 * we send a query command.
                 */
                if (uSDKDeviceStatusConst.STATUS_CONNECTED == status) {
                    Util.querySmartDeviceProperties(device);
                }

            }

            /**
             * callback when property change
             * 智能設備的屬性狀態發生改變時
             */
            @Override
            public void onDeviceAttributeChange(uSDKDevice device,
                                                List<uSDKDeviceAttribute> propertiesChanged) {
                List<String> deviceProperties = getSmartDevicePropertiesValues(device);


                HashMap<String, uSDKDeviceAttribute> propertiesMap =
                        (HashMap<String, uSDKDeviceAttribute>) device.getAttributeMap();

                Log.e("tag", "deviceProperties=" + propertiesMap);

                String typeId = device.getUplusId();


                boolean isUDevKitStatusDialogShow = deviceControlUDevKitDialog.isShowing();
                if (isUDevKitStatusDialogShow) {
                    deviceControlUDevKitDialog.updateSmartDeviceStatus(propertiesChanged, device);
                }

                boolean isElecHeaterStatusDialogShow = deviceControlElecHeaterDialog.isShowing();
                if (isElecHeaterStatusDialogShow) {
                    deviceControlElecHeaterDialog.updateSmartDeviceStatus(propertiesChanged, device);
                }


//                UacDevice[] uacDevices = appDemo.getUserAccount().getDevicesOfAccount();
//
//                if (postionTest % 2 == 0) {
//                    pmHomeButton.setData("78");
//                    jiaquanButton.setData("0.1");
//                    wenduButton.setData(propertiesMap.get("currentTemperature").getAttrValue());
//                    shiduButton.setData(propertiesMap.get("currentHumidity").getAttrValue());
//                    wuyanButton.setData("无烟");
//                    tovButton.setData("0.16");
//                    renderInterface();
//                } else {
//                    pmHomeButton.setData("75");
//                    jiaquanButton.setData("0.11");
//                    wenduButton.setData(propertiesMap.get("currentTemperature").getAttrValue());
//                    shiduButton.setData(propertiesMap.get("currentHumidity").getAttrValue());
//                    wuyanButton.setData("无烟");
//                    tovButton.setData("0.2");
//                    renderInterface();
//                }


            }

            /**
             * callback when alarm
             * 智能設備發生報警時
             */
            @Override
            public void onDeviceAlarm(uSDKDevice device, List<uSDKDeviceAlarm> alarms) {
                for (uSDKDeviceAlarm alarmItem : alarms) {
//                    selecteduSDKDeviceAlarmTextView.setText(alarmItem.getAlarmValue());

                }
            }

            /**
             * callback when ip or nettype change
             * 智能設備IP及網絡類型發生改變時
             */
            @Override
            public void onDeviceBaseInfoChange(uSDKDevice uSDKDevice) {
            }

            @Override
            public void onSubDeviceListChange(
                    uSDKDevice uSDKDevice, ArrayList<uSDKDevice> arrayList) {

            }
        });

    }

    //获得当前设备的属性值集合
    private ArrayList<String> getSmartDevicePropertiesValues(uSDKDevice device) {
        ArrayList<String> list = new ArrayList<String>();

        HashMap<String, uSDKDeviceAttribute> propertiesMap =
                (HashMap<String, uSDKDeviceAttribute>) device.getAttributeMap();
        Set<Map.Entry<String, uSDKDeviceAttribute>> set = propertiesMap.entrySet();
        Iterator<Map.Entry<String, uSDKDeviceAttribute>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, uSDKDeviceAttribute> entry = it.next();
            uSDKDeviceAttribute attr = entry.getValue();
            list.add(attr.getAttrName() + " : " + attr.getAttrValue());
        }

        return list;
    }


//    /**
//     * 开始接入网关
//     * <p>
//     * 获取到 设备列表之后 接入网关 一次
//     * 绑定设备成功之后 接入网关一次
//     */
//    private void startGetWay() {
//        uSDKCloudConnectionState cloudConnectionState =
//                uSDKDeviceMgr.getCloudConnectionState();
//        Log.e("tag", "connect2RemoteServerWhenLogin1");
//        if (cloudConnectionState !=
//                uSDKCloudConnectionState.CLOUD_CONNECTION_STATE_CONNECTED) {
//
//            Log.e("tag", "connect2RemoteServerWhenLogin2");
//            connect2RemoteServerWhenLogin();
//
//        }
//    }
//
//
//    /**
//     * 接入网关
//     */
//    private void connect2RemoteServerWhenLogin() {
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
//        deviceManager.connectToGateway(token, "usermg.uopendev.haier.net", 56821,
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
//
//    }


    /**
     * 海尔相关 end
     */


    private void initHomeButton() {
        pmHomeButton.setTitle("mg/m³", "pm2.5值");
        jiaquanButton.setTitle("mg/m³", "甲醛含量");
        wenduButton.setTitle("℃", "室内温度");
        shiduButton.setTitle("RH", "室内湿度");
        wuyanButton.setTitle("-", "烟雾报警");
        tovButton.setTitle("mg/m³", "TVOC含量");
    }


    private uSDKDevice getDeviceById(String selectDeviceId) {
        if (allSightedDevices != null && allSightedDevices.size() > 0) {
            for (int i = 0; i < allSightedDevices.size(); i++) {
                if (allSightedDevices.get(i).getDeviceId().equals(selectDeviceId)) {
                    return allSightedDevices.get(i);
                }
            }
        }

        return null;
    }


    private void updateDeviceInfo() {
        if (selectDeviceModel == null) {
            handler.postDelayed(loadListRunnable, POST_DELAYED_TIME); // 10秒
            hideLoading();
            return;
        }
        if (!TextUtils.isEmpty(selectDeviceModel.getDevice_name())) {
            Log.e("tag", "selectDeviceModel.getDevice_name()=" + selectDeviceModel.getDevice_name());
            deviceNameTextView.setVisibility(View.VISIBLE);
//            deviceNameTextView.setText(selectDeviceModel.getDevice_name());
            deviceNameTextView.setText(selectDeviceModel.getDevice_name() + " ▾");
        }

        DeviceDetailRoute.getDeviceInfo(DeviceDetailActivity.this, selectDeviceModel.getDevice_id(), new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                handler.postDelayed(runnable, POST_DELAYED_TIME); // 10秒
                DeviceDetailsDataModel model = (DeviceDetailsDataModel) data;
                if (model.getCode() == 0) {
                    hideLoading();
                    hideNoDeviceView();
                    updateNetView(model.getData());
                } else {
                    hideLoading();
                    showNoDataView();
                }
            }
            @Override
            public void onFail(String error) {
                handler.postDelayed(runnable, POST_DELAYED_TIME); // 10秒
                hideLoading();
                Utils.showToast(error);
            }
        });
    }


    /**
     * 更新设备信息
     */
    private void updateNetView(DeviceDetailsModel model) {
        if(model==null){
            return;
        }
        if(!TextUtils.isEmpty(model.getIs_low_voltage())&&"1".equals(model.getIs_low_voltage())){
            Toast.makeText(this,"电池电量低",Toast.LENGTH_SHORT).show();
        }
        deviceNameTextView.setText(selectDeviceModel.getDevice_name() + " ▾");
        rightNavButton.setText("详细数据");
        rightNavButton.setTag("DETAIL");

        if (model.getPm_value() != null) {
            pmHomeButton.setData(model.getPm_value());
        }

        if (model.getFd_value() != null) {
            jiaquanButton.setData(model.getFd_value());
        }

        if (model.getTemp_value() != null) {
            wenduButton.setData(model.getTemp_value());
        }

        if (model.getHumidity_value() != null) {
            shiduButton.setData(model.getHumidity_value());
        }


        if (model.getSmoke_value() != null) {
            wuyanButton.setData(model.getSmoke_value());
        }

        if (model.getTvoc_value() != null) {
            tovButton.setData(model.getTvoc_value());
        }

        SimpleDateFormat sdfTwo = new SimpleDateFormat("MM.dd HH:mm");
        timeTextView.setText("最近更新：" + sdfTwo.format(System.currentTimeMillis())+" 点击刷新");
        renderInterface();
    }

    /**
     * 筛选出第一个 智安家的设备
     */
    private ZDeviceItemModel filterDeivce(List<ZDeviceItemModel> list) {
        for (int i = 0; i < list.size(); i++) {
            ZDeviceItemModel model = list.get(i);
            if (model != null && "1".equals(model.getSelf_device())) {
                return model;
            }
        }
        return null;

    }
}
