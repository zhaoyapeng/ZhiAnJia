package com.zhianjia.m.zhianjia.haier.data;

import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/8/3016:58
 * @Email zyp@jusfoun.com
 * @Description ${智安家 设备列表}
 */
public class ZDeviceListModel extends BaseModel {
    private List<ZDeviceItemModel> data;

    public List<ZDeviceItemModel> getData() {
        return data;
    }

    public void setData(List<ZDeviceItemModel> data) {
        this.data = data;
    }
}
