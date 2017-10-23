package com.zhianjia.m.zhianjia.components.data;

/**
 * Created by dp on 2016/12/31.
 */

public class Device implements java.io.Serializable {
    private String id;
    private String name;
    private String updateTime;
    private NatureInfo pmInfo;
    private NatureInfo jqInfo;
    private NatureInfo wdInfo;
    private NatureInfo sdInfo;
    private String address;

    public String getAddress() {
        if (this.address == null || "null".equals(this.address)) {
            return "暂无地址";
        }

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public NatureInfo getPmInfo() {
        return pmInfo;
    }

    public void setPmInfo(NatureInfo pmInfo) {
        this.pmInfo = pmInfo;
    }

    public NatureInfo getJqInfo() {
        return jqInfo;
    }

    public void setJqInfo(NatureInfo jqInfo) {
        this.jqInfo = jqInfo;
    }

    public NatureInfo getWdInfo() {
        return wdInfo;
    }

    public void setWdInfo(NatureInfo wdInfo) {
        this.wdInfo = wdInfo;
    }

    public NatureInfo getSdInfo() {
        return sdInfo;
    }

    public void setSdInfo(NatureInfo sdInfo) {
        this.sdInfo = sdInfo;
    }
}
