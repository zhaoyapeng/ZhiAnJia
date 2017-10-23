package com.zhianjia.m.zhianjia.events;

import com.zhianjia.m.zhianjia.components.data.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dp on 2017/1/18.
 */

public class RefreshDevicesEvent {
    public List<Device> devices = new ArrayList<>();

    public RefreshDevicesEvent(List<Device> devices) {
        this.devices = devices;
    }
}
