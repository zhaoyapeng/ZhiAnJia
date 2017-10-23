package com.zhianjia.m.zhianjia.components.network;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by 7 on 2017/1/13.
 */

public class SocketManager implements SocketLink.SocketActionListener {

    public static class BroadcastAction {
        public static String CONNECTED = "COM.ZHIANJIA.M.ACTION.CONNECTED";
        public static String DISCONNECTED = "COM.ZHIANJIA.M.ACTION.DISCONNECTED";
        public static String SEND_DATA = "COM.ZHIANJIA.M.ACTION.SEND_DATA";
        public static String RECEIVED_DATA = "COM.ZHIANJIA.M.ACTION.RECEIVED_DATA";
        public static String TIMEOUT = "COM.ZHIANJIA.M.ACTION.TIMEOUT";
    }

    private static final String TAG = "SocketManager";

    private static SocketManager instance;
    private SocketLink socket;

    public static SocketManager sharedInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public void connect(String address, int port) {
        socket = new SocketLink(Utils.getContext(), this);

        socket.link(address, port);

        Log.d(TAG, "Connecting TO " + address + ":" + port + " ...");
    }

    public void sendWiFiConfig(String token, String ssid, String password) {
        if (socket == null) {
            Utils.showToast("设备未连接");
            return;
        }

        if (token == null || "".equals(token)) {
            Utils.showToast("Token为空，请退出登录后重试");
            return;
        }

        if (ssid == null || "".equals(ssid)) {
            Utils.showToast("网络名称不能为空");
            return;
        }

        if (password == null || "".equals(password)) {
            Utils.showToast("密码不能为空");
            return;
        }

        token = formatMessage(transAscStr2HexStr(token));
        String ssidLength = formatMessage(Integer.toHexString(ssid.length()));
        String passwordLength = formatMessage(Integer.toHexString(password.length()));
        ssid = formatMessage(transAscStr2HexStr(ssid));
        password = formatMessage(transAscStr2HexStr(password));

        String message = "AAAAAAAA01" + token + ssidLength + passwordLength + ssid + password;

        Log.d(TAG, "\n   TOKEN: " + token + "\n    SSID: " + ssid + "\nPASSWORD: " + password + "\n MESSAGE: " + message);

        int len = message.length();
        if ((len & 1) == 1) {
            len++;
            message = "0" + message;
        }
        int s_len = len >> 1, index = 0;
        byte[] send_b = new byte[s_len];
        char[] chr = message.toCharArray();
        if (s_len == 0) {
            return;
        }
        send_b[0] = 0;
        for (int i = 0; i < len; i++) {
            send_b[index] = (byte) ((send_b[index] << 4) + hex2byte(chr[i]));
            if ((i & 1) == 1) {
                if (index < (s_len - 1)) {
                    send_b[++index] = 0;
                }

            }
        }

        socket.sendMsg(send_b);
    }

    @Override
    public void onDataReceived(byte[] data) {
        String rec = "";
        for (byte i : data) {
            String s = Integer.toHexString(i & 0xFF);
            if ((s.length()&1) == 1) {
                s = "0" + s;
            }
            rec += s;
        }

        Log.d(TAG, "Socket Received: " + rec);

        Intent intent = new Intent(BroadcastAction.RECEIVED_DATA);
        intent.putExtra("data", rec);
        LocalBroadcastManager.getInstance(Utils.getContext()).sendBroadcast(intent);
    }

    @Override
    public void onDataSend(byte[] data) {
        String send = "";
        for (byte i : data) {
            String s = Integer.toHexString(i & 0xFF);
            if ((s.length()&1) == 1) {
                s = "0" + s;
            }
            send += s;
        }

        Log.d(TAG, "Socket Send: " + send);

        Intent intent = new Intent(BroadcastAction.SEND_DATA);
        intent.putExtra("data", send);
        LocalBroadcastManager.getInstance(Utils.getContext()).sendBroadcast(intent);
    }

    @Override
    public void onConnected() {
        Log.d(TAG, "Socket Connected");

        Intent intent = new Intent(BroadcastAction.CONNECTED);
        LocalBroadcastManager.getInstance(Utils.getContext()).sendBroadcast(intent);
    }

    @Override
    public void onDisconnected() {
        socket = null;

        Log.d(TAG, "Socket Disconnected");

        Intent intent = new Intent(BroadcastAction.DISCONNECTED);
        LocalBroadcastManager.getInstance(Utils.getContext()).sendBroadcast(intent);
    }

    @Override
    public void onTimeout() {
        socket = null;

        Log.d(TAG, "Socket Connect Timeout");

        Intent intent = new Intent(BroadcastAction.TIMEOUT);
        LocalBroadcastManager.getInstance(Utils.getContext()).sendBroadcast(intent);
    }

    private String formatMessage(String message) {
        int len = message.length();
        if ((len & 1) == 1) {
            message = "0" + message;
        }

        return message;
    }

    private String transAscStr2HexStr(String input){
        String output = "";
        for (char i : input.toCharArray()){
            String s = Integer.toHexString((int)i);
            if ((s.length()&1) == 1) {
                s = "0" + s;
            }
            output += s;
        }

        return output;
    }

    public byte hex2byte(char bit) {
        if (bit >= 'a') {
            bit -= 32;
        }
        if (bit < '0') {
            return 0;
        } else if (bit > 'F') {
            return 15;
        } else if (bit <= '9') {
            return (byte) (bit - '0');
        } else {
            return (byte) (bit - 'A' + 10);
        }
    }
}
