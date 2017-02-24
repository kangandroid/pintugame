package com.kings.pintugame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.kings.pintugame.R;
import com.kings.pintugame.adapter.LevelListAdapter;
import com.kings.pintugame.base.BaseActivity;
import com.kings.pintugame.db.DBTool;
import com.kings.pintugame.modle.LevelInfo;
import com.kings.pintugame.utils.ToastUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rv_mms)
    RecyclerView rvMms;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout refreshLayout;
    public static final int PAGE_SIZE = 21;
    private int currentPageNum = 1;
    private List<String> mmImageUrlList = new ArrayList<>();
    private LevelListAdapter mAdapter;
    private DBTool dbTool;
    private LevelInfo clickLevel;
    private int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        dbTool = new DBTool(activity);
        initView();
    }

    protected void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false);
        rvMms.setLayoutManager(gridLayoutManager);
        rvMms.setHasFixedSize(false);
        mAdapter = new LevelListAdapter();
        mAdapter.addItems(loadMore());
        mAdapter.setOnItemClickListener(new LevelListAdapter.OnItemClickListener() {
            @Override
            public void OnClick(LevelInfo info , int position) {
                clickPosition  = position;
                clickLevel = info;
                if (info.isOpen()) {
                    openPhoto(info);
                } else {
                    openPinTu(info);
                }
            }
        });
        rvMms.setAdapter(mAdapter);
        setupRecyclerView();
    }

    private void openPhoto(LevelInfo info) {
        Intent intent = new Intent(activity, PhotosActivity.class);
        intent.putExtra("max_pic_number", info.picCount);
        intent.putExtra("imageUrl_1", info.url);
        startActivity(intent);
    }

    private List<LevelInfo> loadMore() {
        return dbTool.getLevelinfoByPage(PAGE_SIZE, currentPageNum++);
    }

    private void openPinTu(LevelInfo info) {
        Intent intent = new Intent(activity, PingTuActivity.class);
        intent.putExtra("imageUrl_1", info.url);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            boolean isOpen = data.getBooleanExtra("isopen", false);
            if (isOpen){
                clickLevel.isOpen = 1;
                dbTool.changeLevelState(clickLevel.url);
                mAdapter.notifyItemChanged(clickPosition);
            }
        }
    }

    private void setupRecyclerView() {
        BezierLayout headerView = new BezierLayout(this);
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setWaveHeight(140);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOverScrollBottomShow(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addItems(dbTool.getLevelinfoByPage(PAGE_SIZE,currentPageNum++));
                        mAdapter.notifyDataSetChanged();
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });

    }

    long oldTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - oldTime < 3 * 1000) {
                finish();
            } else {
                ToastUtils.show("再按一次退出");
                oldTime = currentTime;
            }
        }
        return true;
    }

    @Override
    protected void initTitleBar() {
        titleBar.setRightTitle("相册");
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }
}
