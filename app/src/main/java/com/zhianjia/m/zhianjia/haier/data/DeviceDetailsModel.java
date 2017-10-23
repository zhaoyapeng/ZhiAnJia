package com.zhianjia.m.zhianjia.haier.data;

/**
 * @author zhaoyapeng
 * @version create time:17/8/3017:30
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class DeviceDetailsModel {
    private DeviceDetailsItemModel fd_value;//
    private DeviceDetailsItemModel humidity_value;//
    private DeviceDetailsItemModel tvoc_value;//
    private DeviceDetailsItemModel smoke_value;//
    private DeviceDetailsItemModel pm_value;//
    private DeviceDetailsItemModel temp_value;//

    private String create_time;
    private String device_id;
    private String device_name;
    private String is_low_voltage;// 电量低 0 否 1是

    public DeviceDetailsItemModel getFd_value() {
        return fd_value;
    }

    public void setFd_value(DeviceDetailsItemModel fd_value) {
        this.fd_value = fd_value;
    }

    public DeviceDetailsItemModel getHumidity_value() {
        return humidity_value;
    }

    public void setHumidity_value(DeviceDetailsItemModel humidity_value) {
        this.humidity_value = humidity_value;
    }

    public DeviceDetailsItemModel getTvoc_value() {
        return tvoc_value;
    }

    public void setTvoc_value(DeviceDetailsItemModel tvoc_value) {
        this.tvoc_value = tvoc_value;
    }

    public DeviceDetailsItemModel getSmoke_value() {
        return smoke_value;
    }

    public void setSmoke_value(DeviceDetailsItemModel smoke_value) {
        this.smoke_value = smoke_value;
    }

    public DeviceDetailsItemModel getPm_value() {
        return pm_value;
    }

    public void setPm_value(DeviceDetailsItemModel pm_value) {
        this.pm_value = pm_value;
    }

    public DeviceDetailsItemModel getTemp_value() {
        return temp_value;
    }

    public void setTemp_value(DeviceDetailsItemModel temp_value) {
        this.temp_value = temp_value;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getIs_low_voltage() {
        return is_low_voltage;
    }

    public void setIs_low_voltage(String is_low_voltage) {
        this.is_low_voltage = is_low_voltage;
    }
}
