package com.zhianjia.m.zhianjia.UI.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/7/19.
 * 标题栏占位控件
 */

public class TitleBarSpaceView extends View {

    /**
     * 构造函数
     *
     * @param context      上下文
     */
    public TitleBarSpaceView(Context context) {
        this(context, null, 0);
    }

    /**
     * 构造函数
     *
     * @param context      上下文
     * @param attrs        布局参数
     */
    public TitleBarSpaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     *
     * @param context      上下文
     * @param attrs        布局参数
     * @param defStyleAttr 样式
     */
    public TitleBarSpaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 测量控件大小
     *
     * @param widthMeasureSpec  预计显示宽度
     * @param heightMeasureSpec 预计显示高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        Context context = getContext();
        int height = 75;
        int sdkVersion = Utils.getAndroidOSVersion();
        if (sdkVersion < 19) {
            height = 0;
        } else if (!isInEditMode()) {
            boolean isActivity = context instanceof Activity;
            if (isActivity) {
                height = Utils.getStatusHeight((Activity) context);
            }
        }
        setMeasuredDimension(width, height);//*/
    }
}
