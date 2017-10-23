package com.zhianjia.m.zhianjia.haier.data;

import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/8/3014:26
 * @Email zyp@jusfoun.com
 * @Description ${海尔云 设备 item}
 */
public class DeviceItemtModel extends BaseModel {
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String online;
    private String wifiType;
    private PermissionsItemAuthModel totalPermission;
    private List<PermissionsItemModel> permissions;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getWifiType() {
        return wifiType;
    }

    public void setWifiType(String wifiType) {
        this.wifiType = wifiType;
    }

    public PermissionsItemAuthModel getTotalPermission() {
        return totalPermission;
    }

    public void setTotalPermission(PermissionsItemAuthModel totalPermission) {
        this.totalPermission = totalPermission;
    }

    public List<PermissionsItemModel> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionsItemModel> permissions) {
        this.permissions = permissions;
    }
}
