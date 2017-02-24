package com.kings.pintugame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kings.pintugame.R;


/**
 * author：kk on 2016/11/24 10:46
 * email：kangkai@letoke.com
 */
public class CustomTitleBar extends FrameLayout {
    ImageView iconLeft;
    TextView titleLeft;
    LinearLayout layoutLeft;
    TextView titleCenter;
    LinearLayout layoutCenter;
    ImageView iconCenter;
    TextView titleRight;
    ImageView iconRight;
    LinearLayout layoutRight;
    View fakeStateBar;
    LinearLayout titleLayout;


    private String centerTitle;
    private String leftTitle;
    private String rightTitle;
    private int textColor;
    private int backgroudColor;

    private int stateBarHeight;//px.
    @DrawableRes
    private int leftIconRes;
    @DrawableRes
    private int rightIconRes;
    @DrawableRes
    private int centerIconRes;
    private View dividerView;
    private int dividerColor;

    public CustomTitleBar(Context context) {
        super(context);
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_titlebar, this, true);
        //左边
        iconLeft = (ImageView) view.findViewById(R.id.icon_left);
        titleLeft = (TextView) view.findViewById(R.id.title_left);
        dividerView =  view.findViewById(R.id.divider);
        layoutLeft = (LinearLayout) view.findViewById(R.id.layout_left);
        //中间
        iconCenter = (ImageView) view.findViewById(R.id.icon_center);
        titleCenter = (TextView) view.findViewById(R.id.title_center);
        layoutCenter = (LinearLayout) view.findViewById(R.id.layout_center);
        //右边
        iconRight = (ImageView) view.findViewById(R.id.icon_right);
        titleRight = (TextView) view.findViewById(R.id.title_right);
        layoutRight = (LinearLayout) view.findViewById(R.id.layout_right);

        fakeStateBar = view.findViewById(R.id.fake_state_bar);
        titleLayout = (LinearLayout) view.findViewById(R.id.title_layout);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomTitleBar, 0, 0);
        try {
            textColor = ta.getColor(R.styleable.CustomTitleBar_textColor, Color.WHITE);
            backgroudColor = ta.getColor(R.styleable.CustomTitleBar_backGroundColor, Color.BLUE);
            dividerColor = ta.getColor(R.styleable.CustomTitleBar_dividerColor, Color.parseColor("#e4e4e4"));
            centerTitle = ta.getString(R.styleable.CustomTitleBar_titleCenter);
            leftTitle = ta.getString(R.styleable.CustomTitleBar_titleLeft);
            rightTitle = ta.getString(R.styleable.CustomTitleBar_titleRight);
            leftIconRes = ta.getResourceId(R.styleable.CustomTitleBar_iconLeft, 0);
            centerIconRes = ta.getResourceId(R.styleable.CustomTitleBar_iconCenter, 0);
            rightIconRes = ta.getResourceId(R.styleable.CustomTitleBar_iconRight, 0);
            stateBarHeight = ta.getResourceId(R.styleable.CustomTitleBar_stateBarHeight, 0);
        } finally {
            iconLeft.setImageResource(leftIconRes);
            iconCenter.setImageResource(centerIconRes);
            iconRight.setImageResource(rightIconRes);
            titleCenter.setText(centerTitle);
            titleLeft.setText(leftTitle);
            titleRight.setText(rightTitle);
            titleLayout.setBackgroundColor(backgroudColor);
            dividerView.setBackgroundColor(dividerColor);
            setTextColor(textColor);
            ViewGroup.LayoutParams params = fakeStateBar.getLayoutParams();
            params.height = stateBarHeight;
            fakeStateBar.setLayoutParams(params);
            ta.recycle();
        }
    }

    public String getCenterTitle() {
        return centerTitle;
    }

    public void setCenterTitle(String centerTitle) {
        this.centerTitle = centerTitle;
        titleCenter.setText(centerTitle);
    }

    public String getLeftTitle() {
        return leftTitle;
    }

    public void setLeftTitle(String leftTitle) {
        titleLeft.setText(leftTitle);
        this.leftTitle = leftTitle;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    public void setRightTitle(String rightTitle) {
        titleRight.setText(rightTitle);
        this.rightTitle = rightTitle;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        titleLeft.setTextColor(textColor);
        titleCenter.setTextColor(textColor);
        titleRight.setTextColor(textColor);
        this.textColor = textColor;
    }

    public int getBackgroudColor() {
        return backgroudColor;
    }

    public void setBackgroudColor(int backgroudColor) {

        this.backgroudColor = backgroudColor;
    }

    public int getStateBarHeight() {
        return stateBarHeight;
    }

    public void setStateBarHeight(int stateBarHeight) {
        ViewGroup.LayoutParams params = fakeStateBar.getLayoutParams();
        params.height = stateBarHeight;
        fakeStateBar.setLayoutParams(params);
        this.stateBarHeight = stateBarHeight;
    }

    public int getLeftIconRes() {
        return leftIconRes;
    }

    public void setLeftIconRes(int leftIconRes) {
        iconLeft.setImageResource(leftIconRes);
        this.leftIconRes = leftIconRes;
    }

    public int getRightIconRes() {
        return rightIconRes;
    }

    public void setRightIconRes(int rightIconRes) {
        iconRight.setImageResource(rightIconRes);
        this.rightIconRes = rightIconRes;
    }

    public void setLeftAction(OnClickListener leftAction){
        layoutLeft.setOnClickListener(leftAction);
    }

    public void setRightAction(OnClickListener rightAction) {
        layoutRight.setOnClickListener(rightAction);
    }

    public void setCenterAction(OnClickListener centerAction) {
        layoutCenter.setOnClickListener(centerAction);
    }

}
