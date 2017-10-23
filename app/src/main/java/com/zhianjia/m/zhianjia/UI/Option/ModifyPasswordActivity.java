package com.zhianjia.m.zhianjia.UI.Option;

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

public class ModifyPasswordActivity extends BaseAppCompatActivity {

    private NavigationBar navigationBar;
    private EditText oldPwdEditText, newPwdEditText, rePwdEditText;
    private Button commitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        oldPwdEditText = (EditText) findViewById(R.id.et_oldpwd);
        newPwdEditText = (EditText) findViewById(R.id.et_newpwd);
        rePwdEditText = (EditText) findViewById(R.id.et_repwd);
        commitButton = (Button) findViewById(R.id.commit_button);
    }

    @Override
    protected void initView() {
        navigationBar.setNavigationLoginStyle("修改密码");
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
        String oldPwd = oldPwdEditText.getText().toString();
        String newPwd = newPwdEditText.getText().toString();
        String rePwd = rePwdEditText.getText().toString();

        if (oldPwd == null || "".equals(oldPwd)) {
            Utils.showToast("原密码不能为空");
            return;
        }

        if (newPwd == null || "".equals(newPwd)) {
            Utils.showToast("新密码不能为空");
            return;
        }

        if (rePwd == null || "".equals(rePwd)) {
            Utils.showToast("确认密码不能为空");
            return;
        }

        if (!newPwd.equals(rePwd)) {
            Utils.showToast("原密码与确认密码不一致");
            return;
        }

        new ChangePasswordApply(FileUtils.getDefaultUser().getToken(), oldPwd, newPwd, new HttpActionListener() {
            @Override
            public void success(String result) {
                Utils.showToast("密码修改成功");

                finish();
            }

            @Override
            public void failure(String message) {
                Utils.showToast(message);
            }
        });
    }
}
