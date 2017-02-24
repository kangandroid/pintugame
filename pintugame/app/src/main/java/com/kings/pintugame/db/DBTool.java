package com.kings.pintugame.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.kings.pintugame.ApplicationImp;
import com.kings.pintugame.Constant;
import com.kings.pintugame.modle.LevelInfo;
import com.kings.pintugame.utils.FileUtils;
import com.kings.pintugame.utils.SPUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.e;

/**
 * 数据库工具类
 *
 * @author surfo-mengxy
 * @ClassName: DBTool
 * @Description: TODO
 * @date 2014年11月20日 下午5:43:03
 */
public class DBTool {
    static String desDir = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/" + Constant.ASSETS_DB_NAME;
    //    static String desDir = FileUtils.getDBPath() + "/" + Constant.ASSETS_DB_NAME;
    private SQLiteDatabase db;

    public DBTool(Context context) {
        if (!new File(desDir).exists()) {
            copyDB();
        }
        db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public List<LevelInfo> getLevelinfo() {
        synchronized (DBTool.class) {
            List<LevelInfo> list = null;
            if (!db.isOpen()) {
                db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
            }
            String sql = "select * from levelinfo order by levelNum desc";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                list = new ArrayList<LevelInfo>();
                do {
                    LevelInfo levelInfo = new LevelInfo();
                    levelInfo.url = cursor.getString(cursor.getColumnIndex("url"));
                    levelInfo.levelNum = cursor.getInt(cursor.getColumnIndex("levelNum"));
                    levelInfo.isOpen = cursor.getInt(cursor.getColumnIndex("isOpen"));
                    levelInfo.picCount = cursor.getInt(cursor.getColumnIndex("picCount"));
                    list.add(levelInfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return list;
        }
    }

    public List<LevelInfo> getLevelinfoByPage(int pageSize, int pageNum) {
        Log.e("kk", "-----------pageSize:" + pageSize + "--pageNum:" + pageNum);
        synchronized (DBTool.class) {
            List<LevelInfo> list = null;
            int startLevelNum = pageSize * (pageNum - 1);
            if (!db.isOpen()) {
                db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
            }
            String sql = "select * from levelinfo order by levelNum limit " + startLevelNum + "," + pageNum * pageSize;
//            String sql = "select top " + pageSize + " * from levelinfo " +
//                    "where levelNum not in (select top " + startLevelNum +
//                    " levelNum from levelinfo order by levelNum)" +
//                    "order by levelNum";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                list = new ArrayList<>();
                do {
                    LevelInfo levelInfo = new LevelInfo();
                    levelInfo.url = cursor.getString(cursor.getColumnIndex("url"));
                    levelInfo.levelNum = cursor.getInt(cursor.getColumnIndex("levelNum"));
                    levelInfo.isOpen = cursor.getInt(cursor.getColumnIndex("isOpen"));
                    levelInfo.picCount = cursor.getInt(cursor.getColumnIndex("picCount"));
                    list.add(levelInfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return list;
        }
    }


    public void insertLevelInfo(List<LevelInfo> list) {
        if (list != null && list.size() > 0) {
            synchronized (DBTool.class) {
                if (!db.isOpen()) {
                    db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
                }
                String sql = "insert into levelinfo(levelNum,url,isOpen,picCount) values(?,?,?,?)";
                for (int i = 0; i < list.size(); i++) {
                    LevelInfo info = list.get(i);
                    if (!isHasThisLevelInfo(db, "levelinfo", "url", info.url)) {
                        db.execSQL(sql, new Object[]{info.levelNum, info.url,
                                info.isOpen, info.picCount});
                    }
                }
                db.close();
            }
        }
    }


    public void insertLevelInfo(LevelInfo info) {
        if (info != null) {
            synchronized (DBTool.class) {
                if (!db.isOpen()) {
                    db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
                }
                String sql = "insert into levelinfo(levelNum,url,isOpen,picCount) values(?,?,?,?)";
                if (!isHasThisLevelInfo(db, "levelinfo", "url", info.url)) {
                    db.execSQL(sql, new Object[]{info.levelNum, info.url,
                            info.isOpen, info.picCount});
                }
                db.close();
            }
        }
    }

    /**
     * 获取info
     * author mxy
     * create at 2016/8/25 16:04
     */
    public LevelInfo getLevelInfoByLevelNum(int levelNum) {
        LevelInfo levelInfo = null;
        synchronized (DBTool.class) {
            if (!db.isOpen()) {
                db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
            }
            String sql = "select * from levelinfo where levelNum = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{levelNum + ""});
            if (cursor.moveToFirst()) {
                levelInfo = new LevelInfo();
                levelInfo.url = cursor.getString(cursor.getColumnIndex("url"));
                levelInfo.levelNum = cursor.getInt(cursor.getColumnIndex("levelNum"));
                levelInfo.isOpen = cursor.getInt(cursor.getColumnIndex("isOpen"));
                levelInfo.picCount = cursor.getInt(cursor.getColumnIndex("picCount"));
            }
            cursor.close();
            db.close();
        }
        return levelInfo;
    }

    /**
     * 修改Msg阅读状态 changeMsgState
     */
    public void changeLevelState(String url) {
        synchronized (DBTool.class) {
            if (!db.isOpen()) {
                db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
            }
            String sql = "update levelinfo set isOpen = 1 where url = ?";
            db.execSQL(sql, new Object[]{url});
            db.close();
        }

    }

    /**
     * 获取最大的msgid getMaxMsgId
     *
     * @return int 返回类型
     * @Title: getMaxMsgId
     * @Description: TODO
     * @author surfo-mengxy
     * @date 2014年11月20日 下午7:17:34
     */
    public int getMaxMsgId() {
        synchronized (DBTool.class) {
            if (!db.isOpen()) {
                db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
            }
            int max = 0;
            String sql = "select max(url) as m from levelinfo";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                max = cursor.getInt(cursor.getColumnIndex("m"));
            }
            cursor.close();
            db.close();
            return max;
        }

    }

    /**
     * 获取数据库中的消息数量（全部消息/未读消息） getCountMsg
     *
     * @return int 返回类型
     * @Title: getCountMsg
     * @Description: TODO
     * @author surfo-mengxy
     * @date 2014年11月20日 下午7:23:32
     */
    public int getOpenLevelCount() {
        synchronized (DBTool.class) {
            if (!db.isOpen()) {
                db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
            }
            int count = 0;
            String sql = "select count(*) as c from levelinfo where isOpen = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{1 + ""});
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("c"));
            }
            cursor.close();
            db.close();
            return count;
        }

    }

    /**
     * 删除一条消息
     */
    public void delMsg(LevelInfo info) {

        synchronized (DBTool.class) {
            if (!db.isOpen()) {
                db = SQLiteDatabase.openDatabase(desDir, null, SQLiteDatabase.OPEN_READWRITE);
            }
            String sql = "delete from levelinfo where url = ?";
            db.execSQL(sql, new Object[]{info.url});
            db.close();
        }
    }

    /**
     *
     */
    private static boolean isHasThisLevelInfo(SQLiteDatabase db,
                                              String tableName, String clomuName, String url) {
        boolean ishas = false;
        String sql = "select * from " + tableName + " where " + clomuName
                + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{url + ""});
        if (cursor.moveToFirst()) {
            ishas = true;
        }
        cursor.close();
        return ishas;
    }

    public static void copyDB() {
        new Thread() {
            @Override
            public void run() {
                Context context = ApplicationImp.getContext();
                AssetManager assets = context.getAssets();
                try {
                    InputStream inputStream = assets.open(Constant.ASSETS_DB_NAME);
                    FileUtils.copyFileWithBuffer(inputStream, desDir);
                    e("kk", "-----------" + desDir);
                    SPUtils.put(Constant.FIRST_START, false);
                } catch (IOException e) {
                    e("kk", "-----------DB copyed failed");
                }
            }
        }.start();
    }
}
