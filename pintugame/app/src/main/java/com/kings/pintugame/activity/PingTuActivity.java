package com.kings.pintugame.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.kings.pintugame.R;
import com.kings.pintugame.base.BaseActivity;
import com.kings.pintugame.utils.ImageUtils;
import com.kings.pintugame.view.PinTuView;
import com.nostra13.universalimageloader.core.assist.FailReason;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.util.Log.e;

public class PingTuActivity extends BaseActivity {

    private static final int DATA_CHECK_OVER = 222;

    @BindView(R.id.gv_pingtu)
    PinTuView pingTuView;
    int maxPicNum = 1;
    @BindView(R.id.tv_steps)
    TextView tvSteps;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private String imgUrl;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (DATA_CHECK_OVER == msg.what) {
                maxPicNum = (int) msg.obj;
                e("kk", "-----------maxPicNum:" + maxPicNum);
            }
        }
    };
    private int rowCount;
    private int columnCount;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }





    private void initView() {
        rowCount = 4;
        columnCount = 3;

        pingTuView.init(rowCount, columnCount);
        imgUrl = getIntent().getStringExtra("imageUrl_1");
        pingTuView.setOnMoveListener(new PinTuView.OnMoveListener() {
            @Override
            public void onMove(int stepCount) {
                tvSteps.setText("已移动-" + stepCount + "-步");
            }
            @Override
            public void onSuccess(int stepCount) {
                Dialog dialog = new Dialog(activity);
                dialog.setTitle("拼图成功");
                View inflate = View.inflate(activity, R.layout.dialog_success, null);
                TextView msg = (TextView) inflate.findViewById(R.id.success_msg);
                msg.setText("恭喜闯关成功！经过"+stepCount+"步。");
                dialog.setContentView(inflate);
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        backwithResult();
                    }
                });

            }
        });
        ImageUtils.loadImage(imgUrl, new ImageUtils.SimpleLoadListener() {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pingTuView.setBitmap(loadedImage);
            }
        });
    }

    private void backwithResult() {
        Intent intent = new Intent();
        intent.putExtra("isopen",true);
        setResult(200,intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onStart();

    }

    @Override
    protected void initTitleBar() {
        titleBar.setCenterTitle("拼图游戏");
        titleBar.setLeftIconRes(R.mipmap.ic_back);
        titleBar.setLeftAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pintu;
    }




    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
