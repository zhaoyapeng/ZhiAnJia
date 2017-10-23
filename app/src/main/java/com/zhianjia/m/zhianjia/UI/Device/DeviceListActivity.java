package com.zhianjia.m.zhianjia.UI.Device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.adapter.DeviceListAdapter;
import com.zhianjia.m.zhianjia.components.network.SocketManager;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.events.ChangeDeviceEvent;
import com.zhianjia.m.zhianjia.haier.constant.RequestConstant;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceItemModel;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceListModel;
import com.zhianjia.m.zhianjia.haier.net.route.DeviceDetailRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.util.HaiErConnectionUtil;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DeviceListActivity extends BaseAppCompatActivity {

    private static final String TAG = "DeviceListActivity";

    private static final int RENAME_REQ_CODE = 1000;

    private ListView deviceListView;
    private DeviceListAdapter deviceListAdapter;
    private List<ZDeviceItemModel> devices = new ArrayList<>();


    private HaiErConnectionUtil haiErConnectionUtil;
    private NavigationBar navigationBar;


    private int POST_DELAYED_TIME = 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        haiErConnectionUtil = new HaiErConnectionUtil(this);
        handler.postDelayed(loadListRunnable, POST_DELAYED_TIME);
        loadDeviceList();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadDeviceList();

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(loadListRunnable);
        deviceListAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_device_list;
    }

    @Override
    protected void getComponents() {
        deviceListView = (ListView) findViewById(R.id.lv_devicelist);
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
    }

    @Override
    protected void initView() {
        deviceListAdapter = new DeviceListAdapter(DeviceListActivity.this);
        deviceListView.setAdapter(deviceListAdapter);

        navigationBar.setNavigationLoginStyle("设备列表");
        navigationBar.showRightArea("添加设备");
        navigationBar.setRightAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_DEVICE_ADD);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                DeviceListActivity.this.startActivity(intent);
            }
        });
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void setListener() {

        deviceListAdapter.setCallBack(new DeviceListAdapter.CallBack() {
            @Override
            public void configureNetWor(ZDeviceItemModel model) {
                Intent intent = new Intent(DeviceListActivity.this, DeviceAddActivity.class);
                intent.putExtra(DeviceAddActivity.UNBIND, true);
                startActivity(intent);
            }

            @Override
            public void unBind(ZDeviceItemModel model) {
                showLoading("");
                DeviceDetailRoute.unBindDevice(DeviceListActivity.this, model.getDevice_id(), new NetWorkCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        LoginModel model = (LoginModel) data;
                        hideLoading();
                        if (RequestConstant.RETCODE.equals(model.getRetCode())) {
                            Utils.showToast("解绑成功");
                            updateToZajService();// 同步
                        } else {
                            Utils.showToast(model.getRetInfo());
                        }

                    }

                    @Override
                    public void onFail(String error) {
                        hideLoading();
                        Utils.showToast(error);
                    }
                });
            }

            @Override
            public void onClickItem(ZDeviceItemModel model) {
                Log.e("tag", "rootLayout3");
                EventBus.getDefault().post(new ChangeDeviceEvent(model));
                finish();
            }
        });


        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ZDeviceItemModel device = devices.get(i);
                if ("1".equals(device.getSelf_device())) {
                    ChangeDeviceEvent changeDeviceEvent = new ChangeDeviceEvent(device);
                    changeDeviceEvent.setPosition(i);
                    EventBus.getDefault().post(new ChangeDeviceEvent(device));
                    finish();
                } else {
                    startHaierApp();
                }
            }
        });

        deviceListAdapter.setRenameOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZDeviceItemModel device = (ZDeviceItemModel) view.getTag();
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_DEVICE_RENAME);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                intent.putExtra("deviceId", device.getDevice_id());
                intent.putExtra("deviceName", device.getDevice_name());
                DeviceListActivity.this.startActivityForResult(intent, RENAME_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RENAME_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                ZDeviceItemModel newDevice = (ZDeviceItemModel) data.getSerializableExtra(DeviceRenameActivity.P_RENAME_DEVICE);
                for (ZDeviceItemModel device : devices) {
                    if (device.getDevice_id().equals(newDevice.getDevice_id())) {
                        device.setDevice_name(newDevice.getDevice_name());
                        break;
                    }
                }

                deviceListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadDeviceList() {
        if (FileUtils.getDefaultUser() == null) {
            return;
        }
        getDeviceListData(true);
    }

    Handler handler = new Handler();
    Runnable loadListRunnable = new Runnable() {
        @Override
        public void run() {
            getDeviceListData(false);
            handler.postDelayed(this, POST_DELAYED_TIME); // 10秒
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

                        handler.postDelayed(loadListRunnable, POST_DELAYED_TIME);

                        unregisterReceiver();
                    }
                }
            }
        }

    };


    /**
     * 获取设备列表
     */
    private void getDeviceListData(boolean isShowLoading) {
        if (isShowLoading) {
            showLoading("");
        }
        DeviceDetailRoute.getBindDeviceList(this, new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                hideLoading();
                if (data instanceof ZDeviceListModel) {
                    ZDeviceListModel model = (ZDeviceListModel) data;
                    if (model.getCode() == 0) {
                        devices = model.getData();
                        deviceListAdapter.refresh(model.getData());
                    } else {
                        Utils.showToast(model.getMsg());
                    }
                } else {
                    Utils.showToast("获取设备列表失败");
                }

            }

            @Override
            public void onFail(String error) {
                hideLoading();
                Toast.makeText(DeviceListActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解除绑定之后 同步 设备列表到 智安家
     */
    private void updateToZajService() {
        DeviceDetailRoute.getBindDeviceList(this, new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                // 只对成功情况做处理
                if (data instanceof ZDeviceListModel) {
                    ZDeviceListModel model = (ZDeviceListModel) data;
                    if (model.getCode() == 0) {
                        devices = model.getData();
                        deviceListAdapter.refresh(model.getData());
                    }
                }

            }

            @Override
            public void onFail(String error) {
            }
        });
    }


    private void startHaierApp() {
        PackageManager packageManager = getPackageManager();
        if (checkPackInfo("com.haier.uhome.uplus")) {
            Intent intent = packageManager.getLaunchIntentForPackage("com.haier.uhome.uplus");
            startActivity(intent);
        } else {
            Toast.makeText(this, "没有安装海尔U+", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    private boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(loadListRunnable);
    }
}
