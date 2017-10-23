package com.zhianjia.m.zhianjia.components.data;

import com.zhianjia.m.zhianjia.haier.net.data.BaseModel;

/**
 * Created by zizi-PC on 2016/7/1.
 * 用户实例
 */

public class User extends BaseModel implements java.io.Serializable {

    private static final long serialVersionUID = 1024L; // 定义序列化ID

    public static int SEARCH_KEY_BIGGER = 1;
    public static int SEARCH_KEY_EQUAL = 0;
    public static int SEARCH_KEY_SMALLER = -1;


    private String user_name;
    private String pinyin;
    private String pinyin_short;
    private long uid;
    private String token;
    private String user_img;
    private String user_tel;
    private String city_name;
    private String address;
    private String area;
    private String accessToken;
    private String create_new;

    public String getCreate_new() {
        return create_new;
    }

    public void setCreate_new(String create_new) {
        this.create_new = create_new;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 设定用户手机号
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.user_tel = mobile;
    }

    /**
     * 获取用户手机号
     * @return 返回用户手机号
     */
    public String getMobile() {
        return user_tel;
    }

    /**
     * 获取用户拼音
     * @return 返回用户拼音
     */
    public String getPinyin() {
        return pinyin;
    }

    /**
     * 设定用户拼音
     * @param pinyin 用户拼音
     */
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }


    public String getCity() {
        return city_name;
    }

    public void setCity(String city) {
        this.city_name = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    /**
     * 设定token
     * @param token 应用Token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 设定用户名
     * @param name 用户名
     */
    public void setName(String name) {
        this.user_name = name;
    }


    /**
     * 设定UID
     * @param uid 用户UID
     */
    public void setUid(long uid) {
        this.uid = uid;
    }

    /**
     * 设定用户头像URL
     * @param url 头像URL
     */
    public void setIconUrl(String url) {
        this.user_img = url;
    }


    /**
     * 获取用户名
     * @return 返回用户名
     */
    public String getName() {
        return user_name;
    }

    /**
     * 获取UID
     * @return 返回UID
     */
    public long getUid() {
        return uid;
    }

    /**
     * 获取应用Token
     * @return 返回应用Token
     */
    public String getToken() {
        return token;
    }

    /**
     * 获取头像URL
     * @return 返回头像URL
     */
    public String getIconUrl() {
        return user_img;
    }


    /**
     * 判断是否是相同用户
     * @param o 判断对象
     * @return 返回是否相同
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User temp = (User) o;
            return temp.getUid() == (uid);
        } else {
            return false;
        }
    }


    /**
     * 判断是否有token
     * @return 返回是否有token
     */
    public boolean hasToken() {
        if (token == null)
            return false;
        else if (token.length() == 0)
            return false;
        else
            return true;
    }

//    /**
//     * 判断是否和目标字符串相似
//     * @param targetStr 目标字符串
//     * @return 返回是否与目标字符串相似
//     */
//    public boolean isSimilarToString(String targetStr) {
//        return (name.contains(targetStr) || searchKey.contains(targetStr.toLowerCase()) || pinyin.contains(targetStr));
//    }

    /**
     * 复制所有字段，排除token
     * @param input 目标用户实例
     */
//    public void copyValue(User input) {
//        copyValue(input, false);
//    }
//
//    /**
//     * 复制所有字段
//     * @param input 目标用户实例
//     * @param isCopyToken 是否复制token
//     */
//    public void copyValue(User input, boolean isCopyToken) {
//
//        name = input.getName();
//        searchKey = input.getSearchKey();
//        pinyin = input.getPinyin();
//        uid = input.getUid();
//        iconUrl = input.getIconUrl();
//        mobile = input.getMobile();
//        if (isCopyToken) {
//            if (token != null && token.length() > 0)
//                token = input.getToken();
//        }
//        _isOnline = input.isOnline();
//    }
}
