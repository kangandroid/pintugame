package com.kings.pintugame.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.kings.pintugame.ApplicationImp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * 文件工具类
 */
public class FileUtils {

    public static long calculateFileSize(File file) {
        if (file == null)
            return 0;
        long result = 0;
        if (file.isDirectory()) {
            for (File sub : file.listFiles())
                result += calculateFileSize(sub);
        } else {
            result = file.length();
        }
        return result;
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 检查文件是否存在
     */
    public static boolean isExits(String fileName) {
        if (isSDCardEnable()) {
            String dir = Environment.getExternalStorageDirectory() + fileName;
            File fileDir = new File(dir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File[] files = fileDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                Log.e("test", f.getName() + "时间：" + f.lastModified());
            }
        }
        return false;
    }

    /**
     * 获取sdcard剩余容量
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * 获取sd卡总容量
     */
    public static long getSDAllSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * 设置缓存路径
     */
    public static String getSDPath(String filePath) {

        if (isSDCardEnable()) {
            String dir = Environment.getExternalStorageDirectory() + filePath;// 获取跟目�?
            File sdDir = new File(dir);
            if (!sdDir.exists()) {
                sdDir.mkdirs();
            }
            return sdDir.toString();
        }
        return null;
    }


    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 容量转换工具
     */
    public static String formatSize(long size) {
        String suffix = null;
        float fSize = 0;

        if (size >= 1024) {
            suffix = "KB";
            fSize = size / 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
            }
            if (fSize >= 1024) {
                suffix = "GB";
                fSize /= 1024;
            }
        } else {
            fSize = size;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static void copyFileTo(File src, String desDir) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(src);
            outStream = new FileOutputStream(desDir + "/" + src.getName());
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inStream);
            close(in);
            close(outStream);
            close(out);
        }
    }


    public static void copyFileWithBuffer(File src, String desDir) {
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(src));
            fos = new BufferedOutputStream(new FileOutputStream(desDir + "/" + src.getName()));
            byte[] buf = new byte[1024];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis);
            close(fos);
        }
    }

    public static void copyFileWithBuffer(InputStream fis, File desDir) {
        OutputStream fos = null;
        if (!desDir.exists()) {
            desDir.mkdir();
        }
        try {
            fos = new BufferedOutputStream(new FileOutputStream(desDir));
            byte[] buf = new byte[1024];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis);
            close(fos);
        }
    }

    public static void copyFileWithBuffer(InputStream fis, String desDir) {
        OutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(desDir));
            byte[] buf = new byte[1024];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis);
            close(fos);
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                closeable = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getApplicatioDataPath() {
        String cacheDir = ApplicationImp.getContext().getCacheDir().getAbsolutePath();
        return cacheDir.substring(0, cacheDir.lastIndexOf("/"));
    }

    public static String getDBPath() {
        String desDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        return desDir + "/pintuyouxi";
    }

}
