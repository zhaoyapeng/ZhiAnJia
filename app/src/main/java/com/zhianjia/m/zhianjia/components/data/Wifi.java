package com.zhianjia.m.zhianjia.components.data;

/**
 * Created by 7 on 2017/1/18.
 */

public class Wifi implements java.io.Serializable {

    private String ssid;
    private String mac;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
