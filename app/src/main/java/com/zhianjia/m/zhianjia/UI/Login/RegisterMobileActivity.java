package com.zhianjia.m.zhianjia.UI.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.widget.LoadingView;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.components.network.apply.register.SendSMSApply;
import com.zhianjia.m.zhianjia.components.network.apply.register.VerifySMSCodeApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.haier.AppDemo;
import com.zhianjia.m.zhianjia.haier.constant.RequestConstant;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.data.UserAccount;
import com.zhianjia.m.zhianjia.haier.net.route.LoginRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetResultCode;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;
import com.zhianjia.m.zhianjia.haier.util.Util;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

public class RegisterMobileActivity extends Activity {

    private final int COUNT_DOWN_ACTION_ID = 1;
    private final int COUNT_DOWN_ACTION_TIME_LENGTH = 1000;
    private final int COUNT_DOWN_COUNT = 60;

    private NavigationBar navigationBar;
    private LinearLayout codeLayout, phoneLayout,pwdLayout;
    private TextView resendTextView;
    private RelativeLayout resendLayout;
    private Button nextButton;
    private EditText mobileEditText, smsEditText,pwdEditText;

    private int countDownNumber;
    private String mobileNO;
    private boolean smsEnabled, isFindPassword;

    private LoginRoute loginRoute;
    private LoadingView loadingView;

    /**
     * 主线程Handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == COUNT_DOWN_ACTION_ID) {
                if (--countDownNumber == 0) {
                    resendTextView.setTextColor(getResources().getColor(R.color.registerSendSMSColor));
                    resendTextView.setBackgroundResource(R.drawable.button_gray_border);
                    resendTextView.setText("重新发送");
                    checkIsNextEnabled();
                } else {
                    resendTextView.setText(countDownNumber + "s后重发");
                    resendTextView.setBackgroundResource(R.color.transparent);
                    mHandler.sendEmptyMessageDelayed(COUNT_DOWN_ACTION_ID, COUNT_DOWN_ACTION_TIME_LENGTH);
                }
            }
        }
    };

    /**
     * 当界面创建时
     *
     * @param savedInstanceState 保存实例状态的
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile);
        loginRoute = new LoginRoute();
        loadingView = LoadingView.buildLoadingView(this);
        getComponents();
        setListener();
        initView();
        checkIsNextEnabled();


    }

    /**
     * 初始化界面
     */
    private void initView() {
        isFindPassword = getIntent().getBooleanExtra(Const.IntentKeyConst.KEY_FIND_PASSWORD, false);
        if (isFindPassword) {
            pwdLayout.setVisibility(View.GONE);
            navigationBar.setNavigationLoginStyle("找回密码");
        } else {
            navigationBar.setNavigationLoginStyle("账号注册");
        }
    }

    /**
     * 获取控件
     */
    private void getComponents() {
        codeLayout = (LinearLayout) findViewById(R.id.code_layout);
        phoneLayout = (LinearLayout) findViewById(R.id.phone_layout);
        resendTextView = (TextView) findViewById(R.id.resend_text_view);
        resendLayout = (RelativeLayout) findViewById(R.id.resend_layout);
        mobileEditText = (EditText) findViewById(R.id.mobile_edit_text);
        smsEditText = (EditText) findViewById(R.id.sms_code_edit_text);
        pwdEditText= (EditText) findViewById(R.id.pwd_edit_text);
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        nextButton = (Button) findViewById(R.id.next_button);
        pwdLayout = (LinearLayout) findViewById(R.id.pwd_layout);

    }


    /**
     * 发送短信验证
     */
    private void sendSMS() {
        mobileNO = mobileEditText.getText().toString();
        boolean isMobile = Utils.isMobileNO(mobileNO);

        if (!isMobile) {
            Utils.showToast("请输入正确的手机号码");
            return;
        }
        getCode();
    }

    /**
     * 跳转下一界面
     */
    private void goNext() {
        Intent intent;
        if (isFindPassword) {
            intent = new Intent(Const.Actions.ACTION_ACTIVITY_FIND_PASSWORD);
        } else {
            intent = new Intent(Const.Actions.ACTION_ACTIVITY_REGISTER_INFORMATION);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_REGISTER_MOBILE);
        intent.putExtra(Const.IntentKeyConst.KEY_MOBILE, mobileNO);
        intent.putExtra(Const.IntentKeyConst.KEY_CODE, smsEditText.getText().toString());
        startActivity(intent);
        finish();
    }

