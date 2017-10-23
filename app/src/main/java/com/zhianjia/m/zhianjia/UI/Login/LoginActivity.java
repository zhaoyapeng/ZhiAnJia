package com.zhianjia.m.zhianjia.UI.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.Device.DeviceDetailActivity;
import com.zhianjia.m.zhianjia.UI.User.ModifyUserInfoActivity;
import com.zhianjia.m.zhianjia.UI.widget.KeyboardSpaceView;
import com.zhianjia.m.zhianjia.UI.widget.LoadingView;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.haier.AppDemo;
import com.zhianjia.m.zhianjia.haier.constant.RequestConstant;
import com.zhianjia.m.zhianjia.haier.data.LoginDataModel;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.data.UserAccount;
import com.zhianjia.m.zhianjia.haier.net.route.LoginRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.haier.util.AppUtil;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * 登录界面
 */
public class LoginActivity extends Activity {

    private EditText userNameEditText, passwordEditText;
    private RelativeLayout findPassLayout;
    //    private RoundImageView avatarIcon;
    private Button loginButton;
    private KeyboardSpaceView keyboardSpaceView;
    private LinearLayout passwordLayout, userLayout;
    private View sp1, sp2;
    private NavigationBar navigationBar;
    private TextView registerTextView;

    private String username, password;
    private LoginRoute loginRoute;
    private LoadingView loadingView;
    /**
     * 返回时
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 当界面创建时
     *
     * @param savedInstanceState 保存实例状态的
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginRoute = new LoginRoute();
        loadingView = LoadingView.buildLoadingView(this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        getComponents();
        initView();
        setListener();
//        checkLoginEnabled();
//        updateIcon();
    }

    /**
     * 获取控件
     */
    private void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
//        avatarIcon = (RoundImageView) findViewById(R.id.avatar_image_view);
        userLayout = (LinearLayout) findViewById(R.id.user_layout);
        passwordLayout = (LinearLayout) findViewById(R.id.password_layout);
        findPassLayout = (RelativeLayout) findViewById(R.id.find_password_layout);
        registerTextView = (TextView) findViewById(R.id.register_text_view);
        userNameEditText = (EditText) findViewById(R.id.user_name_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        loginButton = (Button) findViewById(R.id.login_button);
        keyboardSpaceView = (KeyboardSpaceView) findViewById(R.id.keyboard_space_view);
        sp1 = findViewById(R.id.space_1);
        sp2 = findViewById(R.id.space_2);
//        rootLayout = (RelativeLayout) findViewById(R.id.login_root_layout);
    }

    /**
     * 初始化界面
     */
    private void initView() {
//        Utils.displayUserIconImageView(avatarIcon, null);
        User user = FileUtils.getDefaultUser();
        username = (user != null) ? user.getMobile() : "";
        password = "";
//        canLogin = true;

//        username = "15901353186";
//        password = "123456";
        userNameEditText.setText(username);
        navigationBar.setNavigationLoginStyle(null);
//        rootLayout.setBackground(new BitmapDrawable(Utils.readBitMap(this.getApplicationContext(),  R.drawable.login_bg)));
    }

    /**
     * 更新头像显示
     */
    private void updateIcon() {
//        Object obj = FileUtils.getObject(Const.FileKey.ICON_KEY + username);
//        if (obj != null) {
//            String url = (String) obj;
//            Utils.displayUserIconImageView(avatarIcon, url);
//        } else {
//            Utils.displayUserIconImageView(avatarIcon, null);
//        }
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
//            Utils.cancelInput(avatarIcon);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设定监听
     */
    private void setListener() {
        userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.editTextFocus(userNameEditText);
            }
        });

        passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.editTextFocus(passwordEditText);
            }
        });

        keyboardSpaceView.setKeyboardSpaceActionListener(new KeyboardSpaceView.KeyboardSpaceActionListener() {
            @Override
            public void showKeyboard() {
                sp1.setVisibility(View.GONE);
                sp2.setVisibility(View.GONE);
            }

            @Override
            public void hideKeyboard() {
                sp1.setVisibility(View.VISIBLE);
                sp2.setVisibility(View.VISIBLE);
            }
        });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });

        findPassLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFindPassword();
            }
        });

        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                username = userNameEditText.getText().toString();
//                updateIcon();
//                checkLoginEnabled();
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = passwordEditText.getText().toString();
//                checkLoginEnabled();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (canLogin) {
                doLogin();
//                }
            }
        });
    }

    /**
     * 检查是否可登录
     */
    private void checkLoginEnabled() {
        boolean isPass = Utils.isPassword(password);
        if (isPass && username.length() > 4) {
//            loginButton.setBackgroundResource(R.color.colorPrimary);
//            canLogin = true;
        } else {
//            loginButton.setBackgroundResource(R.color.registerDisableColor);
//            canLogin = false;
        }
    }

    /**
     * 打开找回密码
     */
    private void doFindPassword() {
        Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_REGISTER_MOBILE);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Const.IntentKeyConst.KEY_FIND_PASSWORD, true);
        intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_LOGIN);
        startActivity(intent);
    }

    /**
     * 打开注册
     */
    private void doRegister() {
        Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_REGISTER_MOBILE);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_LOGIN);
        startActivity(intent);
    }

    /**
     * 登录
     */
    private void doLogin() {
        username = userNameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (username == null || "".equals(username)) {
            Utils.showToast("账号不能为空");
            return;
        }

        if (password == null || "".equals(password)) {
            Utils.showToast("密码不能为空");
            return;
        }

        Utils.cancelInput(userNameEditText);
        loadingView.showLoading("");
        doLoginHaier();
    }


    private void doZaJ(final User user) {
        loginRoute.loginZhiaj(this, username, password, user.getAccessToken(), new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                loadingView.hideLoading();
                LoginDataModel loginModel = (LoginDataModel) data;
                if (loginModel.getCode() == 0) {
                    if (loginModel.getData() != null) {
                        loginModel.getData().setAccessToken(user.getAccessToken());
                        FileUtils.saveObject(FileUtils.DEFAULT_USER, loginModel.getData());
                    }
                    SharedPreferencesDevice.saveAccessToken(LoginActivity.this, user.getAccessToken());
                    SharedPreferencesDevice.saveZajToken(LoginActivity.this, loginModel.getData().getToken());

                    if(loginModel.getData()!=null&&"1".equals(loginModel.getData().getCreate_new())){
                        Intent intent = new Intent(LoginActivity.this, ModifyUserInfoActivity.class);
                        intent.putExtra("create_new",1);
                        startActivity(intent);
                    }else {
                                  Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_MAIN);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                        startActivity(intent);
                    }

                    finish();
                }

            }
            @Override
            public void onFail(String error) {
                loadingView.hideLoading();
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doLoginHaier() {
        loginRoute.login(this, username, password, new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                LoginModel model = (LoginModel) data;
                if (model.getRetCode().equals(RequestConstant.RETCODE)) {
//                    String session = Util.getuSerSession();
                    User user = new User();
                    user.setAccessToken(model.getHeaders().get("accessToken"));
                    user.setMobile(username);
                    doZaJ(user);
                }else{
                    loadingView.hideLoading();
                    Toast.makeText(LoginActivity.this, model.getRetInfo(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFail(String error) {
                loadingView.hideLoading();
                Toast.makeText(LoginActivity.this, "登录失败请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
