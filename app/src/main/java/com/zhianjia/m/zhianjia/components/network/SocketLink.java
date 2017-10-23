package com.zhianjia.m.zhianjia.components.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by ZibetK on 2016/12/24.
 */

public class SocketLink implements Runnable {

    public interface SocketActionListener {
        void onDataReceived(byte[] data);

        void onDataSend(byte[] data);

        void onConnected();

        void onDisconnected();

        void onTimeout();
    }

    private Context context;
    private SocketActionListener listener;
    private String host = "192.168.4.1";
    private int port = 8080;
    private Socket socket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private byte[] content = new byte[0];

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
//                IOException ex = (IOException) msg.obj;
//                ShowDialog("login exception" + ex.getMessage());
            } else if (msg.what == 10) {
                listener.onConnected();
            } else if (msg.what == 11) {
                listener.onDisconnected();
            } else if (msg.what == 12) {
                listener.onTimeout();
            } else {
                listener.onDataReceived(content);
            }
        }
    };


    public SocketLink(Context context, SocketActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void disconnected() {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void link(String host1, final int port1) {
        this.host = host1;
        this.port = port1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket != null) {
                    disconnected();
                }

                try {
                    socket = new Socket();
                    InetAddress address = Inet4Address.getByName(host);
                    socket.connect(new InetSocketAddress(address, port), 5 * 1000);

                    in = socket.getInputStream();
                    out = socket.getOutputStream();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Message message = mHandler.obtainMessage();
                    message.what = 1;
                    message.obj = ex;
                    mHandler.sendMessage(message);

                    mHandler.sendEmptyMessage(12);
                }

                //启动线程，接收服务器发送过来的数据
                new Thread(SocketLink.this).start();
                System.out.println("Socket Launcher Thread Exit");
            }
        }).start();
    }

    public void sendMsg(byte[] msg) {
        if (socket.isConnected()) {
            if (!socket.isOutputShutdown()) {
                try {
                    out.write(msg);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                listener.onDataSend(msg);
            }
        }
    }

    @Override
    public void run() {
        try {
            if (!socket.isClosed() && socket.isConnected()) {
                mHandler.sendEmptyMessage(10);
            }

            while (!socket.isClosed()) {
                if (socket.isConnected()) {
                    if (!socket.isInputShutdown()) {
                        byte[] input = new byte[128];
                        int len;
                        if ((len = in.read(input)) > 0) {
                            content = new byte[len];
                            for (int i = 0; i < len; i++) {
                                content[i] = input[i];
                            }
                            mHandler.sendMessage(mHandler.obtainMessage());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessage(11);
        System.out.println("Socket Listener Thread Closed");
    }

    /**
     * 如果连接出现异常，弹出AlertDialog！
     */
    public void ShowDialog(String msg) {
        new AlertDialog.Builder(context).setTitle("notification").setMessage(msg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

}
