package com.zhianjia.m.zhianjia.haier.data;

import com.haier.uhome.account.model.UacDevice;
import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/5/1722:11
 * @Email zyp@jusfoun.com
 * @Description ${海尔云 设备列表model}
 */
public class DeviceListModel extends BaseModel {
    private List<DeviceItemtModel> deviceinfos;

    public List<DeviceItemtModel> getDeviceinfos() {
        return deviceinfos;
    }

    public void setDeviceinfos(List<DeviceItemtModel> deviceinfos) {
        this.deviceinfos = deviceinfos;
    }
}
