package com.kings.pintugame.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.kings.pintugame.R;
import com.kings.pintugame.view.CustomTitleBar;


/**
 * Created by GanQuan on 16/2/20.
 */
public abstract class BaseActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    protected BaseActivity activity = this;
    protected LayoutInflater layoutInflater;
    protected CustomTitleBar titleBar;
    private FrameLayout contentRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_base);
        titleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        initTitleBar();
        contentRoot = (FrameLayout) findViewById(R.id.content_root);
        layoutInflater.inflate(getContentLayout(), contentRoot);

    }

    protected abstract void initTitleBar();

    protected abstract int getContentLayout();

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void onClick(View v) {

    }
}
