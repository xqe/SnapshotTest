package com.vivo.expose.root;

/**
 * Created by 11070542 on 2018/4/8.
 */

public class RootViewOption implements RootViewOptionInterface {

    private int mExposeMarginLeft = 0;
    private int mExposeMarginRight = 0;
    private int mExposeMarginTop = 0;
    private int mExposeMarginBottom = 0;

    public RootViewOption() {

    }

    public RootViewOption(int marginLeft, int marginRight, int marginTop, int marginBottom) {
        mExposeMarginLeft = marginLeft;
        mExposeMarginRight = marginRight;
        mExposeMarginTop = marginTop;
        mExposeMarginBottom = marginBottom;
    }


    @Override
    public int getExposeMarginLeft() {
        return mExposeMarginLeft;
    }

    public RootViewOption setExposeMarginLeft(int mExposeMarginLeft) {
        this.mExposeMarginLeft = mExposeMarginLeft;
        return this;
    }

    @Override
    public int getExposeMarginRight() {
        return mExposeMarginRight;
    }

    public RootViewOption setExposeMarginRight(int mExposeMarginRight) {
        this.mExposeMarginRight = mExposeMarginRight;
        return this;
    }

    @Override
    public int getExposeMarginTop() {
        return mExposeMarginTop;
    }

    public RootViewOption setExposeMarginTop(int mExposeMarginTop) {
        this.mExposeMarginTop = mExposeMarginTop;
        return this;
    }

    @Override
    public int getExposeMarginBottom() {
        return mExposeMarginBottom;
    }

    public RootViewOption setExposeMarginBottom(int mExposeMarginBottom) {
        this.mExposeMarginBottom = mExposeMarginBottom;
        return this;
    }
}
