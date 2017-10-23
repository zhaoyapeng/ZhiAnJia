package com.zhianjia.m.zhianjia.UI.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zhianjia.m.zhianjia.utils.Utils;

/**
 * 剪切图片
 */
public class CutImageView extends ImageView {

    private static String TAG = "CutImageView";
    public static int DR_LINE = 0;
    public static int DR_OVAL = 1;
    static final float MAX_SCALE = 3.0f;
    float imageW;
    float imageH;
    float rotatedImageW;
    float rotatedImageH;
    float viewW;
    float viewH;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static final int NONE = 0;// 初始状态
    static final int DRAG = 1;// 拖动
    static final int ZOOM = 2;// 缩放
    static final int ROTATE = 3;// 旋转
    static final int ZOOM_OR_ROTATE = 4; // 缩放或旋转
    int mode = NONE;

    PointF pA = new PointF();
    PointF pB = new PointF();
    PointF mid = new PointF();
    PointF lastClickPos = new PointF();
    long lastClickTime = 0;
    double rotation = 0.0;
    float dist = 1f;
    private FrameLayout m_fldese;
    private Paint paintFrame; //画笔 黑屏
    private Paint paintLine; //画笔白线

    private Paint paintFrameB; //画笔 白屏
    private Paint paintLineB; //画笔黑线


    public boolean isPaint = true;
    private int ScreenWidth = 0;
    private int ScreenHeight = 0;
    private int paintSx = 0;
    private int paintEx = 0;
    private int paintw = 4;
    private int paintSy = 0;
    private int paintEy = 0;

    private int DRTYPE = 0;
    private int DROVALWIDTH = 400;
    private int DLINEWIDTH = 0;

    /**
     * 设定剪裁样式
     *
     * @param type 剪裁样式
     */
    public void setDRtype(int type) {
        this.DRTYPE = type;
        init();
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param fl_dese 布局
     * @param type    剪裁类型
     */
    public CutImageView(Context context, FrameLayout fl_dese, int type) {
        super(context);
        m_fldese = fl_dese;
        DRTYPE = type;
        init();
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param fl_dese 布局
     */
    public CutImageView(Context context, FrameLayout fl_dese) {
        super(context);
        m_fldese = fl_dese;
        init();
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   布局参数
     */
    public CutImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param attrs    布局参数
     * @param defStyle 样式
     */
    public CutImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    /**
     * 初始化界面
     */
    private void init() {
        if (isInEditMode()) {
            DROVALWIDTH = 300;
            setScaleType(ScaleType.MATRIX);
            //获取屏幕高与宽。
            ScreenWidth = 1080;
            ScreenHeight = 1920;
        } else {
            DROVALWIDTH = Utils.dip2px(150);
            setScaleType(ScaleType.MATRIX);
            //获取屏幕高与宽。
            ScreenWidth = Utils.getScreenWidth();
            ScreenHeight = Utils.getScreenHeight();
        }
        //白线
        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStrokeWidth(4);
        paintLine.setAntiAlias(true);
        //黑色
        paintFrame = new Paint();
        paintFrame.setColor(Color.BLACK);
        paintFrame.setStrokeWidth(0);
        paintFrame.setAlpha(150);
        paintFrame.setAntiAlias(true);
        //黑线
        paintLineB = new Paint();
        paintLineB.setColor(Color.BLACK);
        paintLineB.setStrokeWidth(4);
        paintLineB.setAntiAlias(true);
        //白色
        paintFrameB = new Paint();
        paintFrameB.setColor(Color.WHITE);
        paintFrameB.setStrokeWidth(0);
        paintFrameB.setAlpha(150);
        paintFrameB.setAntiAlias(true);
        //计算画笔区域
        if (DRTYPE == DR_OVAL) {
            int centerx = ScreenWidth / 2;
            int centery = ScreenHeight / 2;
            paintSx = centerx - DROVALWIDTH / 2;
            paintSy = centery - DROVALWIDTH / 2;
            paintEx = centerx + DROVALWIDTH / 2;
            paintEy = centery + DROVALWIDTH / 2;
            DLINEWIDTH = DROVALWIDTH;
        } else if (DRTYPE == DR_LINE) {
            paintSx = (ScreenHeight - ScreenWidth - 100) / 2;
            paintEx = paintSx + ScreenWidth;
            DLINEWIDTH = ScreenWidth;

        }

    }

    /**
     * 设定图像
     *
     * @param bm Bitmap图像
     */
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setImageWidthHeight();
    }

    /**
     * 设定图像
     *
     * @param drawable Drawable图像
     */
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setImageWidthHeight();
    }

    /**
     * 设定图像
     *
     * @param resId 图像ID
     */
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setImageWidthHeight();
    }

