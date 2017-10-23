package com.zhianjia.m.zhianjia.UI.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.Login.LoginActivity;
import com.zhianjia.m.zhianjia.UI.widget.LoadingView;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.haier.constant.RequestConstant;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.net.route.LoginRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * 找回密码
 */
public class ModifyUserInfoActivity extends Activity {

    private NavigationBar navigationBar;
    private EditText newPasswordEditText;
    private Button commitButton;

    private String userName;
    private LoadingView loadingView;
    private LoginRoute loginRoute;
    private TextView nameText;
    private int create_new=0;
    /**
     * 当界面创建时
     *
     * @param savedInstanceState 保存实例状态的
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        create_new = getIntent().getIntExtra("create_new",0);
        loginRoute = new LoginRoute();
        userName = getIntent().getStringExtra("userName");
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
        nameText = (TextView)findViewById(R.id.text_name);
        loadingView = LoadingView.buildLoadingView(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        navigationBar.setNavigationLoginStyle("修改昵称");
        userName = "";
        Utils.setButtonState(commitButton, false);
        nameText.setText("新昵称");
        newPasswordEditText.setText(userName);

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
                userName = newPasswordEditText.getText().toString();
                Utils.setButtonState(commitButton, userName.length() > 0);
            }
        });
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });

        navigationBar.setRightAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 提交数据
     */
    private void doCommit() {
        if (userName.length() > 0) {
            loadingView.showLoading("");
            loginRoute.modifyUserInfo(this,  userName, new NetWorkCallBack() {
                @Override
                public void onSuccess(Object data) {
                    loadingView.hideLoading();
                    LoginModel loginModel = (LoginModel)data;
                    if(loginModel.getCode()==0){
                        Utils.showToast("修改成功");
                        User defaultUser = FileUtils.getDefaultUser();
                        if(defaultUser!=null){
                            defaultUser.setName(userName);
                            FileUtils.saveObject(FileUtils.DEFAULT_USER, defaultUser);
                        }
                        finish();
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

    @Override
    public void finish() {
        if(create_new==1){
            Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
            startActivity(intent);
        }
        super.finish();
    }
}
