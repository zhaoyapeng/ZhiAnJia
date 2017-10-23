package com.zhianjia.m.zhianjia;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.zhianjia.m.zhianjia.haier.AppDemo;
import com.zhianjia.m.zhianjia.utils.CLog;
import com.zhianjia.m.zhianjia.utils.FileUtils;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by 7 on 2016/12/29.
 */

public class ZhiAnJiaApplication extends AppDemo {
    private static ZhiAnJiaApplication _instance;

    public static ZhiAnJiaApplication sharedInstance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        initSystem();
    }

    /**
     * 初始化系统
     */
    public void initSystem() {
        boolean isDebug = BuildConfig.DEBUG;
        CLog.setDebuggable(isDebug);
        Context context = getApplicationContext();
        initImageLoader(context);
        Utils.setContext(context);
        FileUtils.setContext(context);
//        UserDataManager.sharedInstance().loadLocalCurrentUser();
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getApplicationContext(), "58b50ea98f4a9d4576001b77", "MAIN", MobclickAgent.EScenarioType.E_UM_NORMAL, true));
        MobclickAgent.setDebugMode(true);
    }

    /**
     * 初始化ImageLoader
     *
     * @param context 上下文
     */
    public void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 当内存不足时
     */
    @Override
    public void onLowMemory() {
        CLog.e("App onLowMemory:");
        super.onLowMemory();
    }

    /**
     * 当缩减内存时
     *
     * @param level 缩减等级
     */
    @Override
    public void onTrimMemory(int level) {
        CLog.e("App onTrimMemory:");
        super.onTrimMemory(level);
    }
}
