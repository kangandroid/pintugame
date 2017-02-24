package com.kings.pintugame.modle;

/**
 * author：kk on 2017/2/9 12:35
 * email：kangkai@letoke.com
 */
public class LevelInfo {
    public String url;
    public int levelNum;
    //0表示未过关 ，1表示过关
    public int isOpen = 0;
    public int picCount;

    public boolean isOpen() {
        return isOpen == 1;
    }
}
