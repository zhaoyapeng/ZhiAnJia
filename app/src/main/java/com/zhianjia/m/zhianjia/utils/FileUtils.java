package com.zhianjia.m.zhianjia.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.haier.uhome.usdk.api.uSDKDevice;
import com.zhianjia.m.zhianjia.components.data.Device;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.haier.data.UserAccount;
import com.zhianjia.m.zhianjia.haier.util.SharedPreferencesDevice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Zibet.K on 2016/7/20.
 * 文件工具类
 */

public class FileUtils {

    public static final String DEFAULT_USER = "DefaultUser";
    public static final String DEFAULT_DEVICE = "DefaultDevice";
//    public static final String DEFAULT_HAIER_USER = "default_haier_user";

    private static Context _context;

    public static void setContext(Context context) {
        _context = context;
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     *
     * @return 返回是否存在外置存储器
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     * 读取对象
     *
     * @param name 文件名
     * @return 返回对象
     */
    public static Object getObject(String name) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = _context.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }

    /**
     * 保存对象
     *
     * @param name 文件名
     * @param obj  要保存的对象
     */
    public static void saveObject(String name, Serializable obj) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = _context.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }

//    public static User getDefaultUser() {
//        User user = (User) getObject(DEFAULT_USER);
//        if (user == null || user.getToken() == null || "".equals(user.getToken())) {
//            return null;
//        }
//
//        return user;
//    }

    public static User getDefaultUser() {
        User user = (User) getObject(DEFAULT_USER);
//        User user = userAccount.getUser();
        if (user == null || user.getToken() == null || "".equals(user.getToken())) {
            return null;
        }

        return user;
    }

    public static void logout() {
        saveObject(DEFAULT_USER, new User());
        saveObject(DEFAULT_DEVICE, new Device());
    }

    public static uSDKDevice getDefaultDevice() {
        uSDKDevice device = (uSDKDevice) getObject(DEFAULT_DEVICE);
        if (device == null) {
            return null;
        }

        return device;
    }

//
//    public static void saveHaierObject(Serializable obj) {
//        FileOutputStream fos = null;
//        ObjectOutputStream oos = null;
//        try {
//            fos = _context.openFileOutput(DEFAULT_HAIER_USER, MODE_PRIVATE);
//            oos = new ObjectOutputStream(fos);
//            oos.writeObject(obj);
//        } catch (Exception e) {
//            Log.e("tag","saveHaierObject1="+e);
//            e.printStackTrace();
//            //这里是保存文件产生异常
//        } finally {
//            Log.e("tag","saveHaierObject2=");
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    //fos流关闭异常
//                    e.printStackTrace();
//                }
//            }
//            if (oos != null) {
//                try {
//                    oos.close();
//                } catch (IOException e) {
//                    //oos流关闭异常
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    public static UserAccount getDefaultHaierUser() {
//        UserAccount userAccount = (UserAccount) getObject(DEFAULT_HAIER_USER);
//        if (userAccount == null) {
//            return null;
//        }
//
//        return userAccount;
//    }
}