    /**
     * 设定图像遮罩
     *
     * @param isPaint 是否遮罩
     */
    public void SetImagePaint(boolean isPaint) {
        this.isPaint = isPaint;

    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (DRTYPE == DR_OVAL) {
            if (isPaint) {
                canvas.drawLine(paintSx - paintw, paintSy - paintw, paintEx + paintw, paintSy - paintw, paintLine); //上横线.
                canvas.drawLine(paintSx - paintw, paintEy + paintw, paintEx + paintw, paintEy + paintw, paintLine); //下横线.

                canvas.drawLine(paintSx - paintw, paintSy - paintw, paintSx - paintw, paintEy + paintw, paintLine);//左坚线
                canvas.drawLine(paintEx + paintw, paintSy - paintw, paintEx + paintw, paintEy + paintw, paintLine);//右竖线.
                //画钜形

                RectF rect = new RectF(0, 0, ScreenWidth, paintSy - paintw); //上钜形
                canvas.drawRect(rect, paintFrame);

                rect.set(0, paintEy + paintw, ScreenWidth, ScreenHeight); //下钜形
                canvas.drawRect(rect, paintFrame);

                rect.set(0, paintSy - paintw, paintSx - paintw, paintEy + paintw);//左钜形
                canvas.drawRect(rect, paintFrame);
                rect.set(paintEx + paintw, paintSy - paintw, ScreenWidth, paintEy + paintw);//右钜形
                canvas.drawRect(rect, paintFrame);
            } else {
                canvas.drawLine(paintSx, paintSy, paintEx, paintSy, paintLineB); //上横线.
                canvas.drawLine(paintSx, paintEy, paintEx, paintEy, paintLineB); //下横线.

                canvas.drawLine(paintSx, paintSy, paintSx, paintEy, paintLineB);//左坚线
                canvas.drawLine(paintEx, paintSy, paintEx, paintEy, paintLineB);//右竖线.
                //画钜形

                RectF rect = new RectF(0, 0, ScreenWidth, paintSy); //上钜形
                canvas.drawRect(rect, paintFrameB);

                rect.set(0, paintEy, ScreenWidth, ScreenHeight); //下钜形
                canvas.drawRect(rect, paintFrameB);

                rect.set(0, paintSy, paintSx, paintEy);//左钜形
                canvas.drawRect(rect, paintFrameB);
                rect.set(paintEx, paintSy, ScreenWidth, paintEy);//右钜形
                canvas.drawRect(rect, paintFrameB);

            }


        } else if (DRTYPE == DR_LINE) {
            if (isPaint) {
                //画白线
                canvas.drawLine(0, paintSx - paintw, ScreenWidth, paintSx - paintw, paintLine);
                canvas.drawLine(0, paintEx + paintw, ScreenWidth, paintEx + paintw, paintLine);
                RectF rect = new RectF(0, 0, ScreenWidth, paintSx - paintw);
                canvas.drawRect(rect, paintFrame);
                rect.set(0, paintEx + paintw, ScreenWidth, ScreenHeight);
                canvas.drawRect(rect, paintFrame);

            } else {
                //画黑线
                canvas.drawLine(0, paintSx - paintw, ScreenWidth, paintSx - paintw, paintLineB);
                canvas.drawLine(0, paintEx + paintw, ScreenWidth, paintEx + paintw, paintLineB);
                RectF rect = new RectF(0, 0, ScreenWidth, paintSx - paintw);
                canvas.drawRect(rect, paintFrameB);
                rect.set(0, paintEx + paintw, ScreenWidth, ScreenHeight);
                canvas.drawRect(rect, paintFrameB);
            }
        }
    }

