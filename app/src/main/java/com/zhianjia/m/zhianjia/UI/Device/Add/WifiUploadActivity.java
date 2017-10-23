package com.zhianjia.m.zhianjia.UI.Device.Add;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.components.network.apply.user.ChangePasswordApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

public class WifiUploadActivity extends BaseAppCompatActivity {

    private NavigationBar navigationBar;
    private EditText ssidEditText, passwordEditText;
    private Button commitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_wifi_upload;
    }

    @Override
    protected void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        ssidEditText = (EditText) findViewById(R.id.et_ssid);
        passwordEditText = (EditText) findViewById(R.id.et_pwd);
        commitButton = (Button) findViewById(R.id.commit_button);
    }

    @Override
    protected void initView() {
        navigationBar.setNavigationLoginStyle("添加设备");
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


    }
}
