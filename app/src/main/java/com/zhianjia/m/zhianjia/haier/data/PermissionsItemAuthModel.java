package com.zhianjia.m.zhianjia.haier.data;

import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

/**
 * @author zhaoyapeng
 * @version create time:17/8/3014:28
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class PermissionsItemAuthModel extends BaseModel {
    private boolean control;
    private boolean set;
    private boolean view;

    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }
}
