package com.zhianjia.m.zhianjia.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.widget.RoundImageView;

import static com.zhianjia.m.zhianjia.config.Const.Update.VERSION_FILE;

/**
 * 工具类
 */
public class Utils {


    private static Context mContext;
    private static DisplayImageOptions mIconOptions, mCaptureCoverOptions,
            mLocalMediaCaptureCoverOptions, mMobileLiveCaptureCoverOptions;

    /**
     * 保存当前版本
     */
    public static void saveCurrentVersion() {
        FileUtils.saveObject(VERSION_FILE, Utils.getAppVersionName());
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 获取数据量大小字符串
     *
     * @param value 数据量
     * @return 返回数据量以及单位
     */
    public static String getSpeedValue(int value) {
        String[] unitArray = {
                "B", "KB", "MB"
        };
        float checkValue = value;
        String u = "TB";
        for (String unit : unitArray) {
            if (checkValue < 1024) {
                u = unit;
                break;
            }
            checkValue = checkValue / 1024;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(checkValue) + u;
    }

    /**
     * 判断是否为新版
     *
     * @return 返回是否是新安装的版本
     */
    public static boolean isNewVersion() {
        Object obj = FileUtils.getObject(VERSION_FILE);
        String currentVersion = Utils.getAppVersionName();
        if (obj != null) {
            String oldVersion = (String) obj;
            if (!oldVersion.equals(currentVersion)) {
                saveCurrentVersion();
                return true;
            } else {
                return false;
            }
        }
        saveCurrentVersion();
        return true;
    }

    /**
     * 取消输入法
     *
     * @param target 取消的控件
     */
    public static void cancelInput(View target) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     *
     * @param target 显示输入法的焦点控件
     */
    public static void editTextFocus(EditText target) {
        target.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        imm.showSoftInput(target, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获取URI中的Bitmap
     *
     * @param uri     原始URI
     * @param maxSize 最大尺寸
     * @return 返回不超过一定尺寸的Bitmap
     */
    public static Bitmap getBitmapFromUri(Uri uri, int maxSize) {
        try {
            // 读取uri所在的图片
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            //获取图片大小
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            input.close();
            //图片过大则设定采样率
            options.inJustDecodeBounds = false;
            int max = Math.max(options.outHeight, options.outWidth);
            if (max > maxSize) {
                int be = 1;
                while (max > maxSize) {
                    max = max >> 1;
                    be = be << 1;
                }
                if (be <= 1)
                    be = 2;
                options.inSampleSize = be;
            }
            //根据采样返回图片
            input = mContext.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
            input.close();
            int w = bitmap.getWidth();

            int h = bitmap.getHeight();

            System.out.println(w + "   " + h);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取路径
     *
     * @param context 上下文
     * @param uri     原始URI
     * @return 返回路径
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 设定按钮状态
     *
     * @param button    目标按钮
     * @param isEnabled 是否可用
     */
    public static void setButtonState(Button button, boolean isEnabled) {
        if (button != null) {
            button.setEnabled(isEnabled);
            if (isEnabled) {
                button.setBackgroundResource(R.drawable.button_normal_bg);
            } else {
                button.setBackgroundResource(R.drawable.button_normal_bg);
            }
        }
    }

    /**
     * 获取上下文
     *
     * @return 返回上下文
     */
    public static Context getContext() {
        if (mContext == null)
            throw new UnsupportedOperationException();
        return mContext;
    }

    /**
     * 显示提示
     *
     * @param toastString 提示内容
     */
    public static void showToast(String toastString) {
        Toast toast = Toast.makeText(mContext,
                toastString, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示长时间提示
     *
     * @param toastString 提示内容
     */
    public static void showToastLong(String toastString) {
        Toast toast = Toast.makeText(mContext,
                toastString, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示带图像的提示
     *
     * @param toastString 提示内容
     * @param imageID     提示图片的ID
     */
    public static void showToast(String toastString, int imageID) {
        Toast toast = Toast.makeText(mContext,
                toastString, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(mContext);
        imageCodeProject.setImageResource(imageID);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

    /**
     * 显示长时间带图片的提示
     *
     * @param toastString 提示内容
     * @param imageID     提示图片的ID
     */
    public static void showToastLong(String toastString, int imageID) {
        Toast toast = Toast.makeText(mContext,
                toastString, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(mContext);
        imageCodeProject.setImageResource(imageID);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

    /**
     * 设定上下文
     *
     * @param ctx 上下文
     */
    public static void setContext(Context ctx) {
        mContext = ctx;
    }

    /**
     * 判断手机格式是否正确
     *
     * @param mobiles 手机号
     * @return 返回是否为手机号
     */
    public static boolean isMobileNO(String mobiles) {
//        String str = "^((13[0-9])|14[57]|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
//        String str = "[0-9]{11,20}";
//        Pattern p = Pattern.compile(str);
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
        return mobiles.length() > 0;
    }

    /**
     * 判断是否是密码
     *
     * @param pwd 密码
     * @return 返回是否为密码格式
     */
    public static boolean isPassword(String pwd) {
        String str = "^\\w{6,20}$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue dp数值
     * @return 返回像素个数
     */
    public static int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue 像素个数
     * @return 返回dp数值
     */
    public static int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取应用版本号
     *
     * @return 返回版本号
     */
    public static int getAppVersionCode() {
        int versionName = 0;
        try {
            // ---get the package info---
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            versionName = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取应用名称
     *
     * @return 返回应用名称
     */
    public static String getAppName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = mContext.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 获取应用版本名称
     *
     * @return 返回应用版本名称
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 显示用户头像
     *
     * @param mImageView 目标ImageView
     * @param imageUrl   头像URL
     * @param listener   显示结果监听
     */
    public static void displayUserIconImageView(RoundImageView mImageView, String imageUrl, ImageLoadingListener listener) {
        if (imageUrl == null || imageUrl.length() == 0 || imageUrl.equals("null")) {
            mImageView.setImageResource(R.drawable.photo);
            return;
        }
        if (mIconOptions == null) {
            mIconOptions = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.photo)
                    .showImageOnFail(R.drawable.photo)
                    .cacheInMemory(true).cacheOnDisk(true)
                    .displayer(new FadeInBitmapDisplayer(1000, true, false, false))
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        ImageLoader.getInstance().displayImage(imageUrl, mImageView, mIconOptions, listener);
    }

    /**
     * 显示用户头像
     *
     * @param mImageView 目标ImageView
     * @param imageUrl   头像URL
     */
    public static void displayUserIconImageView(RoundImageView mImageView, String imageUrl) {
        if (imageUrl == null || imageUrl.length() == 0 || imageUrl.equals("null")) {
            mImageView.setImageResource(R.drawable.photo);
            return;
        }
        if (mIconOptions == null) {
            mIconOptions = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.photo)
                    .showImageOnFail(R.drawable.photo)
                    .cacheInMemory(true).cacheOnDisk(true)
                    .displayer(new FadeInBitmapDisplayer(1000, true, false, false))
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        ImageLoader.getInstance().displayImage(imageUrl, mImageView, mIconOptions, null);
    }

    /**
     * 获取屏幕宽度
     *
     * @return 返回屏幕宽度
     */
    public static int getScreenWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕头部状态栏高度
     *
     * @param activity 当前显示的Activity
     * @return 返回状态栏高度
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取是否有导航栏（虚拟按键）
     *
     * @return 返回是否有导航栏（虚拟按键）
     */
    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = mContext.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 获取导航栏高度（虚拟按键高度）
     *
     * @return 返回导航栏高度（虚拟按键高度）
     */
    public static int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        Resources rs = mContext.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar()) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 获取当前应用的uid
     *
     * @return 返回当前应用的UID
     */
    public static int getAppUid() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ApplicationInfo appinfo = mContext.getApplicationInfo();
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningProcess : run) {
            if ((runningProcess.processName != null) && runningProcess.processName.equals(appinfo.processName)) {
                return Integer.parseInt(String.valueOf(runningProcess.uid));
            }
        }
        return -1;
    }

    /**
     * 获取安卓版本
     *
     * @return 返回安卓版本
     */
    public static int getAndroidOSVersion() {
        return Integer.valueOf(Build.VERSION.SDK_INT);
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
