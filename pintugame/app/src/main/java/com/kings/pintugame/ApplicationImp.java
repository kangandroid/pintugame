package com.kings.pintugame;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.kings.pintugame.utils.FileUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

/**
 * author：kk on 2016/12/6 19:39
 * email：kangkai@letoke.com
 */
public class ApplicationImp extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initImageLoader(mContext);
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 初始化universal-image-loader
     */
    public static void initImageLoader(Context context) {
        //设置缓存图片路径
        String cachePath = context.getCacheDir().getAbsolutePath() + "/imgs";
        File catchDir = new File(FileUtils.getSDPath(cachePath));
        File file = Environment.getDataDirectory();
        Log.e("kk", "-----------file：" +file.getAbsolutePath() );
        File filesDir = context.getFilesDir();
        Log.e("kk", "-----------filesDir：" +filesDir.getAbsolutePath() );
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                //设置缓存图片地址
                .discCache(new UnlimitedDiskCache(catchDir))
//                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量
                // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCache((new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)))
                .memoryCacheSize(2 * 1024 * 1024)
                //缓存sdcard大小
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //缓存的文件数量
                .discCacheFileCount(100)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // connectTimeout (5 s), readTimeout (10 s)超时时间
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 10 * 1000))
//                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
