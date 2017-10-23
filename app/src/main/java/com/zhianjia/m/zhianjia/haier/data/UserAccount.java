package com.zhianjia.m.zhianjia.haier.data;

import com.haier.uhome.account.model.UacDevice;
import com.haier.uhome.usdk.api.uSDKDevice;
import com.zhianjia.m.zhianjia.components.data.User;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Express U+ Cloud Account
 *
 */
public class UserAccount implements Serializable{
    private String userName;
    private String password;
    private String userId;
    private String session;
    private String accessToken;
    private String token;
    private User user;
    private JSONArray devicesJsonArray;
    private UacDevice[] devicesOfAccount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public JSONArray getDevicesJsonArray() {
        return devicesJsonArray;
    }

    public void setDevicesJsonArray(JSONArray devicesJsonArray) {
        this.devicesJsonArray = devicesJsonArray;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
    public boolean isSmartDeviceBelongAccount(uSDKDevice smartDeviceItem) {
        String smartDeviceId = smartDeviceItem.getDeviceId();
        if(devicesJsonArray != null) {
            for (int i = 0; i < devicesJsonArray.length(); i++) {
                JSONObject deviceJsonItem = null;
                try {
                    deviceJsonItem = devicesJsonArray.getJSONObject(i);
                    String deviceJsonId = deviceJsonItem.getString("id");
                    if(smartDeviceId.equals(deviceJsonId)) {
                        return true;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

        }

        return false;

    }*/

    public boolean isSmartDeviceBelongAccount(uSDKDevice smartDeviceItem) {
        String smartDeviceId = smartDeviceItem.getDeviceId();

        UacDevice[] deviceItems = getDevicesOfAccount();
        if(deviceItems != null) {
            for (int i = 0; i < deviceItems.length; i++) {
                UacDevice deviceItem = null;
                deviceItem = deviceItems[i];
                String deviceId = deviceItem.getId();
                if(smartDeviceId.equals(deviceId)) {
                    return true;

                }

            }

        }

        return false;

    }

    public boolean isLogin() {
        if(getSession() == null) {
            return false;

        } else {
            return true;

        }
    }

    public UacDevice[] getDevicesOfAccount() {
        return devicesOfAccount;
    }

    public void setDevicesOfAccount(UacDevice[] devicesOfAccount) {
        this.devicesOfAccount = devicesOfAccount;
    }
}
