package com.zhianjia.m.zhianjia.UI.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/7/30.
 * 导航栏
 */

public class NavigationBar extends RelativeLayout {

    private final int MESSAGE_SHOW_ID = 0;
    private final int MESSAGE_HIDE_ID = 1;
    private final int MESSAGE_DELAY_ID = 2;
    private final int MESSAGE_ANIMATION_DELAY = 500;
    private final int MESSAGE_SHOW_TIME_LENGTH = 2000;

    private ImageView leftImage, rightImage;
    private TextView titleTextView, leftTextView, rightTextView, messageTextView;
    private RelativeLayout leftArea, rightArea, centerArea, rootLayout, messageLayout;
    private RoundImageView avatarImageView;

    private OnClickListener leftClickListener, rightClickListener, centerClickListener;
    private boolean isShowing;

    public TextView getTitleTextView() {
        return titleTextView;
    }

    /**
     * 主线程Handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == MESSAGE_SHOW_ID) {
                handler.sendEmptyMessageDelayed(MESSAGE_DELAY_ID, MESSAGE_SHOW_TIME_LENGTH);
            } else if (what == MESSAGE_DELAY_ID) {
                hideMessage();
            } else if (what == MESSAGE_HIDE_ID) {
                showMessageInList();
            }
        }
    };

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public NavigationBar(Context context) {
        super(context);
        initView(context);
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   布局参数
     */
    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 构造杉树
     *
     * @param context      上下文
     * @param attrs        布局参数
     * @param defStyleAttr 样式
     */
    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化界面
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        buildComponents(context);
        if (isInEditMode()) {
            return;
        }
        isShowing = false;
        setListener();
        rightArea.setVisibility(GONE);
        leftArea.setVisibility(GONE);
        if (!isInEditMode()) {

        }
    }

    /**
     * 建立布局
     *
     * @param context 上下文
     */
    private void buildComponents(Context context) {
        LayoutInflater.from(context).inflate(R.layout.navigation_bar_layout, this);
        leftArea = (RelativeLayout) findViewById(R.id.leftArea);
        centerArea = (RelativeLayout) findViewById(R.id.centerArea);
        rightArea = (RelativeLayout) findViewById(R.id.rightArea);
        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        messageLayout = (RelativeLayout) findViewById(R.id.message_layout);

        leftImage = (ImageView) findViewById(R.id.leftImageView);
        rightImage = (ImageView) findViewById(R.id.rightImageView);

        leftTextView = (TextView) findViewById(R.id.left_text_view);
        rightTextView = (TextView) findViewById(R.id.right_text_view);
        messageTextView = (TextView) findViewById(R.id.message_text_view);
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        avatarImageView = (RoundImageView) findViewById(R.id.user_avatar_image_view);
    }

    /**
     * 设定监听
     */
    private void setListener() {
        messageLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        leftArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callClickListener(leftClickListener, v);
            }
        });

        centerArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callClickListener(centerClickListener, v);
            }
        });

        rightArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callClickListener(rightClickListener, v);
            }
        });
    }

    /**
     * 触发监听
     *
     * @param listener 目标监听
     * @param view     触发控件
     */
    private void callClickListener(OnClickListener listener, View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    /**
     * 显示提示消息
     *
     * @param message
     */
    public void showToast(String message) {

    }

    /**
     * 显示消息列表中的消息
     */
    public void showMessageInList() {
        if (isShowing) {
            return;
        }

    }

    /**
     * 隐藏消息
     */
    public void hideMessage() {
        isShowing = false;
        messageLayout.setVisibility(GONE);
        messageLayout.clearAnimation();
        messageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.message_hide_anim));
        handler.sendEmptyMessageDelayed(MESSAGE_HIDE_ID, MESSAGE_ANIMATION_DELAY);
    }

    /**
     * 设置左侧区域点击监听
     *
     * @param leftClickListener 左侧区域点击监听
     */
    public void setLeftAreaClickedListener(OnClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    /**
     * 设定中间区域点击监听
     *
     * @param centerClickListener 中间区域点击监听
     */
    public void setCenterAreaClickedListener(OnClickListener centerClickListener) {
        this.centerClickListener = centerClickListener;
    }

    /**
     * 设定右侧区域点击监听
     *
     * @param rightClickListener 右侧区域点击监听
     */
    public void setRightAreaClickedListener(OnClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    /**
     * 设定标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        centerArea.setVisibility(VISIBLE);
        titleTextView.setText(title);
    }

    /**
     * 左侧区域显示图片
     *
     * @param res 图片ID
     */
    public void showLeftArea(int res) {
        leftArea.setVisibility(VISIBLE);
        leftTextView.setVisibility(GONE);
        if (res > 0) {
            setLeftImageRes(res);
        }
    }

    /**
     * 左侧区域显示文字
     *
     * @param label 显示文字
     */
    public void showLeftArea(String label) {
        leftArea.setVisibility(VISIBLE);
        leftTextView.setVisibility(VISIBLE);
        leftTextView.setText(label);
        leftImage.setVisibility(GONE);
        avatarImageView.setVisibility(GONE);
    }

    /**
     * 右侧区域显示图片
     *
     * @param res 图片ID
     */
    public void showRightArea(int res) {
        rightArea.setVisibility(VISIBLE);
        rightTextView.setVisibility(GONE);
        if (res > 0) {
            setRightImageRes(res);
        }
    }

    /**
     * 右侧区域显示文字
     *
     * @param label
     */
    public void showRightArea(String label) {
        Log.e("tag","showRightArea");
        rightArea.setVisibility(VISIBLE);
        rightTextView.setVisibility(VISIBLE);
        rightTextView.setText(label);
        rightImage.setVisibility(GONE);
    }

    /**
     * 隐藏左侧区域
     */
    public void hideLeftArea() {
        leftArea.setVisibility(GONE);
    }

    /**
     * 隐藏右侧区域
     */
    public void hideRightArea() {
        rightArea.setVisibility(GONE);
    }

    /**
     * 隐藏中间区域
     */
    public void hideCenterArea() {
        centerArea.setVisibility(GONE);
    }

    /**
     * 隐藏所有区域
     */
    public void hideAllArea() {
        hideLeftArea();
        hideCenterArea();
        hideRightArea();
    }

    /**
     * 设定左侧区域显示用户头像
     *
     * @param userInfo 用户信息
     */
    public void setLeftAvatarIcon(User userInfo) {
        leftArea.setVisibility(VISIBLE);
        leftTextView.setVisibility(GONE);
        leftImage.setVisibility(GONE);
        avatarImageView.setVisibility(VISIBLE);
        avatarImageView.setBorder(Utils.dip2px(1f));
        if (userInfo != null) {
            Utils.displayUserIconImageView(avatarImageView, userInfo.getIconUrl());
        } else {
            Utils.displayUserIconImageView(avatarImageView, null);
        }
    }

    /**
     * 设定导航栏为登录模式
     *
     * @param title 标题
     */
    public void setNavigationLoginStyle(String title) {
        rootLayout.setBackgroundResource(R.color.transparent);
        showLeftArea(R.drawable.close);
        titleTextView.setTextColor(getResources().getColor(R.color.white));
        if (title == null) {
            hideLeftArea();
            hideCenterArea();
        } else {
            setTitle(title);
        }
        hideRightArea();
    }

    /**
     * 左侧区域显示图片
     *
     * @param res 图片id
     */
    private void setLeftImageRes(int res) {
        leftImage.setVisibility(VISIBLE);
        avatarImageView.setVisibility(GONE);
        leftImage.setImageResource(res);
    }

    /**
     * 设定右侧区域显示图片
     *
     * @param res 图片ID
     */
    private void setRightImageRes(int res) {
        rightImage.setImageResource(res);
    }


}
