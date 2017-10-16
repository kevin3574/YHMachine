package kzy.com.gyyengineer.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.leanchat.util.LogUtils;
import kzy.com.gyyengineer.leanchat.util.Utils;

/**
 * Created by lzw on 15/4/24.
 */
public class PhotoUtils {
    public static DisplayImageOptions avatarImage = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    //.displayer(new RoundedBitmapDisplayer(20))
                    //.displayer(new FadeInBitmapDisplayer(100))// 淡入
            .build();
    public static DisplayImageOptions avatarImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.chat_default_user_avatar)
            .showImageForEmptyUri(R.drawable.chat_default_user_avatar)
            .showImageOnFail(R.drawable.chat_default_user_avatar)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    //.displayer(new RoundedBitmapDisplayer(20))
                    //.displayer(new FadeInBitmapDisplayer(100))// 淡入
            .build();
    public static DisplayImageOptions avatarlogin = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .displayer(new RoundedBitmapDisplayer(180))
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    //.displayer(new RoundedBitmapDisplayer(20))
                    //.displayer(new FadeInBitmapDisplayer(100))// 淡入
            .build();
    /**
     * 创建人：赵金祥
     * 设置圆角图片
     * 邮箱:cc112837@163.com
     * 创建时间：2016/3/31 22:42
     */
    public static DisplayImageOptions avatarImageOption = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.chat_default_user_corner)
            .showImageForEmptyUri(R.drawable.chat_default_user_corner)
            .showImageOnFail(R.drawable.chat_default_user_corner)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .displayer(new RoundedBitmapDisplayer(180))
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    //.displayer(new RoundedBitmapDisplayer(20))
                    //.displayer(new FadeInBitmapDisplayer(100))// 淡入
            .build();
    private static DisplayImageOptions normalImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.chat_common_empty_photo)
            .showImageForEmptyUri(R.drawable.chat_common_empty_photo)
            .showImageOnFail(R.drawable.chat_common_image_load_fail)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    //.displayer(new RoundedBitmapDisplayer(20))
            .displayer(new FadeInBitmapDisplayer(100))// 淡入
            .build();

    public static void displayImageCacheElseNetwork(ImageView imageView,
                                                    String path, String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                imageLoader.displayImage("file://" + path, imageView, normalImageOptions);
                return;
            }
        }
        imageLoader.displayImage(url, imageView, normalImageOptions);
    }

    public static String compressImage(String path, String newPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int inSampleSize = 1;
        int maxSize = 3000;
        if (options.outWidth > maxSize || options.outHeight > maxSize) {
            int widthScale = (int) Math.ceil(options.outWidth * 1.0 / maxSize);
            int heightScale = (int) Math.ceil(options.outHeight * 1.0 / maxSize);
            inSampleSize = Math.max(widthScale, heightScale);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int newW = w;
        int newH = h;
        if (w > maxSize || h > maxSize) {
            if (w > h) {
                newW = maxSize;
                newH = (int) (newW * h * 1.0 / w);
            } else {
                newH = maxSize;
                newW = (int) (newH * w * 1.0 / h);
            }
        }
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(newPath);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        } catch (FileNotFoundException e) {
            LogUtils.logException(e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        recycle(newBitmap);
        recycle(bitmap);
        return newPath;
    }

    public static void recycle(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
        }
        System.gc();
    }
    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void saveBitmap(String filePath,
                                  Bitmap bitmap) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(out);
        }
    }
}
