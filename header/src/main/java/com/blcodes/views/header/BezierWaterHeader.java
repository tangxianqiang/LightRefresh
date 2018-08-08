package com.blcodes.views.header;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.blcodes.views.refresh.header.BaseHeaderView;

public class BezierWaterHeader extends BaseHeaderView {
    public BezierWaterHeader(Context context) {
        this(context,null);
    }

    public BezierWaterHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierWaterHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void handleDrag(float dragY) {

    }

    @Override
    public boolean doRefresh() {
        return false;
    }

    @Override
    public void setParent(ViewGroup parent) {

    }

    @Override
    public boolean checkRefresh() {
        return false;
    }

    @Override
    public void refreshCompleted() {

    }

    @Override
    public int getHeaderHeight() {
        return 0;
    }

    @Override
    public void autoRefresh() {

    }
}
