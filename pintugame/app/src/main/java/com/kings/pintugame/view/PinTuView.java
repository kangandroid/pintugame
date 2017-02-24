package com.kings.pintugame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.kings.pintugame.animator.Techniques;
import com.kings.pintugame.animator.YoYo;
import com.nineoldandroids.animation.Animator;

/**
 * author：kk on 2017/1/12 14:39
 * email：kangkai@letoke.com
 */
public class PinTuView extends GridLayout {
    private int widthsize;
    private int heightsize;
    private Bitmap mBitmap;
    private int mRowCount = 1, mColumnCount = 1;
    private PieceView[][] mChildren;
    private Context mContext;
    private PieceView mBlankPiece;
    private boolean isBimapseted = false;
    private boolean hasMeasured = false;
    private int pieceWidth;
    private int pieceHeight;
    int stepCount = 0;

    public PinTuView(Context context) {
        this(context, null);
    }

    public PinTuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinTuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(@NonNull int rowCount, @NonNull int columnCount) {
        mRowCount = rowCount;
        mColumnCount = columnCount;
        setColumnCount(mColumnCount);
        setRowCount(mRowCount);
        initView();

    }

    public void setBitmap(@NonNull Bitmap bitmap) {
        mBitmap = bitmap;
        if (hasMeasured && !isBimapseted) {
            mBitmap = trimBitmap(mBitmap);
            splitBitmap();
        }
    }

