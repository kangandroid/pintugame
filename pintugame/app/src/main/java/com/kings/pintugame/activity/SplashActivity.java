package com.kings.pintugame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.kings.pintugame.Constant;
import com.kings.pintugame.R;
import com.kings.pintugame.db.DBTool;
import com.kings.pintugame.utils.SPUtils;

import butterknife.BindView;


public class SplashActivity extends FragmentActivity {

    @BindView(R.id.ad_containor)
    FrameLayout adContainor;

    boolean isfirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// 使用自定义布局参数

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);

        isfirstStart = (boolean) SPUtils.get(Constant.FIRST_START, true);
        if (isfirstStart) {
            DBTool.copyDB();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 开屏展示界面的 onDestroy() 回调方法中调用
    }
}
