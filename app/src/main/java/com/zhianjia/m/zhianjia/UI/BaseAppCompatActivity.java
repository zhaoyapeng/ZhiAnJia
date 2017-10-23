package com.zhianjia.m.zhianjia.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.zhianjia.m.zhianjia.UI.widget.LoadingView;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/8/9.
 * 界面父类
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    private LoadingView loadingView;

    /**
     * 返回时
     */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Utils.cancelInput(loadingView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 创建时
     *
     * @param savedInstanceState 保存实例状态的
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        initBaseView();
        getComponents();
        initView();
        setListener();
    }

    /**
     * 显示加载
     *
     * @param message 显示加载内容
     */
    protected void showLoading(String message) {
        loadingView.showLoading(message);
    }

    /**
     * 取消加载
     */
    protected void hideLoading() {
        loadingView.hideLoading();
    }

    /**
     * 初始化
     */
    private void initBaseView() {
        loadingView = LoadingView.buildLoadingView(this);
    }

    /**
     * 获取布局ID
     *
     * @return 返回布局ID
     */
    protected abstract int getLayoutResource();

    /**
     * 获取控件
     */
    protected abstract void getComponents();

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 设定监听
     */
    protected abstract void setListener();




}