    /**
     * 验证验证码
     */
    private void nextStep() {
        mobileNO = mobileEditText.getText().toString();

//        goNext();/*
        boolean isMobile = Utils.isMobileNO(mobileNO);

        if (!isMobile) {
            Utils.showToast("请输入正确的手机号码");
            return;
        }



        if (!isFindPassword&&pwdEditText.getText().toString().length() == 0) {
            Utils.showToast("请输入密码");
            return;
        }

        if (smsEditText.getText().toString().length() == 0) {
            Utils.showToast("请输入验证码");
            return;
        }
        submitNet();
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

        codeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.editTextFocus(smsEditText);
            }
        });

        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.editTextFocus(mobileEditText);
            }
        });

        resendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (smsEnabled) {
                    sendSMS();
//                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });
        mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkIsNextEnabled();
            }
        });

        smsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkIsNextEnabled();
            }
        });
    }

    /**
     * 触摸时
     *
     * @param event 触摸事件
     * @return 返回是否消耗触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Utils.cancelInput(navigationBar);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 检查是否可以进行下一步
     */
    private void checkIsNextEnabled() {
//        boolean isMobile = Utils.isMobileNO(mobileEditText.getText().toString());
//        int len2 = smsEditText.getText().length();
//        if (countDownNumber == 0) {
//            smsEnabled = isMobile;
//            if (isMobile) {
//                mobileNO = mobileEditText.getText().toString();
//                resendTextView.setTextColor(getResources().getColor(R.color.registerSendSMSColor));
//            } else {
//                resendTextView.setTextColor(getResources().getColor(R.color.registerDisableColor));
//            }
//        }
//        Utils.setButtonState(nextButton, (isMobile && (len2 > 3)));
    }


    /**
     * 获取验证码
     * */
    private void getCode(){
        loadingView.showLoading("");
        if(isFindPassword){
            loginRoute.getResetCode(this, mobileNO, new NetWorkCallBack() {
                @Override
                public void onSuccess(Object data) {
                    loadingView.hideLoading();
                    resendTextView.setTextColor(getResources().getColor(R.color.registerDisableColor));
                    countDownNumber = COUNT_DOWN_COUNT;
                    mHandler.removeMessages(COUNT_DOWN_ACTION_ID);
                    mHandler.sendEmptyMessage(COUNT_DOWN_ACTION_ID);
                    Utils.showToast("短信已发送");
                }

                @Override
                public void onFail(String error) {
                    loadingView.hideLoading();
                    Utils.showToast(error);
                }
            });
        }else {
            loginRoute.getCode(this, mobileNO, new NetWorkCallBack() {
                @Override
                public void onSuccess(Object data) {
                    loadingView.hideLoading();
                    LoginModel loginModel = (LoginModel)data;
                    if(TextUtils.equals(RequestConstant.RETCODE,loginModel.getRetCode())){
                        resendTextView.setTextColor(getResources().getColor(R.color.registerDisableColor));
                        countDownNumber = COUNT_DOWN_COUNT;
                        mHandler.removeMessages(COUNT_DOWN_ACTION_ID);
                        mHandler.sendEmptyMessage(COUNT_DOWN_ACTION_ID);
                        Utils.showToast("短信已发送");
                    }else{
                        Toast.makeText(RegisterMobileActivity.this,loginModel.getRetInfo(),Toast.LENGTH_SHORT).show();
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

    /**
     * 注册 or 重置密码
     * */
    public void submitNet(){

        if(isFindPassword){
            goNext();
        }else{
            loadingView.showLoading("");
            loginRoute.registerAndLogin(this, mobileNO,  pwdEditText.getText().toString(),smsEditText.getText().toString(), new NetWorkCallBack() {
                @Override
                public void onSuccess(Object data) {
                    loadingView.hideLoading();
//                {"openId":"28194954","retCode":"00000","retInfo":"成功"}

                    LoginModel loginModel = (LoginModel)data;
                    if(TextUtils.equals(RequestConstant.RETCODE,loginModel.getRetCode())){
                        User user = new User();
                        user.setMobile(mobileNO);
                        user.setAccessToken(loginModel.getHeaders().get("accessToken"));
                        SharedPreferencesDevice.saveAccessToken(RegisterMobileActivity.this,loginModel.getHeaders().get("accessToken"));
                        SharedPreferencesDevice.saveZajToken(RegisterMobileActivity.this, user.getToken());
                        FileUtils.saveObject(FileUtils.DEFAULT_USER,user);
                        Utils.showToast("注册成功");
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
}
