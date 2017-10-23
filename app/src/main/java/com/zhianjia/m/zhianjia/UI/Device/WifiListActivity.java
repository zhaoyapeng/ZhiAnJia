package com.zhianjia.m.zhianjia.UI.Device;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.adapter.WifiListAdapter;
import com.zhianjia.m.zhianjia.components.data.Wifi;
import com.zhianjia.m.zhianjia.events.SelectWifiEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class WifiListActivity extends BaseAppCompatActivity {

    private static final String TAG = "WifiListActivity";

    private static final int REQ_GPS_PERMISSION = 1;

    private NavigationBar navigationBar;

    private List<ScanResult> mWifiList;
    private List<Wifi> wifis = new ArrayList<>();
    private ListView wifiListView;
    private WifiListAdapter wifiListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(WifiListActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WifiListActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_GPS_PERMISSION);
        } else {
            startWifiScan();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_wifi_list;
    }

    @Override
    protected void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        wifiListView = (ListView) findViewById(R.id.lv_wifilist);
    }

    @Override
    protected void initView() {
        navigationBar.setNavigationLoginStyle("请选择WiFi");

        wifiListAdapter = new WifiListAdapter(WifiListActivity.this, R.layout.wifi_list_item, wifis);
        wifiListView.setAdapter(wifiListAdapter);
    }

    @Override
    protected void setListener() {
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Wifi wifi = wifis.get(i);

                EventBus.getDefault().post(new SelectWifiEvent(wifi));

                finish();
            }
        });
    }

    private void startWifiScan() {
        WifiManager mWifiManager = (WifiManager)WifiListActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }

        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();

        wifis.clear();

        for (int i = 0; i < mWifiList.size(); i++) {
            ScanResult result = mWifiList.get(i);

            Wifi wifi = new Wifi();
            wifi.setSsid(result.SSID);
            wifi.setMac(result.BSSID);

            if (wifi.getSsid() == null || "".equals(wifi.getSsid())) {
                continue;
            }

            boolean hasContain = false;
            for (Wifi aWifi:wifis) {
                if (aWifi.getSsid().equals(wifi.getSsid())) {
                    hasContain = true;
                    break;
                }
            }

            if (hasContain) {
                continue;
            }

            wifis.add(wifi);
        }

        wifiListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_GPS_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限开启成功
                    startWifiScan();
                } else {
                    //权限开启失败 显示提示信息
                    showMissingPermissionDialog();
                }
            }
            break;
        }
    }

    /**
     * 显示提示信息
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转至当前应用的权限设置页面
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
