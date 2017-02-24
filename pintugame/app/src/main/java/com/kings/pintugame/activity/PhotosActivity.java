package com.kings.pintugame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kings.pintugame.R;
import com.kings.pintugame.base.BaseActivity;
import com.kings.pintugame.utils.ImageUtils;
import com.kings.pintugame.view.JazzyViewPager;
import com.kings.pintugame.view.PhotoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosActivity extends BaseActivity {

    private static final int DATA_CHECK_OVER = 222;
    @BindView(R.id.vp_photos)
    JazzyViewPager vpPhotos;
    private List<String> images = new ArrayList<>();
    private MyPhotoPagerAdapter adapter;
    private int curPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void initTitleBar() {
        getImageUrls();
        titleBar.setRightTitle("拼此图");
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PingTuActivity.class);
                intent.putExtra("imageUrl_1", images.get(curPosition));
                startActivity(intent);
            }
        });
        titleBar.setCenterTitle((curPosition + 1) + "/" + images.size());
        titleBar.setLeftIconRes(R.mipmap.ic_back);
        titleBar.setLeftAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        vpPhotos.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        vpPhotos.setTransitionEffect(JazzyViewPager.TransitionEffect.CubeIn);
        adapter = new MyPhotoPagerAdapter();
        vpPhotos.setAdapter(adapter);
        vpPhotos.setCurrentItem(curPosition);
        vpPhotos.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
                titleBar.setCenterTitle((curPosition + 1) + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_photos;
    }

    public void getImageUrls() {
        final String imageUrl_1 = getIntent().getStringExtra("imageUrl_1");
        images.add(imageUrl_1);
        String urlhead = imageUrl_1.substring(0, imageUrl_1.lastIndexOf("/") + 1);
        String urlend = imageUrl_1.substring(imageUrl_1.lastIndexOf("."));
        int maxPicNumber = getIntent().getIntExtra("max_pic_number", 1);
        for (int i = 2; i <=maxPicNumber ; i++) {
            String url = urlhead + i + urlend;
            images.add(url);
        }
    }


    class MyPhotoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView ivPhoto = new PhotoView(PhotosActivity.this);
            ivPhoto.enable();
            ivPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageUtils.displayImage(images.get(position), ivPhoto);
            container.addView(ivPhoto);
            vpPhotos.setObjectForPosition(ivPhoto, position);
            return ivPhoto;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
