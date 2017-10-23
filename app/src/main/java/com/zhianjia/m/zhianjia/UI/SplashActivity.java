package com.zhianjia.m.zhianjia.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.components.network.apply.AppUpdateApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 应用开始页
 */
public class SplashActivity extends AppCompatActivity {

    private int SPLASH_NEXT_ID = 1;
    private int SPLASH_NEXT_TIME_LENGTH = 3000;
    /**
     * 主线程Handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == SPLASH_NEXT_ID) {
                checkLoginState();
            }
        }
    };

    /**
     * 界面创建时
     * @param savedInstanceState 保存实例状态的
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加标记，分别是锁屏状态下显示，解锁，保持屏幕长亮，打开屏幕
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager.LayoutParams params = win.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        win.setAttributes(params);
        setContentView(R.layout.activity_splash);

        new AppUpdateApply(new HttpActionListener() {
            @Override
            public void success(String result) {
                System.out.println("AppUpdateApply:" + result);
                JSONTokener jsonParser = new JSONTokener(result);
                try {
                    JSONObject info = (JSONObject) jsonParser.nextValue();
                    int code = info.getInt("code");
                    String msg = info.getString("msg");
                    if (code == 0) {
                        JSONObject data = info.getJSONObject("data");
                        if (data != null) {
                            int versionCode = data.getInt("ver_code");
                            int currentVersionCode = Utils.getAppVersionCode();
                            int isForce = data.getInt("is_force"); // 0-非强制 1-强制更新
                            String changelog = data.getString("change_log");
                            final String url = data.getString("update_url");

                            if (versionCode > currentVersionCode) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                                builder.setTitle("发现新版本");
                                builder.setMessage(changelog);

                                builder.setPositiveButton("更新",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri content_url = Uri.parse(url);
                                            intent.setData(content_url);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                );

                                if (isForce == 0) {
                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            handler.sendEmptyMessageDelayed(SPLASH_NEXT_ID, 1000);
                                        }
                                    });
                                }

                                builder.setCancelable(false);

                                builder.show();
                            } else {
                                handler.sendEmptyMessageDelayed(SPLASH_NEXT_ID, SPLASH_NEXT_TIME_LENGTH);
                            }
                        } else {
                            handler.sendEmptyMessageDelayed(SPLASH_NEXT_ID, SPLASH_NEXT_TIME_LENGTH);
                        }
                    } else {
                        handler.sendEmptyMessageDelayed(SPLASH_NEXT_ID, SPLASH_NEXT_TIME_LENGTH);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessageDelayed(SPLASH_NEXT_ID, SPLASH_NEXT_TIME_LENGTH);
                }
            }

            @Override
            public void failure(String message) {
                handler.sendEmptyMessageDelayed(SPLASH_NEXT_ID, SPLASH_NEXT_TIME_LENGTH);
            }
        });
    }


    private void checkLoginState() {
        if (FileUtils.getDefaultUser() == null) {
            Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_LOGIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
            startActivity(intent);
        }else{
            Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
            startActivity(intent);
        }
        finish();

    }
}
