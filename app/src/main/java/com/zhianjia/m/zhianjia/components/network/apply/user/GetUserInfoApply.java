package com.zhianjia.m.zhianjia.components.network.apply.user;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.components.network.apply.BaseApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpAction;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.FileUtils;

/**
 * Created by Zibet.K on 2016/8/14.
 * 获取用户信息申请
 */

public class GetUserInfoApply extends BaseApply {

    private String token;

    /**
     * 构造函数
     * @param token 应用Token
     * @param listener 获取信息结果监听
     */
    public GetUserInfoApply(String token, HttpActionListener listener) {
        super(listener);
        this.token = token;
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
        return Const.NetWork.BASE_URL + "get_self_info";
    }

    /**
     * 获取申请信息
     *
     * @return 返回申请信息
     */
    @Override
    protected String getHttpContent() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("token", token);
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
//        System.out.println(data.toString());
//        String userName = data.optString("user_name");
//        String pinyin = data.optString("pinyin");
//        String mobile = data.optString("user_tel");
//        long uid = data.optLong("uid", -1);
//        String img = data.optString("img");
//        String searchKey = data.optString("pinyin_short");
//        String city = data.optString("city");
//        String address = data.optString("address");
//        String area = data.optString("area");
//        if (uid > 0) {
//            User defaultUser = FileUtils.getDefaultUser();
//            defaultUser.setName(userName);
//            defaultUser.setPinyin(pinyin);
//            defaultUser.setMobile(mobile);
//            defaultUser.setIconUrl(img);
//            defaultUser.setSearchKey(searchKey);
//            defaultUser.setCity(city);
//            defaultUser.setAddress(address);
//            defaultUser.setArea(area);
//            FileUtils.saveObject(FileUtils.DEFAULT_USER, defaultUser);
//            if (_listener != null) {
//                _listener.success(result.toString());
//            }
//        } else {
//            if (_listener != null) {
//                _listener.failure("登录没有获取到UID信息");
//            }
//        }
    }

    /**
     * 申请失败
     *
     * @param errorMessage 失败原因
     */
    @Override
    protected void onNetFailure(String errorMessage) {
        System.out.println(errorMessage);
        if (_listener != null) {
            _listener.failure(errorMessage);
        }
    }
}
