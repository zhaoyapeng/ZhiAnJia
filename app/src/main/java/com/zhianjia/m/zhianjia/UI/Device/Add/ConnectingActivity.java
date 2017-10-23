package com.zhianjia.m.zhianjia.UI.Device.Add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.components.network.SocketManager;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.Utils;

public class ConnectingActivity extends BaseAppCompatActivity {

    private NavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tryToConnectSocket();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_connecting;
    }

    @Override
    protected void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
    }

    @Override
    protected void initView() {
        navigationBar.setNavigationLoginStyle("添加设备");
    }

    @Override
    protected void setListener() {
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void tryToConnectSocket() {
        registerReceiver();
        SocketManager.sharedInstance().connect(Const.NetWork.SOCKET_ADDRESS, Const.NetWork.SOCKET_PORT);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketManager.BroadcastAction.CONNECTED);
        intentFilter.addAction(SocketManager.BroadcastAction.DISCONNECTED);
        LocalBroadcastManager.getInstance(Utils.getContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(Utils.getContext()).unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver();

            String action = intent.getAction();
            if (SocketManager.BroadcastAction.CONNECTED.equals(action)) { // 已连接
                Intent i = new Intent(Const.Actions.ACTION_ACTIVITY_WIFI_UPLOAD);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                ConnectingActivity.this.startActivity(i);
            } else if (SocketManager.BroadcastAction.DISCONNECTED.equals(action)) { // 断开连接
                Intent i = new Intent(Const.Actions.ACTION_ACTIVITY_WIFI_DETECT);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                ConnectingActivity.this.startActivity(i);
            }
        }

    };
}