    /**
     * 设定图像宽度和高度
     */
    private void setImageWidthHeight() {
        Drawable d = getDrawable();
        if (d == null) {
            return;
        }

        imageW = d.getIntrinsicWidth();
        imageH = d.getIntrinsicHeight();
        rotatedImageH = Math.min(imageH, imageW);
        rotatedImageW = rotatedImageH;
        initImage();
    }

    /**
     * 大小改变时
     *
     * @param w    宽度
     * @param h    高度
     * @param oldw 旧的宽度
     * @param oldh 旧的高度
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;
        if (oldw == 0) {
            initImage();
        } else {
            fixScale();
            fixTranslation(true);
            setImageMatrix(matrix);
        }
    }

    /**
     * 初始化图片
     */
    private void initImage() {
        if (viewW <= 0 || viewH <= 0 || imageW <= 0 || imageH <= 0) {
            return;
        }
        mode = NONE;
        matrix.setScale(0, 0);
        matrix.setRotate(90);
        initfixScale();
        fixTranslation(true);
        setImageMatrix(matrix);
    }

    /**
     * 初始化缩放
     */
    private void initfixScale() {
        float p[] = new float[9];
        matrix.getValues(p);
        float curScale = Math.abs(p[0]) + Math.abs(p[1]);

        float minScale = Math.min((float) viewW / (float) rotatedImageW,
                (float) viewH / (float) rotatedImageH);
        if (curScale < minScale) {
            if (curScale > 0) {
                double scale = minScale / curScale;
                p[0] = (float) (p[0] * scale);
                p[1] = (float) (p[1] * scale);
                p[3] = (float) (p[3] * scale);
                p[4] = (float) (p[4] * scale);
                matrix.setValues(p);
            } else {
                matrix.setScale(minScale, minScale);
            }
        }
    }

    /**
     * 缩放
     */
    private void fixScale() {
        float p[] = new float[9];
        matrix.getValues(p);
        float curScale = Math.abs(p[0]) + Math.abs(p[1]);

        float minScale = 0.5f;
        if (curScale < minScale) {
            if (curScale > 0) {
                double scale = minScale / curScale;
                p[0] = (float) (p[0] * scale);
                p[1] = (float) (p[1] * scale);
                p[3] = (float) (p[3] * scale);
                p[4] = (float) (p[4] * scale);
                matrix.setValues(p);
            } else {
                matrix.setScale(minScale, minScale);
            }
        }
    }

    /**
     * 最大缩放
     *
     * @return 返回最大缩放
     */
    private float maxPostScale() {
        float p[] = new float[9];
        matrix.getValues(p);
        float curScale = Math.abs(p[0]) + Math.abs(p[1]);

        float minScale = 0.5f;
        float maxScale = Math.max(minScale, MAX_SCALE);
        return maxScale / curScale;
    }

