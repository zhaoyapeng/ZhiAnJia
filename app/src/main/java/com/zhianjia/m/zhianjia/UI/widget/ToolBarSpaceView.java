package com.zhianjia.m.zhianjia.UI.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/7/30.
 */

public class ToolBarSpaceView extends View {

    /**
     * 构造函数
     *
     * @param context      上下文
     */
    public ToolBarSpaceView(Context context) {
        super(context);
    }

    /**
     * 构造函数
     *
     * @param context      上下文
     * @param attrs        布局参数
     */
    public ToolBarSpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造函数
     *
     * @param context      上下文
     * @param attrs        布局参数
     * @param defStyleAttr 样式
     */
    public ToolBarSpaceView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        int h = 0;
        if (!isInEditMode()) {
            if (Utils.checkDeviceHasNavigationBar()) {
                h = Utils.getNavigationBarHeight();
            }
        } else {
            h = 140;
        }
        setMeasuredDimension(width, h);//*/
    }
}
