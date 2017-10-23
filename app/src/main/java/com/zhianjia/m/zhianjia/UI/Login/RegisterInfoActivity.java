package com.zhianjia.m.zhianjia.UI.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.components.network.apply.register.RegisterUserApply;
import com.zhianjia.m.zhianjia.components.network.apply.user.UploadAvatarApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.UI.widget.KeyboardSpaceView;
import com.zhianjia.m.zhianjia.UI.widget.LoadingView;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.UI.widget.RoundImageView;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * 注册信息
 */
public class RegisterInfoActivity extends Activity {

    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int SELECT_PIC_KITKAT = 3;

    private final String[] DIALOG_ITEMS = new String[]{"选择本地图片", "拍照"};
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

    private NavigationBar navigationBar;
    private RelativeLayout avatarLayout, avatarOuter;
    private LinearLayout usernameLayout;
    private RoundImageView avatarImageView;
    private EditText userNameEditText, passwordEditText, repasswordEditText;
    private TextView mobileTextView;
    private Button commitButton;
    private LoadingView loadingView;
    private KeyboardSpaceView keyboardSpaceView;

    private String userName, password, mobile, iconUrl, repassword;
    private boolean canCommit;

    /**
     * 当界面创建时
     *
     * @param savedInstanceState 保存实例状态的
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        getComponents();
        initView();
        setListener();
        checkCommitButton();
    }

    /**
     * 获取控件
     */
    private void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        usernameLayout = (LinearLayout) findViewById(R.id.username_layout);
        avatarLayout = (RelativeLayout) findViewById(R.id.avatar_layout);
        avatarOuter = (RelativeLayout) findViewById(R.id.change_user_info_avatar_layout);
        avatarImageView = (RoundImageView) findViewById(R.id.avatar_image_view);
        mobileTextView = (TextView) findViewById(R.id.mobile_text_view);
        userNameEditText = (EditText) findViewById(R.id.input_name_edit_text);
        passwordEditText = (EditText) findViewById(R.id.input_password_edit_text);
        repasswordEditText = (EditText) findViewById(R.id.reinput_password_edit_text);
        commitButton = (Button) findViewById(R.id.commit_button);
        keyboardSpaceView = (KeyboardSpaceView) findViewById(R.id.keyboard_space_view);
        loadingView = LoadingView.buildLoadingView(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        Utils.displayUserIconImageView(avatarImageView, null);
        avatarImageView.setBorderColor(getResources().getColor(R.color.registerAvatarBorderColor));
        navigationBar.setNavigationLoginStyle("账号注册");
        mobile = getIntent().getStringExtra(Const.IntentKeyConst.KEY_MOBILE);
        mobileTextView.setText(mobile);
        userName = "";
        password = "";
        repassword = "";
        iconUrl = "";
        canCommit = false;
    }

    /**
     * 设定监听
     */
    private void setListener() {
        avatarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAvatarIcon();
            }
        });
        usernameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.editTextFocus(userNameEditText);
            }
        });
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        keyboardSpaceView.setKeyboardSpaceActionListener(new KeyboardSpaceView.KeyboardSpaceActionListener() {
            @Override
            public void showKeyboard() {
                avatarOuter.setVisibility(View.GONE);
            }

            @Override
            public void hideKeyboard() {
                avatarOuter.setVisibility(View.VISIBLE);
            }
        });

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                userName = userNameEditText.getText().toString();
                checkCommitButton();
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
                checkCommitButton();
            }
        });

        repasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                repassword = repasswordEditText.getText().toString();
                checkCommitButton();
            }
        });

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canCommit) {
                    doCommit();
                }
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
     * 检查提交按钮
     */
    private void checkCommitButton() {
        boolean isPwd = Utils.isPassword(password);
        boolean isRepwd = Utils.isPassword(repassword);
        boolean isPwdRepwdEqual = password.equals(repassword);
        canCommit = isPwd && isRepwd && isPwdRepwdEqual && (userName.length() > 0);
        Utils.setButtonState(commitButton, canCommit);
    }

    /**
     * 检查头像
     */
    private void changeAvatarIcon() {

        new AlertDialog.Builder(this)
                .setTitle("设置头像")
                .setCancelable(true)
                .setItems(DIALOG_ITEMS, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Local Image
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    startActivityForResult(intent, SELECT_PIC_KITKAT);
                                } else {
                                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                                }
                                break;
                            case 1:// Take Picture
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (FileUtils.hasSdcard()) {
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    IMAGE_FILE_NAME)));
                                }
                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    /**
     * 选定头像后
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case SELECT_PIC_KITKAT:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (FileUtils.hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Utils.showToast("未找到存储卡，无法存储照片！");
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    setImageToView();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri 头像URI
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            System.out.println("the uri is not exist");
            return;
            //Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_CUT_AVATAR);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_LOGIN);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = Utils.getPath(this, uri);
            Uri target = Uri.fromFile(new File(url));
            intent.putExtra(Const.IntentKeyConst.KEY_AVATAR_URI, target);
        } else {
            intent.putExtra(Const.IntentKeyConst.KEY_AVATAR_URI, uri);
        }
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setImageToView() {
//        Bitmap photo = UserDataManager.sharedInstance().getNewAvatarIcon();
//        UserDataManager.sharedInstance().setNewAvatarIcon(null);
//        if (photo != null) {
//            loadingView.showLoading("头像上传中");
//            new UploadAvatarApply(photo, new HttpActionListener() {
//                @Override
//                public void success(String result) {
//                    iconUrl = result;
//                    loadingView.hideLoading();
//                }
//
//                @Override
//                public void failure(String message) {
//                    System.out.println(message);
//                    Utils.showToast(message);
//                    loadingView.hideLoading();
//                }
//            });
//            avatarImageView.setImageBitmap(photo);
//        } else {
//            Utils.showToast("头像剪裁失败");
//        }
    }

    /**
     * 提交数据
     */
    private void doCommit() {
        new RegisterUserApply(mobile, password, iconUrl, userName, new HttpActionListener() {
            @Override
            public void success(String result) {
                finish();
            }

            @Override
            public void failure(String message) {
                Utils.showToast(message);
            }
        });
    }

}
