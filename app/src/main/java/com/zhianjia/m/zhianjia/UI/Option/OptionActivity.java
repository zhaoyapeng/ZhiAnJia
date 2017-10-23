package com.zhianjia.m.zhianjia.UI.Option;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.SplashActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.components.network.apply.AppUpdateApply;
import com.zhianjia.m.zhianjia.components.network.apply.user.LogoutApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.haier.net.route.LoginRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class OptionActivity extends BaseAppCompatActivity {

    private NavigationBar navigationBar;

    private TextView versionTextView, profileTextView;
    private RelativeLayout profileLayout, modifyPwdLayout, checkUpgradeLayout, aboutLayout, logoutLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_option;
    }

    @Override
    protected void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        versionTextView = (TextView) findViewById(R.id.tv_version);
        profileTextView = (TextView) findViewById(R.id.profile_textview);
        profileLayout = (RelativeLayout) findViewById(R.id.profile_layout);
        modifyPwdLayout = (RelativeLayout) findViewById(R.id.modify_pwd_layout);
        checkUpgradeLayout = (RelativeLayout) findViewById(R.id.check_upgrade_layout);
        aboutLayout = (RelativeLayout) findViewById(R.id.about_layout);
        logoutLayout = (RelativeLayout) findViewById(R.id.logout_layout);
    }

    @Override
    protected void initView() {
        navigationBar.setNavigationLoginStyle("设置");
        versionTextView.setText("Version " + Utils.getAppVersionName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        User defaultUser = FileUtils.getDefaultUser();
        if(defaultUser!=null){
            profileTextView.setText(defaultUser.getName());
        }
    }

    @Override
    protected void setListener() {
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_USER_PROFILE);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                OptionActivity.this.startActivity(intent);
            }
        });

        modifyPwdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_MODIFYPWD);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                OptionActivity.this.startActivity(intent);
            }
        });

        checkUpgradeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpgrade();
            }
        });

        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_ABOUT);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                OptionActivity.this.startActivity(intent);
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FileUtils.getDefaultUser() != null) {
                    logout();
                } else {
                    clearDefaultUser();
                }
            }
        });
    }

    private void logout() {
//        new LogoutApply(FileUtils.getDefaultUser().getToken(), new HttpActionListener() {
//            @Override
//            public void success(String result) {
//                clearDefaultUser();
//            }
//
//            @Override
//            public void failure(String message) {
//                clearDefaultUser();
//            }
//        });

         LoginRoute.loginOut(this, new NetWorkCallBack() {
             @Override
             public void onSuccess(Object data) {
                 clearDefaultUser();
             }

             @Override
             public void onFail(String error) {
                 clearDefaultUser();
             }
         });



    }

    private void clearDefaultUser() {
        FileUtils.logout();
        SharedPreferencesDevice.clear(this);
        Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_LOGIN);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_USER_PROFILE);
        OptionActivity.this.startActivity(intent);

        finish();
    }

    private void checkUpgrade() {
        new AppUpdateApply(new HttpActionListener() {
            @Override
            public void success(String result) {
                System.out.println("checkUpgrade:" + result);
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
                            String changelog = data.getString("change_log");
                            final String url = data.getString("update_url");

                            if (versionCode > currentVersionCode) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OptionActivity.this);
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

                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                builder.setCancelable(false);

                                builder.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OptionActivity.this);
                                builder.setMessage("当前为最新版本，无需更新");

                                builder.setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }
                                );

                                builder.setCancelable(false);

                                builder.show();
                            }
                        } else {
                            Utils.showToast("获取版本信息失败");
                        }
                    } else {
                        Utils.showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast("获取版本信息失败");
                }
            }

            @Override
            public void failure(String message) {
                Utils.showToast("获取版本信息失败");
            }
        });
    }
}
