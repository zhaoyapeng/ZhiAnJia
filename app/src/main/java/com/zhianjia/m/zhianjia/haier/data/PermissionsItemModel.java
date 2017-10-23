package com.zhianjia.m.zhianjia.haier.data;

import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/8/3014:28
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class PermissionsItemModel extends BaseModel {
    private PermissionsItemAuthModel auth;
    private String authType;

    public PermissionsItemAuthModel getAuth() {
        return auth;
    }

    public void setAuth(PermissionsItemAuthModel auth) {
        this.auth = auth;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
