package com.kings.pintugame.utils;

import android.app.ProgressDialog;

import com.kings.pintugame.ApplicationImp;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * author：kk on 2017/2/12 10:57
 * email：kangkai@letoke.com
 */
public class RootUtils {
    // 获取ROOT权限
    public void getRoot() {
        if (is_root()) {
            ToastUtils.show("已经具有ROOT权限!");
        } else {
            try {
                ProgressDialog progressDialog = ProgressDialog.show(ApplicationImp.getContext(),
                        "ROOT", "正在获取ROOT权限...", true, false);
                Runtime.getRuntime().exec("su");
            } catch (Exception e) {
                ToastUtils.show("获取ROOT权限时出错!");
            }
        }


    }

    private void RunAsRooter() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void CopyAppToSystem() throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream out = new DataOutputStream(process.getOutputStream());
        out.writeBytes("mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system\n");
        out.writeBytes("cat /sdcard/myApp.adk > /system/app/myApp.adk\n");
        out.writeBytes("mount -o remount,ro -t yaffs2 /dev/block/mtdblock3 /system\n");
        out.writeBytes("exit\n");
        out.flush();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 判断是否具有ROOT权限
    public static boolean is_root() {
        boolean res = false;
        try {
            res = !((!new File("/system/bin/su").exists()) &&
                    (!new File("/system/xbin/su").exists()));
        } catch (Exception e) {

        }
        return res;
    }
}
