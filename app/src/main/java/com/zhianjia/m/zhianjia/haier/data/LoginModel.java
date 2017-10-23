package com.zhianjia.m.zhianjia.haier.data;

import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

/**
 * @author zhaoyapeng
 * @version create time:17/5/1710:21
 * @Email zyp@jusfoun.com
 * @Description ${登录 login}
 */
public class LoginModel extends BaseModel {
    private String openId;

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