    private void initView() {
        mBlankPiece = new PieceView(mContext);
        mBlankPiece.setBackgroundColor(Color.WHITE);
        mChildren = new PieceView[mRowCount][mColumnCount];
        for (int i = 0; i < mRowCount; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                mChildren[i][j] = mBlankPiece.clone();
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(1, 1, 1, 1);
                addView(mChildren[i][j], params);
                mChildren[i][j].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getMoveType((PieceView) v) == MoveType.CANT_MOVE) return;
                        movePiece((PieceView) v);
                    }
                });
            }
        }
    }

    /**
     * 切割图片并分配到每一个单元
     */
    private void splitBitmap() {
        for (int i = 0; i < mRowCount; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                Bitmap pieceBitmap = Bitmap.createBitmap(mBitmap,
                        j * pieceWidth + 1, i * pieceHeight + 1, pieceWidth - 2, pieceHeight - 2);
                mChildren[i][j].setImageBitmap(pieceBitmap);
                PieceInfo pieceInfo = new PieceInfo(pieceBitmap, j, i);
                mChildren[i][j].setTag(pieceInfo);
            }
        }
        setBlankPiece(mChildren[mRowCount / 2][mColumnCount / 2]);
        randomMove(mRowCount * mColumnCount * 10);
        isMoving = false;
    }

    public void setBlankPiece(PieceView imageView) {
        mBlankPiece = imageView;
        mBlankPiece.setImageBitmap(null);
    }

    private MoveType getMoveType(PieceView imageView) {
        PieceInfo pieceInfo = (PieceInfo) imageView.getTag();
        PieceInfo blankPieceInfo = (PieceInfo) mBlankPiece.getTag();
        if (pieceInfo.rowNum == blankPieceInfo.rowNum &&
                pieceInfo.columnNum - blankPieceInfo.columnNum == 1) {
            return MoveType.LEFT;
        } else if (pieceInfo.rowNum == blankPieceInfo.rowNum &&
                pieceInfo.columnNum - blankPieceInfo.columnNum == -1) {
            return MoveType.RIGHR;
        } else if (pieceInfo.columnNum == blankPieceInfo.columnNum &&
                pieceInfo.rowNum - blankPieceInfo.rowNum == 1) {
            return MoveType.UP;
        } else if (pieceInfo.columnNum == blankPieceInfo.columnNum &&
                pieceInfo.rowNum - blankPieceInfo.rowNum == -1) {
            return MoveType.DOWN;
        } else {
            return MoveType.CANT_MOVE;
        }
    }

    private void showBlankPiece() {
        final PieceInfo tag = (PieceInfo) mBlankPiece.getTag();
        YoYo.with(Techniques.FlipInY)
                .duration(1200)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mBlankPiece.setImageBitmap(tag.bitmap);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (null != mListener) {
                            mListener.onSuccess(stepCount);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .playOn(mBlankPiece);

    }

    /**
     * 随机移动单元打乱顺序
     *
     * @param times 移动的次数
     */
    public void randomMove(int times) {
        for (int i = 0; i < times; i++) {
            PieceInfo blankPieceInfo = (PieceInfo) mBlankPiece.getTag();
            int columnNum = blankPieceInfo.columnNum;
            int rowNum = blankPieceInfo.rowNum;
            int floor = (int) Math.floor(Math.random() * 4);
            switch (floor) {
                case 0:
                    columnNum++;
                    if (columnNum > mColumnCount - 1) columnNum -= 2;
                    break;
                case 1:
                    columnNum--;
                    if (columnNum < 0) columnNum += 2;
                    break;
                case 2:
                    rowNum++;
                    if (rowNum > mRowCount - 1) rowNum -= 2;
                    break;
                case 3:
                    rowNum--;
                    if (rowNum < 0) rowNum += 2;
                    break;
            }
            exchangeBitmapInfo(mChildren[rowNum][columnNum]);
            stepCount = 0;
        }

    }

    /**
     * 判断拼图是否成功，成功为TRUE
     *
     * @return
     */
    private boolean isComplete() {
        for (int i = 0; i < mRowCount; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                PieceInfo tag = (PieceInfo) mChildren[i][j].getTag();
                if (tag.bmX == tag.columnNum && tag.bmY == tag.rowNum) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    enum MoveType {
        UP, DOWN, RIGHR, LEFT, CANT_MOVE
    }


    private boolean isMoving = true;

    private void movePiece(final PieceView imageView) {
        if (isMoving) return;
        isMoving = true;
        float toX = 0, toY = 0;
        switch (getMoveType(imageView)) {
            case UP:
                toY = -1;
                break;
            case DOWN:
                toY = 1;
                break;
            case RIGHR:
                toX = 1;
                break;
            case LEFT:
                toX = -1;
                break;
            case CANT_MOVE:
                return;
        }
        TranslateAnimation animation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, toX,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, toY);
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                exchangeBitmapInfo(imageView);
                stepCount++;
                if (null != mListener) {
                    mListener.onMove(stepCount);
                }
                if (isComplete()) {
                    showBlankPiece();
                } else {
                    isMoving = false;

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);

    }




    /**
     * 与空白的单元交换数据
     *
     * @param imageView
     */
    private void exchangeBitmapInfo(PieceView imageView) {
        PieceInfo pieceInfo = (PieceInfo) imageView.getTag();
        PieceInfo blankPieceInfo = (PieceInfo) mBlankPiece.getTag();
        mBlankPiece.setImageBitmap(pieceInfo.bitmap);
        Bitmap bitmap = blankPieceInfo.bitmap;
        blankPieceInfo.bitmap = pieceInfo.bitmap;
        pieceInfo.bitmap = bitmap;
        int x = blankPieceInfo.bmX;
        blankPieceInfo.bmX = pieceInfo.bmX;
        pieceInfo.bmX = x;
        int y = blankPieceInfo.bmY;
        blankPieceInfo.bmY = pieceInfo.bmY;
        pieceInfo.bmY = y;
        setBlankPiece(imageView);
    }

    /**
     * 获取手势
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    private MoveType getGesture(float startX, float startY, float endX, float endY) {
        float deltaX = Math.abs(endX - startX);
        float deltaY = Math.abs(endY - startY);
        if (deltaX > deltaY) {
            if (endX > startX) {
                return MoveType.RIGHR;
            } else {
                return MoveType.LEFT;
            }
        } else {
            if (endY > startY) {
                return MoveType.DOWN;
            } else {
                return MoveType.UP;
            }
        }
    }


    private Bitmap trimBitmap(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        if (width / mColumnCount > height / mRowCount) {
            src = Bitmap.createBitmap(src,
                    (width - mColumnCount * height / mRowCount) / 2,
                    0, mColumnCount * height / mRowCount,
                    height);
        } else {
            src = Bitmap.createBitmap(src, 0,
                    (height - mRowCount * width / mColumnCount) / 2,
                    width, mRowCount * width / mColumnCount);
        }
        return Bitmap.createScaledBitmap(src, widthsize, heightsize, false);
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        widthsize = getSize(widthSpec);
        heightsize = getSize(heightSpec);
        pieceWidth = widthsize / mColumnCount;
        pieceHeight = heightsize / mRowCount;
        widthsize = pieceWidth * mColumnCount;
        heightsize = pieceHeight * mRowCount;
        setMeasuredDimension(widthsize, heightsize);

    }


    private int getSize(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {//测量类型：精确数值
            return MeasureSpec.getSize(measureSpec);
        } else {//测量类型：未指定
            return 100;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        hasMeasured = true;
        if (mBitmap != null) {
            mBitmap = trimBitmap(mBitmap);
            splitBitmap();
            isBimapseted = true;
        }

    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Bundle bundle = state instanceof Bundle ? ((Bundle) state) : null;
    }

    class PieceView extends ImageView implements Cloneable {

        public PieceView(Context context) {
            super(context);
        }

        @Override
        public PieceView clone() {
            PieceView pieceView = null;
            try {
                pieceView = (PieceView) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return pieceView;
        }
    }

    class PieceInfo {
        int rowNum;
        int columnNum;
        Bitmap bitmap;
        int bmX;
        int bmY;

        public PieceInfo(Bitmap bitmap, int columnNum, int rowNum) {
            this.bitmap = bitmap;
            this.columnNum = columnNum;
            this.rowNum = rowNum;
            bmX = columnNum;
            bmY = rowNum;
        }
    }




    public void setOnMoveListener(OnMoveListener listener) {
        this.mListener = listener;
    }

    OnMoveListener mListener;

    public interface OnMoveListener {
        void onSuccess(int stepCount);
        void onMove(int stepCount);

    }

}
