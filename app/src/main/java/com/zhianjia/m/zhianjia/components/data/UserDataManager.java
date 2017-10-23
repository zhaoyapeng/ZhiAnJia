package com.zhianjia.m.zhianjia.components.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.zhianjia.m.zhianjia.components.network.apply.user.LogoutApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by zizi-PC on 2016/7/1.
 * 用户数据管理
 */

public class UserDataManager {
    /**
     * 单例实例
     */
    private static UserDataManager instance = null;

    /**
     * 活动共享实例
     *
     * @return 返回共享实例
     */
    public static UserDataManager sharedInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    //Uid 索引
    private HashMap<Long, User> userMap;
    //用户列表
    private ArrayList<User> friendsList, searchList;
    //当前用户
    private User currentUser;
    //搜索关键字
    private String searchKey;
    //当前头像
    private Bitmap currentIocn;

    /**
     * 构造函数
     */
    private UserDataManager() {
        userMap = new HashMap<>();
        friendsList = new ArrayList<>();
        searchList = new ArrayList<>();
        searchKey = "";
    }

    /**
     * 设定当前头像
     *
     * @param icon 头像
     */
    public void setNewAvatarIcon(Bitmap icon) {
        currentIocn = icon;
    }

    /**
     * 获取当前头像
     *
     * @return 返回头像
     */
    public Bitmap getNewAvatarIcon() {
        return currentIocn;
    }

    /**
     * 保存当前用户登录信息到文件
     */
    public void saveCurrentUserAtLocal() {
        FileUtils.saveObject(Const.FileKey.CURRENT_USER_KEY, currentUser);
    }

    /**
     * 保存当前用户好友列表到本地文件
     */
    public void saveCurrentUserListAtLocal() {
        long uid = currentUser.getUid();
        FileUtils.saveObject(Const.FileKey.USER_LIST_KEY + uid, friendsList);
    }

    /**
     * 加载好友列表
     *
     * @param uid 用户UID
     */
    public void loadFriendsList(long uid) {
        Object obj = FileUtils.getObject(Const.FileKey.USER_LIST_KEY + uid);
        friendsList.clear();
        userMap.clear();
        if (obj != null) {
            friendsList = (ArrayList<User>) obj;
            for (User user : friendsList) {
                userMap.put(user.getUid(), user);
            }
        }
    }

    /**
     * 加载当前用户信息
     */
    public void loadLocalCurrentUser() {
        Object obj = FileUtils.getObject(Const.FileKey.CURRENT_USER_KEY);
        if (obj != null) {
            User temp = (User) obj;
            currentUser = temp;
            loginWithCurrentUser(temp.getToken(), false);
        }
    }

    /**
     * 通过UID获取用户实例
     *
     * @param uid 目标UID
     * @return 返回指定UID用户实例
     */
    public User getUserByUID(long uid) {
        return userMap.get(uid);
    }

    /**
     * 获取当前用户
     *
     * @return 返回当前用户
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * 判断是否登录
     *
     * @return 返回是否登录
     */
    public boolean isLogin() {
        if (currentUser == null) {
            return false;
        } else if (currentUser.hasToken()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设定当前用户
     *
     * @param currentUser 当前用户
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * 登录当前用户
     *
     * @param token      应用Token
     * @param isSaveUser 是否保存用户
     */
    public void loginWithCurrentUser(String token, boolean isSaveUser) {
        searchList.clear();
        friendsList.clear();
        FileUtils.saveObject(Const.FileKey.ICON_KEY + currentUser.getMobile(), currentUser.getIconUrl());
        loadFriendsList(currentUser.getUid());
        if (isSaveUser) {
            saveCurrentUserAtLocal();
        }
    }

    /**
     * 登出当前用户
     */
    public void logoutCurrentUser() {
        new LogoutApply(currentUser.getToken(), new HttpActionListener() {
            @Override
            public void success(String result) {
                doLogout();
            }

            @Override
            public void failure(String message) {
                doLogout();
            }
        });
    }

    /**
     * 获取当前用户手机号
     *
     * @return 返回用户手机号
     */
    public String getLastUserMobile() {
        if (currentUser != null) {
            return currentUser.getMobile();
        }
        return "";
    }

    /**
     * 登出
     */
    public void doLogout() {
        friendsList.clear();
        searchList.clear();
        currentUser.setToken(null);
        saveCurrentUserAtLocal();
    }

    /**
     * 获取当前应用Token
     *
     * @return
     */
    public String getCurrentUserToken() {
        if (currentUser == null) {
            return null;
        } else {
            return currentUser.getToken();
        }
    }

    /**
     * 获取当前用户UID
     *
     * @return 返回当前用户UID
     */
    public long getCurrentUserUID() {
        if (currentUser == null) {
            return -1;
        } else {
            return currentUser.getUid();
        }
    }

}
