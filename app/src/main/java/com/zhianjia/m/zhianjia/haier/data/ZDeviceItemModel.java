package com.zhianjia.m.zhianjia.haier.data;

import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

import java.io.Serializable;

/**
 * @author zhaoyapeng
 * @version create time:17/8/3016:57
 * @Email zyp@jusfoun.com
 * @Description ${智安家 设备 item}
 */
public class ZDeviceItemModel extends BaseModel implements Serializable {
    private String is_online;// 是否在线 0否， 1是
    private String device_name;
    private String state;
    private String address;
    private String self_device;//self_device为1时是智安家设备，0是海尔设备
    private String device_id;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSelf_device() {
        return self_device;
    }

    public void setSelf_device(String self_device) {
        this.self_device = self_device;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
