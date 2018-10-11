package com.vivo.expose.model;

/**
 * Created by 11070542 on 2017/12/15.
 * 曝光定制需求
 */

public class PromptlyOption implements PromptlyOptionInterface {

    //100%显示算曝光
    public static final int PERCENT_FULL = 100;
    //50%显示算曝光
    public static final int PERCENT_HALF = 50;

    /**
     * 露出百分之多少视为显示，取值范围(0,100]，默认50%
     */
    private int mVisiblePercentToBeExpose = PERCENT_HALF;

    /**
     * 仅开始曝光，不结束曝光，默认要结束曝光并记录曝光时长与曝光次数
     */
    private boolean mDisableExposeEnd = false;

    public PromptlyOption() {

    }

    /**
     * 禁止结束曝光，仅开始曝光(目前仅小程序banner有这定制需求)
     * 即不重复曝光
     */
    public PromptlyOption disableExposeEnd() {
        mDisableExposeEnd = true;
        return this;
    }

    /**
     * 露出百分之多少视为显示，取值范围(0,100]
     */
    public PromptlyOption setVisiblePercentToDoExpose(int percent) {
        mVisiblePercentToBeExpose = percent;
        return this;
    }

    public int getVisiblePercentToDoExpose() {
        return mVisiblePercentToBeExpose;
    }

    public boolean isDisableExposeEnd() {
        return mDisableExposeEnd;
    }
}
