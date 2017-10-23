package com.zhianjia.m.zhianjia.haier.data;

import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

/**
 * @author zhaoyapeng
 * @version create time:17/8/2917:22
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class LoginDataModel extends BaseModel {

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
