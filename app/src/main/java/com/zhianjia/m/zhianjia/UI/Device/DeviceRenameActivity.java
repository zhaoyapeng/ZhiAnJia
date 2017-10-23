package com.zhianjia.m.zhianjia.UI.Device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.haier.uhome.account.model.UacDevice;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.events.DeviceRenameEvent;
import com.zhianjia.m.zhianjia.haier.constant.RequestConstant;
import com.zhianjia.m.zhianjia.haier.data.LoginModel;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceItemModel;
import com.zhianjia.m.zhianjia.haier.net.route.DeviceDetailRoute;
import com.zhianjia.m.zhianjia.haier.net.volley.NetWorkCallBack;
import com.zhianjia.m.zhianjia.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class DeviceRenameActivity extends BaseAppCompatActivity {

    private static final String TAG = "DeviceRenameActivity";

    public static final String P_RENAME_DEVICE = "P_RENAME_DEVICE";


    private NavigationBar navigationBar;
    private EditText newNameEditText;
    private Button saveButton;

    private String deviceId, deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        deviceId = intent.getStringExtra("deviceId");
        deviceName = intent.getStringExtra("deviceName");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_device_rename;
    }

    @Override
    protected void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        newNameEditText = (EditText) findViewById(R.id.et_newname);
        saveButton = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void initView() {
        navigationBar.setNavigationLoginStyle("修改昵称");
        newNameEditText.setHint(deviceName);
    }

    @Override
    protected void setListener() {
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rename();
            }
        });
    }

    private void rename() {
        final String newName = newNameEditText.getText().toString();

        if (newName == null || "".equals(newName)) {
            Utils.showToast("新昵称不能为空");
            return;
        }

        if (deviceName.equals(newName)) {
            Utils.showToast("新昵称与旧昵称一致");
            return;
        }
//
//        new ModifyDeviceNameApply(FileUtils.getDefaultUser().getToken(), device.getId(), newName, new HttpActionListener() {
//            @Override
//            public void success(String result) {
//                Log.d(TAG, "ModifyDeviceNameApply: " + result);
//                JSONTokener jsonParser = new JSONTokener(result);
//                try {
//                    JSONObject info = (JSONObject) jsonParser.nextValue();
//                    int code = info.getInt("code");
//                    String msg = info.getString("msg");
//                    if (code == 0) {
//                        Utils.showToast("修改成功");
//
//                        device.setName(newName);
//
//                        Intent intent = new Intent();
//                        intent.putExtra(P_RENAME_DEVICE, device);
//                        setResult(RESULT_OK, intent);
//
//                        EventBus.getDefault().post(new DeviceRenameEvent(device));
//
//                        finish();
//                    } else {
//                        Utils.showToast(msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Utils.showToast("昵称修改失败");
//                }
//            }
//
//            @Override
//            public void failure(String message) {
//                Utils.showToast("昵称修改失败");
//            }
//        });


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("name", newName);
        DeviceDetailRoute.updateDeviceName(DeviceRenameActivity.this, hashMap, new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                LoginModel model = (LoginModel) data;
                if (RequestConstant.RETCODE.equals(model.getRetCode())) {
                    ZDeviceItemModel device = new ZDeviceItemModel();
                    device.setDevice_id(deviceId);
                    device.setDevice_name(newName);

                        Intent intent = new Intent();
                        intent.putExtra(P_RENAME_DEVICE, device);
                        setResult(RESULT_OK, intent);
                    EventBus.getDefault().post(new DeviceRenameEvent(device));

                    Utils.showToast("修改成功");
                    finish();
                } else {
                    Utils.showToast(model.getRetInfo());
                }

            }

            @Override
            public void onFail(String error) {

            }
        });
    }
}
