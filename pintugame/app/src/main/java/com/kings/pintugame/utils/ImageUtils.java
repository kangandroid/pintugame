package com.kings.pintugame.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.kings.pintugame.R;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Devuser on 2015/5/5.
 */
public class ImageUtils {
    private static PauseOnScrollListener mPauseOnScrollListener;
    private static PauseOnScrollListener mPauseOnScrollListenerExtra;
    private static ImageLoader mImageLoader = ImageLoader.getInstance();

    private static DisplayImageOptions mDefaultOption;
    private static DisplayImageOptions mRoundDefault;
    private static DisplayImageOptions mDefaultOption_result;

    public static void displayImage(String imgUrl, ImageView imageView) {
        ImageLoader.getInstance().
                displayImage(imgUrl, imageView, getDefaultOption(true), null);
    }

    public static void displayResImage(@DrawableRes int resId, ImageView imageView) {
        String imgUrl = "drawable://" + resId;
        ImageLoader.getInstance().
                displayImage(imgUrl, imageView, getDefaultOption(), null);
    }

    public static Bitmap loadBitmap(String imgUrl) {
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imgUrl);
        return bitmap;
    }

    private static DisplayImageOptions getDefaultOption() {
        if (mDefaultOption_result == null){
            mDefaultOption_result = new DisplayImageOptions
                    .Builder()
                    .cacheInMemory(true).cacheOnDisk(false)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return mDefaultOption_result;
    }

    private static DisplayImageOptions getDefaultOption(boolean isshow) {
        if (mDefaultOption == null)
            mDefaultOption = new DisplayImageOptions
                    .Builder()
                    .showImageOnLoading(R.drawable.img_loading)
                    .showImageForEmptyUri(R.drawable.img_fail)
                    .showImageOnFail(R.drawable.img_empty)
                    .cacheInMemory(true).cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new FadeInBitmapDisplayer(500))
                    .build();
        return mDefaultOption;
    }

    private static DisplayImageOptions getRoundOption() {
        if (mRoundDefault == null)
            mRoundDefault = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.img_loading)
                    .showImageForEmptyUri(R.drawable.img_fail)
                    .showImageOnFail(R.drawable.img_empty)
                    .cacheInMemory(true).cacheOnDisk(true)
                    .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(180))
                    .build();
        return mRoundDefault;
    }

    private static DisplayImageOptions getRoundOption(float cornerRadiusDip) {
        if (mRoundDefault == null) {
            int cornerRadiusPixels = DensityUtils.dp2px(cornerRadiusDip);
            mRoundDefault = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.img_loading)
                    .showImageForEmptyUri(R.drawable.img_fail)
                    .showImageOnFail(R.drawable.img_empty)
                    .cacheInMemory(true).cacheOnDisk(true)
                    .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                    .build();
        }
        return mRoundDefault;
    }

    public static void loadImage(String imgUrl, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(imgUrl, listener);
    }

    public static void displayGreyImage(String imgUrl, ImageView imageView) {
        ImageLoader.getInstance().displayImage(imgUrl, imageView, greyOptions, null);
    }

    public static void displayRoundImage(String imgUrl, ImageView imageView) {
        ImageLoader.getInstance().displayImage(imgUrl, imageView, getRoundOption(), null);
    }

    public static void displayRoundImage(String imgUrl, ImageView imageView, float cornerRadiusDip) {
        ImageLoader.getInstance().displayImage(imgUrl, imageView, getRoundOption(cornerRadiusDip), null);
    }

    private static DisplayImageOptions greyOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.img_loading)
            .showImageForEmptyUri(R.drawable.img_fail)
            .showImageOnFail(R.drawable.img_empty)
            .cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .postProcessor(new BitmapProcessor() {
                        @Override
                        public Bitmap process(Bitmap bitmap) {
                            return greyBitmap(bitmap);
                        }
                    })
            .build();

    public synchronized static PauseOnScrollListener getScrollListener() {
        if (mPauseOnScrollListener == null) {
            mPauseOnScrollListener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);
            Log.e("gq", "getScrollListener:OnScrollListener()");
        }

        return mPauseOnScrollListener;
    }

    public synchronized static PauseOnScrollListener getScrollListener(AbsListView.OnScrollListener OnScrollListener) {
        if (mPauseOnScrollListenerExtra == null) {
            mPauseOnScrollListenerExtra = new PauseOnScrollListener(ImageLoader.getInstance(), true, true,
                    OnScrollListener);
        }
        Log.e("gq", "getScrollListener:OnScrollListener(OnScrollListener)");
        return mPauseOnScrollListenerExtra;
    }

    /**
     * 灰度话化图片
     */
    private static Bitmap greyBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.01f); // 决定灰度值
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }

    public static void saveImage(Context context, String name, Bitmap bitmap) {
        try {
            FileOutputStream outputStream = context.openFileOutput(name, Context.MODE_WORLD_READABLE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
//            ToastUtil.showToast("背景保存成功");
        } catch (IOException e) {
            e.printStackTrace();
//            ToastUtil.showToast("背景保存失败");
        }

    }


    public static String saveWallPaperImage(Context context, String name, Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "我的");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }

    public static void copyImgToPhoto(Context context, String filePath, String name) {
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, name, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filePath))));
    }

    public static String getImgName(String url) {
        String s[] = url.split("/");
        return s[s.length - 1];
    }

    public static double getCacheSize(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        Log.d("ZipengS",
                "ImageLoader cache path=" + cacheDir.getAbsolutePath() + "\nsize="
                        + FileUtils.calculateFileSize(ImageLoader.getInstance().getDiskCache().getDirectory()));
        long cacheSize = FileUtils.calculateFileSize(ImageLoader.getInstance().getDiskCache().getDirectory());
        double size = (double) cacheSize / (1024 * 1024);

        return size;
    }

    public static void clearCache(Context context) {
        ImageLoader.getInstance().clearMemoryCache();
    }

    public static DiskCache getDiskCache(Context context) {
        return ImageLoader.getInstance().getDiskCache();
    }

    public static class SimpleLoadListener implements ImageLoadingListener{

        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }

}
