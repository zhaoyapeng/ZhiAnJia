package com.zhianjia.m.zhianjia.components.data;

/**
 * Created by dp on 2016/12/31.
 */

public class NatureInfo implements java.io.Serializable {
    public static final int LEVEL_LOW = 1;
    public static final int LEVEL_MIDDLE = 2;
    public static final int LEVEL_HIGH = 3;

    private int level; // 1-低|2-中|3-高
    private double value;
    private String desc;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
