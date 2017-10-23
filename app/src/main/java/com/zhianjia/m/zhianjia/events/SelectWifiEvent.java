package com.zhianjia.m.zhianjia.events;

import com.zhianjia.m.zhianjia.components.data.Wifi;

/**
 * Created by 7 on 2017/1/18.
 */

public class SelectWifiEvent {
    public Wifi wifi;

    public SelectWifiEvent(Wifi wifi) {
        this.wifi = wifi;
    }
}
