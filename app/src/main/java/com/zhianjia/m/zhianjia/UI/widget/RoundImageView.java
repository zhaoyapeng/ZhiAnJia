package com.zhianjia.m.zhianjia.UI.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆形图片框
 */
public class RoundImageView extends ImageView {

    private int borderWidth = 0;
    private static int borderColor = 0xfff8f8f8;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public RoundImageView(Context context) {
        super(context);

    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   布局参数
     */
    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param attrs    布局参数
     * @param defStyle 样式
     */
    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 设定边框宽度
     *
     * @param width 边框宽度
     */
    public void setBorder(int width) {
        borderWidth = width;
    }

    /**
     * 设定边框颜色
     *
     * @param color 边框颜色
     */
    public void setBorderColor(int color) {
        borderColor = color;
    }

    /**
     * 绘制图像
     *
     * @param canvas 画布
     */
    protected void onDraw(Canvas canvas) {
        int roundPx = borderWidth;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(borderColor);
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = null;
        try {
            bitmap = b.copy(Config.RGB_565, false);
        } catch (Exception e) {
        }
        int w = getWidth();
        if (bitmap != null) {
            Bitmap roundBitmap = getCroppedBitmap(bitmap, w, roundPx);

            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawCircle(w / 2, w / 2, w / 2 - 1, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        }
    }

    /**
     * 绘制圆形图片
     *
     * @param bmp     图片
     * @param length  图片宽度/高度
     * @param roundPx 圆形直径
     * @return 返回圆形图片
     */
    public static Bitmap getCroppedBitmap(Bitmap bmp, int length, int roundPx) {

        Bitmap scaleBmp;
        int midLength = length >> 1;
        if (bmp.getWidth() != length || bmp.getHeight() != length)
            scaleBmp = Bitmap.createScaledBitmap(bmp, length, length, false);
        else
            scaleBmp = bmp;

        Bitmap output = Bitmap.createBitmap(length, length, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setColor(Color.WHITE); //bedef0
        canvas.drawCircle(midLength, midLength, length / 2 - 2, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaleBmp, 0, 0, paint);

        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundPx + 2);
        canvas.drawCircle(midLength, midLength, (length - roundPx) / 2, paint);

        return output;
    }
} 
