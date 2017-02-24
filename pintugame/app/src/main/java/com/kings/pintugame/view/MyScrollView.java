package com.kings.pintugame.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
    // 拖动的距离 size = 4 的意思 只允许拖动屏幕的1/4
    private static final int size = 4;
    private View mContentView;

    private Context mContext;

    int mHeaderHeight;
    int mFooterHeight;
    private float y;
    private Rect normal = new Rect();

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentView == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                /**
                 * size=4 表示 拖动的距离为屏幕的高度的1/4
                 */
                int deltaY = (int) (preY - nowY) / size;
                // 滚动
                // scrollBy(0, deltaY);

                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(mContentView.getLeft(), mContentView.getTop(),
                                mContentView.getRight(), mContentView.getBottom());
                        return;
                    }
                    int yy = mContentView.getTop() - deltaY;

                    // 移动布局
                    mContentView.layout(mContentView.getLeft(), yy, mContentView.getRight(),
                            mContentView.getBottom() - deltaY);
                }
                break;
            default:
                break;
        }
    }

    // 开启动画移动
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, mContentView.getTop(),
                normal.top);
        ta.setDuration(200);
        mContentView.startAnimation(ta);
        // 设置回到正常的布局位置
        mContentView.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount()>0){
            mContentView = getChildAt(0);
        }
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        int offset = mContentView.getMeasuredHeight() - getHeight();
        Log.e("kk", "-----------offset" + offset);
        int scrollY = getScrollY();
        Log.e("kk", "-----------scrollY" + scrollY);
        return scrollY == 0 || scrollY == offset;
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return rect.bottom;
    }


    private OnScrollChangeListener listener;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.OnScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setListener(OnScrollChangeListener listener) {
        this.listener = listener;
    }

    public interface OnScrollChangeListener {
        void OnScrollChanged(int l, int t, int oldl, int oldt);
    }


}