    /**
     * 图片位移
     *
     * @param init 是否是初始化
     */
    private void fixTranslation(boolean init) {
        RectF rect = new RectF(0, 0, imageW, imageH);
        matrix.mapRect(rect);
        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;
        if (DRTYPE == DR_LINE) {
            //图片高度小于剪切高度时,让图片居中显示
            if (height <= DLINEWIDTH) {
                deltaY = paintSx - rect.top + (DLINEWIDTH - height) / 2;
            } else {
                if (rect.top > paintSx && rect.bottom > paintEx)
                    deltaY = paintSx - rect.top;
                else if (rect.bottom < paintEx && rect.top < paintSx)
                    deltaY = paintEx - rect.bottom;
            }
            //图片宽度小于剪切宽度时,让图片居中显示.
            if (width <= DLINEWIDTH) {
                deltaX = 0 - rect.left + (DLINEWIDTH - width) / 2;

            } else {
                if (rect.left > 0 && rect.right > DLINEWIDTH)
                    deltaX = 0 - rect.left;
                else if (rect.right < DLINEWIDTH && rect.left < 0)
                    deltaX = DLINEWIDTH - rect.right;
            }
        } else if (DRTYPE == DR_OVAL) {
            if (init) {
                deltaY = (ScreenHeight - height) / 2;
                if (rect.left < 0) {
                    deltaX = ScreenWidth - rect.right;
                }

            } else {


                {
                    if (rect.bottom < paintEx && rect.top < paintSx)
                        deltaY = paintEx - rect.bottom;
                }


                {
                    if (rect.right < DLINEWIDTH && rect.left < 0)
                        deltaX = DLINEWIDTH - rect.right;
                }
            }
        }

        // LogDebugUtil.i(TAG,"image W:"+width +"H:"+height+"VIEW :"+ScreenWidth+":"+ScreenWidth);
        // LogDebugUtil.i(TAG,"image RECT {left:"+rect.left+"top:"+rect.top+"right:"+rect.right+"bottom"+rect.bottom);
        // LogDebugUtil.i(TAG,"deltaX:"+deltaX+"deltaY"+deltaY);
        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 触摸时
     *
     * @param event 触摸事件
     * @return 是否消耗触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 主点按下
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                pA.set(event.getX(), event.getY());
                pB.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            // 副点按下
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getActionIndex() > 1)
                    break;
                dist = spacing(event.getX(0), event.getY(0), event.getX(1),
                        event.getY(1));
                // 如果连续两点距离大于10，则判定为多点模式
                if (dist > 10f) {
                    savedMatrix.set(matrix);
                    pA.set(event.getX(0), event.getY(0));
                    pB.set(event.getX(1), event.getY(1));
                    mid.set((event.getX(0) + event.getX(1)) / 2,
                            (event.getY(0) + event.getY(1)) / 2);
                    mode = ZOOM_OR_ROTATE;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (mode == DRAG) {
                    if (spacing(pA.x, pA.y, pB.x, pB.y) < 50) {
                        long now = System.currentTimeMillis();
                        if (now - lastClickTime < 500
                                && spacing(pA.x, pA.y, lastClickPos.x,
                                lastClickPos.y) < 50) {
                            doubleClick(pA.x, pA.y);
                            now = 0;
                        }
                        lastClickPos.set(pA);
                        lastClickTime = now;
                    }
                } else if (mode == ROTATE) {
                    int level = (int) Math.floor((rotation + Math.PI / 4)
                            / (Math.PI / 2));
                    //LogDebugUtil.i(TAG,"ACTION_POINTER_UP ROTATE--level:"+level +"-->rotation:"+rotation);
                    if (level == 4)
                        level = 0;
                    matrix.set(savedMatrix);
                    matrix.postRotate(90 * level, mid.x, mid.y);
                    if (level == 1 || level == 3) {
                        float tmp = rotatedImageW;
                        rotatedImageW = rotatedImageH;
                        rotatedImageH = tmp;
                        fixScale();
                    }
                    fixTranslation(false);
                    setImageMatrix(matrix);
                }
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:

                if (mode == ZOOM_OR_ROTATE) {
                    PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x,
                            event.getY(1) - event.getY(0) + pA.y);
                    double a = spacing(pB.x, pB.y, pC.x, pC.y);
                    double b = spacing(pA.x, pA.y, pC.x, pC.y);
                    double c = spacing(pA.x, pA.y, pB.x, pB.y);
                    if (a >= 10) {
                        double cosB = (a * a + c * c - b * b) / (2 * a * c);
                        double angleB = Math.acos(cosB);
                        double PID4 = Math.PI / 4;
                        if (angleB > PID4 && angleB < 3 * PID4) {
                            mode = ROTATE;
                            rotation = 0;
                        } else {
                            mode = ZOOM;
                        }
                    }
                }

                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    pB.set(event.getX(), event.getY());
                    matrix.postTranslate(event.getX() - pA.x, event.getY() - pA.y);
                    fixTranslation(false);
                    setImageMatrix(matrix);

                } else if (mode == ZOOM) {
                    float newDist = spacing(event.getX(0), event.getY(0),
                            event.getX(1), event.getY(1));
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float tScale = Math.min(newDist / dist, maxPostScale());
                        matrix.postScale(tScale, tScale, mid.x, mid.y);
                        fixScale();
                        fixTranslation(false);
                        setImageMatrix(matrix);
                    }
                } else if (mode == ROTATE) {
                    PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x,
                            event.getY(1) - event.getY(0) + pA.y);
                    double a = spacing(pB.x, pB.y, pC.x, pC.y);
                    double b = spacing(pA.x, pA.y, pC.x, pC.y);
                    double c = spacing(pA.x, pA.y, pB.x, pB.y);
                    if (b > 10) {
                        double cosA = (b * b + c * c - a * a) / (2 * b * c);
                        double angleA = Math.acos(cosA);
                        double ta = pB.y - pA.y;
                        double tb = pA.x - pB.x;
                        double tc = pB.x * pA.y - pA.x * pB.y;
                        double td = ta * pC.x + tb * pC.y + tc;
                        if (td > 0) {
                            angleA = 2 * Math.PI - angleA;
                        }
                        rotation = angleA;
                        //LogDebugUtil.i(TAG,"ACTION_POINTER_UP ROTATE--Math.PI:"+Math.PI +"-->rotation:"+rotation);
                        matrix.set(savedMatrix);
                        matrix.postRotate((float) (rotation * 180 / Math.PI),
                                mid.x, mid.y);
                        fixTranslation(false);
                        setImageMatrix(matrix);
                    }

                }
                break;
        }
        return true;
    }

    /**
     * 设定旋转
     */
    public void setRotate() {
        //旋转
        matrix.postRotate(90);
        fixTranslation(true);
        setImageMatrix(matrix);

    }

    /**
     * 两点的距离
     *
     * @param x1 点1 x
     * @param y1 点1 y
     * @param x2 点2 x
     * @param y2 点2y
     * @return 返回距离
     */
    private float spacing(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 双点击消失
     *
     * @param x 坐标x
     * @param y 坐标y
     */
    private void doubleClick(float x, float y) {
        // this.setVisibility(GONE);
        //  m_fldese.removeView(this);
    }

    /**
     * 获取BITMAP
     *
     * @return 返回BITMAP
     */
    public Bitmap getBitmap() {
        Bitmap bm = null;
        try {
            this.setDrawingCacheEnabled(true);
            int x = 0;
            int y = 0;
            int w = 0;
            int h = 0;

            if (DRTYPE == DR_LINE) {
                x = 0;
                y = paintSx;
                w = h = ScreenWidth;
                h = ScreenWidth;
            } else if (DRTYPE == DR_OVAL) {
                x = paintSx;
                y = paintSy;
                w = h = DROVALWIDTH;
            }

            int output = 500;
            bm = Bitmap.createBitmap(output, output, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            canvas.drawBitmap(getDrawingCache(), new Rect(x, y, x + w, y + h), new Rect(0, 0, output, output), null);

            System.out.println(bm.getWidth() + "," + bm.getHeight());

            this.setDrawingCacheEnabled(false);
            //LogDebugUtil.i(TAG,"getBitmap W:H ->"+bm.getWidth()+":"+bm.getHeight());

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
            //LogDebugUtil.e(TAG,e.getMessage());
        }
        return bm;
    }

}