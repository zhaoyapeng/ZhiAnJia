package com.zhianjia.m.zhianjia.components.network.apply.register;

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
 * Created by zizi-PC on 2016/8/2.
 * 注册用户申请
 */

public class RegisterUserApply extends BaseApply {

    private long mobile;
    private String password;
    private String iconUrl, userName;

    /**
     * 构造函数
     * @param mobile 用户手机号
     * @param password 用户密码
     * @param iconUrl 用户头像URL
     * @param userName 用户名
     * @param listener 注册申请结果监听
     */
    public RegisterUserApply(String mobile, String password, String iconUrl, String userName, HttpActionListener listener) {
        super(listener);
        this.mobile = Long.parseLong(mobile);
        this.password = Utils.md5(password);
        this.iconUrl = iconUrl;
        this.userName = userName;
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
        return Const.NetWork.BASE_URL + "reg_modify_user";
    }

    /**
     * 获取申请信息
     *
     * @return 返回申请信息
     */
    @Override
    protected String getHttpContent() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_tel", mobile);
            jsonObject.put("user_pwd", password);
            if (iconUrl != null) {
                jsonObject.put("img", iconUrl);
            } else {
                jsonObject.put("img", "");
            }
            jsonObject.put("user_name", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String ret = jsonObject.toString();
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
//        try {
//            String userName = data.optString("user_name");
//            String token = data.optString("token");
//            String pinyin = data.optString("pinyin");
//            long uid = data.optLong("uid", -1);
//            String img = data.optString("img");
//            String searchKey = data.optString("pinyin_short");
//            if (uid > 0) {
//                User user = new User(userName, searchKey, pinyin, img, uid, token);
//                user.setMobile(mobile + "");
//                FileUtils.saveObject(FileUtils.DEFAULT_USER, user);
////                UserDataManager.sharedInstance().setCurrentUser(user);
////                UserDataManager.sharedInstance().loginWithCurrentUser(token, true);
//                _listener.success(result.getString("msg"));
//            } else {
//                _listener.success("登录没有获取到UID信息");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            _listener.success("Json 解析错误");
//        }
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
