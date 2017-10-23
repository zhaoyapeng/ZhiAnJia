package com.zhianjia.m.zhianjia.UI.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhianjia.m.zhianjia.R;

/**
 * Created by Zibet.K on 2016/7/12.
 * 加载界面
 */

public class LoadingView extends RelativeLayout {
    /**
     * 加载界面统一添加方式
     *
     * @param activity 要添加 加载界面 的activity
     * @return 返回 加载界面
     */
    public static LoadingView buildLoadingView(Activity activity) {
        LoadingView view = new LoadingView(activity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.addContentView(view, params);
        view.hideLoading();
        return view;
    }

    private LoadingAnimation animation;
    private TextView loadingTextView;
    private int loadingCount = 0;
    private ProgressBar progressBar;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    private LoadingView(Context context) {
        super(context);
        buildComponents(context);
    }

    /**
     * 显示加载中
     *
     * @param loadingString 加载中提示文字
     */
    public void showLoading(String loadingString) {
//        this.loadingTextView.setText(loadingString);
//        if (loadingCount++ == 0) {
//            this.setVisibility(View.VISIBLE);
//            animation.start();
//            animation.setVisibility(VISIBLE);
//        }
        setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏加载中
     */
    public void hideLoading() {
//        if (loadingCount <= 1) {
//            this.setVisibility(View.GONE);
//            animation.setVisibility(GONE);
//            animation.stop();
//            loadingCount = 0;
//        } else {
//            loadingCount--;
//        }

        setVisibility(View.GONE);
    }

    /**
     * 获取控件
     *
     * @param context 上下文
     */
    private void buildComponents(Context context) {
        LayoutInflater.from(context).inflate(R.layout.loading_layout, this);
        animation = (LoadingAnimation) findViewById(R.id.loading_animation);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        loadingTextView = (TextView) findViewById(R.id.LoadingTextView);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
