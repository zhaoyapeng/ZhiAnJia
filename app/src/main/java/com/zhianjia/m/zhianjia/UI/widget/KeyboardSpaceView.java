package com.zhianjia.m.zhianjia.UI.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/7/30.
 * 同步键盘高度控件
 */

public class KeyboardSpaceView extends View {

    private int minHeight = 0;
    private boolean calculateToolBar = false;
    private KeyboardSpaceActionListener listener;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public KeyboardSpaceView(Context context) {
        super(context);
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   布局参数
     */
    public KeyboardSpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造函数
     *
     * @param context      上下文
     * @param attrs        布局参数
     * @param defStyleAttr 样式
     */
    public KeyboardSpaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设定键盘事件监听
     *
     * @param listener 键盘事件监听
     */
    public void setKeyboardSpaceActionListener(KeyboardSpaceActionListener listener) {
        this.listener = listener;
    }

    /**
     * 计算布局
     *
     * @param widthMeasureSpec  期待宽度
     * @param heightMeasureSpec 期待高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            minHeight = MeasureSpec.getSize(heightMeasureSpec);
        }

        int h = minHeight;
        int offset = 0;
        Context context = getContext();
        boolean isActivity = context instanceof Activity;
        if (!isInEditMode()) {
            if (isActivity) {
                Rect localRect = new Rect();
                ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
                offset = Utils.getScreenHeight() - localRect.bottom;
                h = offset;
                if (h < minHeight) {
                    h = minHeight;
                }
//                System.out.println(h + localRect.toString() + "jh" + Utils.getScreenHeight() + "dd" + offset);
            }

        } else {
            h = minHeight;
        }

        if (offset > 0) {
            if (calculateToolBar) {
                if (Utils.checkDeviceHasNavigationBar()) {
                    h += Utils.getNavigationBarHeight();
                }
            }
            if (listener != null) {
                listener.showKeyboard();
            }
        } else {
            if (listener != null) {
                listener.hideKeyboard();
            }
        }
        setMeasuredDimension(width, h);//*/
    }

    /**
     * 设定是否计算虚拟按键高度
     *
     * @param calculateToolBar 是否计算虚拟按键高度
     */
    public void setCalculateToolBar(boolean calculateToolBar) {
        this.calculateToolBar = calculateToolBar;
    }

    /**
     * 键盘事件监听
     */
    public interface KeyboardSpaceActionListener {
        /**
         * 键盘显示
         */
        void showKeyboard();

        /**
         * 键盘隐藏
         */
        void hideKeyboard();
    }
}
