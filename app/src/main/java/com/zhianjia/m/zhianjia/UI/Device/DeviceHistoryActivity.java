package com.zhianjia.m.zhianjia.UI.Device;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.events.ChangeDeviceEvent;
import com.zhianjia.m.zhianjia.events.DeviceRenameEvent;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceItemModel;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DeviceHistoryActivity extends BaseAppCompatActivity {

//    private static final int LIST_REQ_CODE = 1001;

    private NavigationBar navigationBar;
    private WebView webView;
    private ZDeviceItemModel selectDeviceModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deviceRenameEventHandler(DeviceRenameEvent event) {
//        Device device = event.device;
//        navigationBar.setNavigationLoginStyle(device.getName() + " ▼");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeDeviceEventHandler(ChangeDeviceEvent event) {
//        Device device = event.device;
//        navigationBar.setNavigationLoginStyle(device.getName() + " ▼");
//        webView.loadUrl(Const.NetWork.BASE_WEB_URL + "device.php?token=" + FileUtils.getDefaultUser().getToken() + "&device_id=" + device.getId());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_device_history;
    }

    @Override
    protected void getComponents() {
        if(getIntent().getExtras()!=null&&getIntent().getExtras().getSerializable("selectDeviceModel")!=null)
            selectDeviceModel = (ZDeviceItemModel) getIntent().getExtras().getSerializable("selectDeviceModel");
        Log.e("tag","selectDeviceModel="+selectDeviceModel);
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        webView = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void initView() {
//        if(getIntent()!=null&&getIntent().getStringExtra("name")!=null){
//            navigationBar.setNavigationLoginStyle(getIntent().getStringExtra("name"));
//        }

        if (selectDeviceModel != null)
            navigationBar.setNavigationLoginStyle(selectDeviceModel.getDevice_name());
//        uSDKDevice device = FileUtils.getDefaultDevice();
//        Log.e("tag","device"+device);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading("");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideLoading();
            }
        });

        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setAppCacheEnabled(false);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.loadUrl(Const.NetWork.BASE_WEB_URL + "device.php?token=" + FileUtils.getDefaultUser().getToken() + "&device_id=" + device.getId());
        if (selectDeviceModel != null)
            webView.loadUrl("http://zhianjia.mohetoys.com/device_t.php?token=" + SharedPreferencesDevice.getAzjToken(this) + "&device_id=" + selectDeviceModel.getDevice_id());
//        Log.e("tag","url="+Const.NetWork.BASE_WEB_URL + "device.php?token=" + FileUtils.getDefaultUser().getToken() + "&device_id=" + device.getId());

//        webView.loadUrl("http://zhianjia.mohetoys.com/device_t.php");

    }

    @Override
    protected void setListener() {
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        navigationBar.setCenterAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_DEVICE_LIST);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_MAIN);
                DeviceHistoryActivity.this.startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == LIST_REQ_CODE) {
//            if (resultCode == RESULT_OK) {
//                Device device = (Device) data.getSerializableExtra("P_DEVICE");
//                navigationBar.setNavigationLoginStyle(device.getName() + " ▼");
//
//                FileUtils.saveObject(FileUtils.DEFAULT_DEVICE, device);
//
//                webView.loadUrl(Const.NetWork.BASE_WEB_URL + "device.php?token=" + FileUtils.getDefaultUser().getToken() + "&device_id=" + device.getId());
//            }
//        }
//    }
}
