package com.kings.pintugame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * author：kk on 2017/2/10 15:05
 * email：kangkai@letoke.com
 */
public class MyViewGroupImp extends ViewGroup {

    private Context mContext;

    public MyViewGroupImp(Context context) {
        this(context,null);
    }

    public MyViewGroupImp(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public MyViewGroupImp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }
// AbsoluteLayout,
// AdapterView<T extends Adapter>,
// CoordinatorLayout,
// DrawerLayout,
// FragmentBreadCrumbs,
// FrameLayout,
// GridLayout,
// LinearLayout,
// LinearLayoutCompat,
// PagerTitleStrip,
// RecyclerView,
// RelativeLayout,
// ShadowOverlayContainer,
// SlidingDrawer,
// SlidingPaneLayout,
// SwipeRefreshLayout,
// Toolbar, TvView, ViewPager
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
