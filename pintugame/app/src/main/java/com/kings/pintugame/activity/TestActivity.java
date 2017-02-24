package com.kings.pintugame.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.kings.pintugame.Constant;
import com.kings.pintugame.R;
import com.kings.pintugame.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {


    @BindView(R.id.my)
    ScrollView my;
    private String urlFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initTitleBar() {
        titleBar.setCenterTitle("测试");
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
        return R.layout.activity_test;
    }


    protected void initView() {

        Log.e("kk", "-----------" + my.getChildAt(0) );
//        TextView textView = new TextView(activity);
//        textView.setText("i am a content");
//        textView.setGravity(Gravity.CENTER);
//        my.addContentView(textView,new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                1500));
//        TextView header = new TextView(activity);
//        header.setText("i am a header");
//        header.setGravity(Gravity.CENTER);
//        my.addHeaderView(header,new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                200));
//        TextView footer = new TextView(activity);
//        footer.setText("i am a footer");
//        footer.setGravity(Gravity.CENTER);
//        my.addFooterView(footer, new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                200));
//        generateUrls();
//        MmImagesAdapter adapter = new MmImagesAdapter(mmImageUrlList);
//        mPtrrv.setAdapter(adapter);

    }

    private int girlNum = Constant.MM_MIN;

    private List<String> mmImageUrlList = new ArrayList<>();

    private void generateUrls() {
        for (int i = 0; i < 30; i++) {
            String url = urlFirst.replace(Constant.PAGE_NUM, girlNum + "");
            mmImageUrlList.add(url);
            girlNum++;
        }
    }
}
