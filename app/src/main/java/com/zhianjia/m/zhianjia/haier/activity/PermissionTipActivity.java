package com.zhianjia.m.zhianjia.haier.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.haier.uhome.usdk.api.uSDKDeviceManager;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.haier.AppDemo;

/**
 *
 */
public class PermissionTipActivity extends Activity {

    private Button requestPermissionRefuseBT = null;
    private Button requestPermissionGoBT = null;
    private TextView permissionTipTV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_tip_layout);
        initUI();
        initEvent();
    }

    private void initUI() {
        requestPermissionGoBT = (Button)
                findViewById(R.id.permission_request_go_button);

        requestPermissionRefuseBT = (Button)
                findViewById(R.id.permission_request_refuse_button);

        permissionTipTV = (TextView)
                findViewById(R.id.permission_tip_textview);
    }

    private void initEvent() {
        requestPermissionGoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2PermissionConfigPage();

            }
        });

        requestPermissionRefuseBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    /**
     * 如果没有为应用设置权限
     * 调用App的方法让程序停止扫描
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void jump2PermissionConfigPage() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 100);

        /*
        AppDemo appDemo = (AppDemo) getApplication();
        requestPermissions(appDemo.getPermissionStringArray(), 100);
        */
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                return;

            } else {
                permissionTipTV.setText("App没有获得权限，定位功能授权后继续实时扫描未入网的智能设备！");

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppDemo appDemo = (AppDemo) getApplication();
        int result = checkPermission(appDemo.getPermissionStringArray()[0],
                android.os.Process.myPid(), android.os.Process.myUid());

        if(result == PackageManager.PERMISSION_GRANTED) {
            uSDKDeviceManager deviceManger = uSDKDeviceManager.getSingleInstance();
            appDemo.helpSmartDeviceAutoJoinWifi(deviceManger);
            Toast.makeText(this, "已为您开启扫描待入网的智能设备", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            permissionTipTV.setText(getString(R.string.tip_app_permission_grant_failed));

        }
    }
}
