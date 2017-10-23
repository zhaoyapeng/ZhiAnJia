package com.zhianjia.m.zhianjia.components.network.apply.user;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/8/2.
 * 登录申请
 */

public class LoginApply extends BaseApply {

    private long mobile;
    private String password;

    /**
     * 构造函数
     *
     * @param userTel  用户手机号
     * @param password 密码
     * @param listener 登录结果监听
     */
    public LoginApply(String userTel, String password, HttpActionListener listener) {
        super(listener);
        this.mobile = Long.parseLong(userTel);
        this.password = Utils.md5(password);
        doApply();
    }

    /**
     * 获取网络申请方法
     *
     * @return 返回网络申请方法（Post/Get)
     */
    @Override
    protected HttpAction.HttpMethod getMethod() {
        return HttpAction.HttpMethod.POST;
    }

    /**
     * 获取网址
     *
     * @return 返回网址
     */
    @Override
    protected String getUrl() {
        return Const.NetWork.BASE_URL + "login";
    }

    /**
     * 获取申请信息
     *
     * @return 返回申请信息
     */
    @Override
    protected String getHttpContent() {
        Log.d("LoginApply", "usertel:" + mobile + " password:" + password);
        JSONObject obj = new JSONObject();
        try {
            obj.put("usertel", mobile);
            obj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String ret = obj.toString();
        return ret;
    }

    /**
     * 申请成功
     *
     * @param result 成功消息
     * @param data   消息数据
     */
    @Override
    protected void onNetSuccess(JSONObject result, JSONObject data) {
        if (_listener != null) {
//            try {
//                String userName = data.optString("user_name");
//                String token = data.optString("token");
//                String pinyin = data.optString("pinyin");
//                long uid = data.optLong("uid", -1);
//                String img = data.optString("img");
//                String searchKey = data.optString("pinyin_short");
//                if (uid > 0) {
//                    User user = new User(userName, searchKey, pinyin, img, uid, token);
//                    user.setMobile(mobile + "");
//                    FileUtils.saveObject(FileUtils.DEFAULT_USER, user);
////                    UserDataManager.sharedInstance().setCurrentUser(user);
////                    UserDataManager.sharedInstance().loginWithCurrentUser(token, true);
//                    _listener.success(result.getString("msg"));
//                } else {
//                    _listener.failure("登录没有获取到UID信息");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                _listener.failure("Json 解析错误");
//            }
        }
    }

    /**
     * 申请失败
     *
     * @param errorMessage 失败原因
     */
    @Override
    protected void onNetFailure(String errorMessage) {
        if (_listener != null) {
            _listener.failure(errorMessage);
        }
    }

}
