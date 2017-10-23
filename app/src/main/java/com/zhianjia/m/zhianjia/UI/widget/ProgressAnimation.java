
package com.zhianjia.m.zhianjia.UI.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * Created by Zibet.K on 2016/8/9.
 * 圆形进度条
 */

public class ProgressAnimation extends SurfaceView implements SurfaceHolder.Callback {

    private final float MIN_SPEED = 40f;
    private final float A = 10f;
    private final float DRAW_WIDTH = 5;
    private float MAX_ANGLE = 360f;

    private SurfaceHolder holder;
    private RenderThread renderThread;
    private RectF drawRect;
    private int width, height;
    private boolean isDraw = false;// 控制绘制的开关
    private long lastDrawTime;
    private float deltaTime;
    private Paint p, p1, pb;

    private float max_angle = 0f;

    private float stAngle, edAngle, stSpeed, edSpeed, halfMax, offsetAngle;
    private int state;

    private boolean viewCreated, needStart;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public ProgressAnimation(Context context) {
        super(context);
        initView();
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   布局参数
     */
    public ProgressAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 构造函数
     *
     * @param context      上下文
     * @param attrs        布局参数
     * @param defStyleAttr 样式
     */
    public ProgressAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 设定百分比
     *
     * @param percent 百分比（0-1）
     */
    public void setPercent(float percent) {
        if (percent < 0) {
            percent = 0;
        } else if (percent > 1) {
            percent = 1;
        }
        max_angle = percent * 360f;
    }

    /**
     * 初始化界面
     */
    private void initView() {
        if (isInEditMode()) {
            return;
        }
        holder = this.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);//设置画布  背景透明

        p = new Paint();
        p.setColor(getResources().getColor(R.color.progressAnimationLight));
        p.setStrokeWidth(Utils.dip2px(DRAW_WIDTH));
        p.setStyle(Paint.Style.STROKE);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));

        p1 = new Paint();
        p1.setColor(getResources().getColor(R.color.colorPrimary));
        p1.setStrokeWidth(Utils.dip2px(DRAW_WIDTH));
        p1.setStyle(Paint.Style.STROKE);

        pb = new Paint();
        pb.setColor(getResources().getColor(R.color.registerDisableColor));
        pb.setStrokeWidth(Utils.dip2px(DRAW_WIDTH));
        pb.setStyle(Paint.Style.STROKE);

        halfMax = MAX_ANGLE / 2;
        viewCreated = false;
        needStart = false;
    }

    /**
     * 停止动画
     */
    public void stop() {
        isDraw = false;
        if (renderThread != null) {
            try {
                renderThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始动画
     */
    public void start() {
        if (viewCreated) {
            needStart = false;
            isDraw = true;
            needStart = true;
            renderThread = new RenderThread();
            renderThread.start();
            stAngle = 0;
            edAngle = 0;
            stSpeed = 0;
            edSpeed = 0;
            offsetAngle = -90;
            state = 0;
        } else {
            needStart = true;
        }
    }

    /**
     * 计算显示区域
     *
     * @param widthMeasureSpec  预计显示宽度
     * @param heightMeasureSpec 预计显示高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isInEditMode()) {
            return;
        }
        int w = Utils.dip2px(DRAW_WIDTH);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        drawRect = new RectF(w, w, width - w, height - w);
    }

    /**
     * 界面创建
     *
     * @param holder 界面绘制实例
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        viewCreated = true;
        if (needStart) {
            start();
        }
    }

    /**
     * 界面变化
     *
     * @param holder 界面绘制实例
     * @param format 绘制格式
     * @param width  宽度
     * @param height 高度
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * 界面销毁
     *
     * @param holder 界面绘制实例
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDraw = false;
    }

    /**
     * 绘制界面的线程
     *
     * @author Administrator
     */
    private class RenderThread extends Thread {
        @Override
        public void run() {
            // 不停绘制界面
            lastDrawTime = System.currentTimeMillis();
            while (isDraw) {
                long currentTime = System.currentTimeMillis();
                deltaTime = (float) (currentTime - lastDrawTime) / 1000;
                lastDrawTime = currentTime;
                calculateAnimation();
                drawUI();
            }
            super.run();
        }
    }

    /**
     * 界面绘制
     */
    public void drawUI() {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            try {
                drawCanvas(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    holder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 计算动画参数
     */
    private void calculateAnimation() {
        if (state == 0) {
            if (stAngle < halfMax) {
                stSpeed += A;
            } else if (stAngle < MAX_ANGLE) {
                stSpeed -= A;
                if (stSpeed < MIN_SPEED) {
                    stSpeed = MIN_SPEED;
                }
            } else {
                state = 1;
                stSpeed = 0;
                stAngle = MAX_ANGLE;
                edAngle = 0;
                edSpeed = 0;
            }
            stAngle += stSpeed * deltaTime;
        } else if (state == 1) {
            if (edAngle < halfMax) {
                edSpeed += A;
            } else if (edAngle < MAX_ANGLE) {
                edSpeed -= A;
                if (edSpeed < MIN_SPEED) {
                    edSpeed = MIN_SPEED;
                }
            } else {
                state = 0;
                stAngle = 0;
                stSpeed = 0;
                edAngle = 0;
                edSpeed = 0;
            }
            edAngle += edSpeed * deltaTime;
        }
        offsetAngle = max_angle - 90;

    }

    /**
     * 绘制界面
     *
     * @param canvas 绘制画板
     */
    private void drawCanvas(Canvas canvas) {
        // 在 canvas 上绘制需要的图形
        float angle = edAngle + offsetAngle;
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawArc(drawRect, 0, 360, false, pb);
        canvas.drawArc(drawRect, angle, stAngle - edAngle, false, p);
        canvas.drawArc(drawRect, -90, max_angle, false, p1);

    }

}
