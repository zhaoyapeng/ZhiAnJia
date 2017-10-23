package com.zhianjia.m.zhianjia.UI.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.components.network.apply.register.ResetPasswordApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.UI.widget.LoadingView;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.haier.constant.RequestConstant;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.net.route.LoginRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * 找回密码
 */
public class FindPasswordActivity extends Activity {

    private NavigationBar navigationBar;
    private EditText newPasswordEditText;
    private Button commitButton;

    private String newPassword, mobile,code;
    private LoadingView loadingView;
    private LoginRoute loginRoute;
    /**
     * 当界面创建时
     *
     * @param savedInstanceState 保存实例状态的
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        loginRoute = new LoginRoute();
        getComponents();
        initView();
        setListener();
    }

    /**
     * 获取界面控件
     */
    private void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        newPasswordEditText = (EditText) findViewById(R.id.new_pass_edit_text);
        commitButton = (Button) findViewById(R.id.commit_button);
        mobile = getIntent().getStringExtra(Const.IntentKeyConst.KEY_MOBILE);
        code = getIntent().getStringExtra(Const.IntentKeyConst.KEY_CODE);
        loadingView = LoadingView.buildLoadingView(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        navigationBar.setNavigationLoginStyle("重置密码");
        newPassword = "";
        Utils.setButtonState(commitButton, false);
    }

    /**
     * 设定监听
     */
    private void setListener() {
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPassword = newPasswordEditText.getText().toString();
                Utils.setButtonState(commitButton, newPassword.length() > 0);
            }
        });
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });
    }

    /**
     * 提交数据
     */
    private void doCommit() {
        if (newPassword.length() > 0) {
            loadingView.showLoading("");
            loginRoute.resetPwd(this, mobile,  newPassword,code, new NetWorkCallBack() {
                @Override
                public void onSuccess(Object data) {
                    loadingView.hideLoading();
                    LoginModel loginModel = (LoginModel)data;
                    if(TextUtils.equals(RequestConstant.RETCODE,loginModel.getRetCode())){
                        Utils.showToast("修改成功");
                        Intent intent = new Intent(FindPasswordActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        Utils.showToast(loginModel.getRetInfo());
                    }

                }

                @Override
                public void onFail(String error) {
                    loadingView.hideLoading();
                    Utils.showToast(error);
                }
            });
        }
    }
}
