package com.zhianjia.m.zhianjia.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zizi-PC on 2016/7/1.
 * 线程池管理
 */

public class ThreadManager {

    /**
     * 单例实例引用
     */
    private static ThreadManager instance = null;

    /**
     * 获取单例实例引用
     *
     * @return 返回单例实例引用，为空则实例化并返回
     */
    public static ThreadManager sharedInstance() {
        if (ThreadManager.instance == null) {
            ThreadManager.instance = new ThreadManager();
        }
        return ThreadManager.instance;
    }

    private ExecutorService executor = null;

    /**
     * 构造函数
     */
    private ThreadManager() {
        executor = Executors.newCachedThreadPool();
    }

    /**
     * 获取新的线程
     *
     * @param runnable 返回新的线程
     */
    public void runNewThread(Runnable runnable) {
        executor.execute(runnable);
    }

}
