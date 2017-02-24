package com.kings.pintugame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * author：kk on 2017/2/9 17:17
 * email：kangkai@letoke.com
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    public DividerItemDecoration(Context context) {
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        boolean didStructureChange = state.didStructureChange();
        int childCount = parent.getChildCount();
    }

}
