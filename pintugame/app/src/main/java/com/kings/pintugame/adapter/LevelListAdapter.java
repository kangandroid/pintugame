package com.kings.pintugame.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kings.pintugame.R;
import com.kings.pintugame.adapter.base.BaseRecyclerAdapter;
import com.kings.pintugame.adapter.base.CommonHolder;
import com.kings.pintugame.modle.LevelInfo;
import com.kings.pintugame.utils.ImageUtils;

import butterknife.BindView;

/**
 * author：kk on 2016/12/17 00:37
 * email：kangkai@letoke.com
 */
public class LevelListAdapter extends BaseRecyclerAdapter<LevelInfo> {


    @Override
    public CommonHolder setViewHolder(ViewGroup parent) {
        return new CardHolder(parent.getContext(), parent);
    }


    class CardHolder extends CommonHolder<LevelInfo> {

        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.item_view)
        LinearLayout itemView;
        @BindView(R.id.iv_lock)
        ImageView ivLock;

        public CardHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.item_imglist);
        }

        @Override
        public void bindData(final LevelInfo info) {
            tvInfo.setText("第-" + info.levelNum + "-关");
            if (info.isOpen()) {
                ivLock.setVisibility(View.GONE);
                ImageUtils.displayImage(info.url, ivPic);
            } else {
                ImageUtils.displayGreyImage(info.url, ivPic);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        dataList.indexOf(info);
                        listener.OnClick(info,dataList.indexOf(info));
                    }
                }
            });
        }

    }

    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void OnClick(LevelInfo info,int posititon);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
