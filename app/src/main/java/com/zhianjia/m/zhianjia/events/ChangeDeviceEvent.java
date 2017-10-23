package com.zhianjia.m.zhianjia.events;

import com.haier.uhome.account.model.UacDevice;
import com.zhianjia.m.zhianjia.components.data.Device;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceItemModel;

/**
 * Created by 7 on 2017/1/18.
 */

public class ChangeDeviceEvent {
    public ZDeviceItemModel device;
    private int position;

    public ChangeDeviceEvent(ZDeviceItemModel device) {
        this.device = device;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
