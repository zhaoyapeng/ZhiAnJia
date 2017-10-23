package com.zhianjia.m.zhianjia.haier.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.List;

public class AppUtil {

    // 获取当前应用应用名
    public static String getAppName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    // 获取当前应用版本名
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 获取当前应用版本号
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 获取当前应用包名
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    // 获取当前应用图标
    public static Drawable getAppIcon(Context context) {
        return context.getApplicationInfo().loadIcon(context.getPackageManager());
    }

    // 通过进程名获取进程的进程id
    public static int getPid(Context context, String processName) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            if (processName.equals(appProcess.processName)) {
                return appProcess.pid;
            }
        }
        return 0;
    }
//
//    // 安装apk
//    public static void installApk(Context context, String apkPath) {
//        File file = new File(apkPath);
//        Log.e("", "apk size=" + LibIOUtil.getFileSize(file));
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }

    // 启动apk
    public static void launchApk(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    // 通过文件名获取包名
    public static String getPackageName(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            return appInfo.packageName;
        } else {
            return null;
        }
    }

    // 判断本程序 最顶部activity 是否是 此activity
    public static boolean isActivityTopStartThisProgram(Context context, String activityName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = activityManager.getRunningTasks(100);
        if (list != null && list.size() > 0) {
            for (RunningTaskInfo runningTaskInfo : list) {
                Log.e("tag",
                        "runningTaskInfo.topActivity.getClassName() = " + runningTaskInfo.topActivity.getClassName());
                Log.e("tag", "activityName = " + activityName);
                if (context.getPackageName().equals(runningTaskInfo.baseActivity.getPackageName())) {
                    if (runningTaskInfo.topActivity.getClassName().equals(activityName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否已经启动这个程序 且在活动在第一个界面
     *
     * @param context
     * @return
     */
    public static boolean isTopStartProgram(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = activityManager.getRunningTasks(100);
        if (list != null && list.size() > 0) {
            RunningTaskInfo runningTaskInfo = list.get(0);
            if (context.getPackageName().equals(runningTaskInfo.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context
     * @return
     */

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean appIsRun(final Context mContext) {
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = activityManager.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
          if(mContext.getPackageName().equals(info.topActivity.getPackageName())||mContext.getPackageName().equals(info.baseActivity.getPackageName()))
              return true;
        }

        return  false;
    }



    public static boolean isAnimatorVision() {
        return android.os.Build.VERSION.SDK_INT >= 11;
    }


}
