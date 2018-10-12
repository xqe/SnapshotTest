package com.vivo.expose.model;

import android.text.TextUtils;

import com.vivo.expose.utils.HideVlog;

import java.io.Serializable;

/**
 * Created by 11070542 on 2018/1/17.
 * 曝光状态记录
 */

class ExposeStatus implements Serializable, Cloneable {
    private static final String TAG = "ExposeStatus";

    private static final long ONE_SECOND = 1000;

    /**
     * 曝光状态,是否曝光中
     */
    private boolean mExpStatus = false;

    /**
     * 曝光时长 = 曝光结束时间 - 曝光时间mExpTime
     */
    private long mExpDur;

    /**
     * 曝光时长拼接
     */
    private String mExpDurString;

    /**
     * 曝光次数
     */
    private int mExpCount = 0;

    /**
     * 上次开始曝光时间
     */
    private long mLastStartExpTime;

    /**
     * 上次结束曝光时间
     */
    private long mLastEndExpTime;


    /**
     * 是否允许开始曝光，禁止后这个item无法开始曝光
     */
    private boolean mCanExposeStart = true;

    public void setCanExposeStart(boolean canExposeStart) {
        this.mCanExposeStart = canExposeStart;
    }


    /**
     * 曝光次数
     *
     * @return
     */
    int getExpCount() {
        return mExpCount;
    }

    /**
     * 是否曝光时长小于50毫秒
     *
     * @return
     */
    boolean isExposeTooShortDelay() {
        if (mExpStatus) {
            return false;
        }
        return mExpDur > 0 && mExpDur < 50;
    }

    String getmExpDurString() {
        return mExpDurString;
    }

    /**
     * 除了界面可见/不可见和首页数据刷新时需要强制改变状态外,其他情况都应该判断当前状态和要设置的状态不相等才能设置
     */
    synchronized boolean setmExpStatus(boolean expStatus, String page, ExposeAppData exposeAppData) {
        if (expStatus && !mCanExposeStart) {
            //mCanExposeStart为false时不允许开始曝光
            HideVlog.d(TAG, "this item cannot expose start " + exposeAppData.getDebugDescribe());
            return false;
        }
        if (expStatus != mExpStatus) {

            long currentTime = System.currentTimeMillis();

            HideVlog.d(TAG, "setStatus" + expStatus + ":this=" + hashCode() + "start=" + mLastStartExpTime + ",end=" + mLastEndExpTime + ",current=" + currentTime);
            if (expStatus) {
                //由未曝光变成曝光，更新上次曝光时间为此刻

                boolean hasRevertExp = false;//是否撤销了上一次结束曝光
                if (currentTime - mLastEndExpTime < ONE_SECOND) {
                    //一结束曝光，就马上开始曝光，这种算持续还在曝光，则撤销上一次结束曝光
                    hasRevertExp = revertExpDurString();
                }
                if (!hasRevertExp) {
                    if (page != null) {
                        HideVlog.debugExpose("HidePromptlyReporterUtils", "exposeStart|" + page + "|" + exposeAppData.getDebugDescribe() + "|" + exposeAppData.hashCode());
                    }
                    mLastStartExpTime = System.currentTimeMillis();
                    mExpCount++;
                }

                if (exposeAppData.isCanSingleExpose()) {
                    ReporterConnectCallback callback = ReportType.getReporterConnectCallback();
                    if (callback != null) {
                        callback.reportSingleExposeData(exposeAppData, hasRevertExp);
                    }
                }
            } else {
                //由曝光变成非曝光，记录曝光时长
                long expDur = currentTime - mLastStartExpTime;
                mExpDur += expDur;
                if (TextUtils.isEmpty(mExpDurString)) {
                    mExpDurString = Long.toString(expDur);
                } else {
                    mExpDurString = mExpDurString + "," + expDur;
                }
                mLastEndExpTime = currentTime;
            }
            mExpStatus = expStatus;

            return true;
        } else {
            return false;
        }
    }

    /**
     * 撤销上一次的结束曝光
     *
     * @return 撤销
     */
    private boolean revertExpDurString() {
        if (TextUtils.isEmpty(mExpDurString)) {
            return false;
        }
        if (!mExpDurString.contains(",")) {
            mExpDurString = null;
        } else {
            mExpDurString = mExpDurString.substring(0, mExpDurString.lastIndexOf(","));
        }
        return true;
    }

    void resetExpCount() {
        mExpCount = 0;
        mExpDur = 0;
        mExpDurString = "";
    }

}
