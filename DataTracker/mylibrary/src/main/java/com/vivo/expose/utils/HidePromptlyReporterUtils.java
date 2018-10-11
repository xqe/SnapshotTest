package com.vivo.expose.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.vivo.expose.model.ExposeAppData;
import com.vivo.expose.model.ExposeItemInterface;
import com.vivo.expose.model.OnceExposeReporter;
import com.vivo.expose.model.ReportType;
import com.vivo.expose.model.ReporterConnectCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 11070542 on 2017/12/5.
 * 实时曝光上报工具
 */

public class HidePromptlyReporterUtils {

    private static final String TAG = "HidePromptlyReporterUtils";

    private static final ConcurrentHashMap<ReportType, HidePromptlyReporterUtils> REPORTERS = new ConcurrentHashMap<>();
    private static final String TAG_REC_EXP = "store_thread_rec_exp";

    //10秒
    private static final long TEN_SECOND = 10000;
    //1分钟
    private static final long ONE_MINUTE = 60000;

    private final List<ExposeAppData> mItemList = new ArrayList<>();
    //正在等待执行上报曝光
    private boolean mIsWaitingToExecuteReport = false;

    @NonNull
    private ReportType mReportType;


    /**
     * 调用此方法前，确认exposeItem的需要上报的曝光数据已绑定，如：
     * 1.pos 所在的banner的位置
     * 2.icpos 在banner中的位置
     * 3.object_id
     * 等等等等
     *
     * @param exposeItemInterface 要开始曝光的Item
     */
    static boolean exposeStart(ReportType type, @Nullable ExposeItemInterface exposeItemInterface, OnceExposeReporter onceExposeReporter) {
        if (exposeItemInterface == null || type == null) {
            return false;
        }
        ExposeAppData exposeAppData = exposeItemInterface.getExposeAppData();
        if (onceExposeReporter != null) {
            onceExposeReporter.attemptToOnceExpose(type.getOnceExposePage(), exposeAppData);
        }

        if (exposeAppData.setExpStatus(true, type.getPage(), exposeAppData)) {
            HidePromptlyReporterUtils.reportItem(exposeAppData, type, false);
            return true;
        }
        return false;
    }

    /**
     * 调用此方法前，确认exposeItem的需要上报的曝光数据已绑定，如：
     * 1.pos 所在的banner的位置
     * 2.icpos 在banner中的位置
     * 3.object_id
     * 等等等等
     *
     * @param exposeItemInterface 要结束曝光的Item
     */
    static void exposeEnd(ReportType type, @Nullable ExposeItemInterface exposeItemInterface) {
        if (exposeItemInterface == null || type == null) {
            return;
        }
        ExposeAppData exposeAppData = exposeItemInterface.getExposeAppData();
        if (exposeAppData.setExpStatus(false, type.getPage(), exposeAppData)) {
            HideVlog.debugExpose(TAG, "exposeEnd|" + type.getPage() + "|" + exposeAppData.getDebugDescribe() + "|" + exposeAppData.hashCode());
            HidePromptlyReporterUtils.reportItem(exposeAppData, type, false);
        }
    }


    /**
     * 上报Item,要曝光的数据put进ExposeAppData中
     *
     * @param exposeAppData 要曝光的Item
     * @param type          决定了埋点的page字段
     * @param immediately   是否立即上报
     */
    public static void reportItem(@Nullable ExposeAppData exposeAppData, ReportType type, boolean immediately) {
        if (type == null || TextUtils.isEmpty(type.getPage()) || type.getReporter() == null) {
            return;
        }

        synchronized (REPORTERS) {
            HidePromptlyReporterUtils reporter = REPORTERS.get(type);
            if (reporter == null) {
                reporter = new HidePromptlyReporterUtils(type);
                REPORTERS.put(type, reporter);
            }
            reporter.reportExpItem(exposeAppData, immediately);
        }

    }

    private HidePromptlyReporterUtils(@NonNull ReportType reportType) {
        this.mReportType = reportType;
    }

    /**
     * 上报一个exposeItem
     *
     * @param exposeAppData null表示处理待上报数据
     * @param immediately   马上上报
     */
    private void reportExpItem(@Nullable ExposeAppData exposeAppData, boolean immediately) {
        //        HideVlog.d(TAG, "reportExpItem：" + immediately);

        synchronized (mItemList) {

            if (exposeAppData != null) {

                if (!mItemList.contains(exposeAppData)) {
                    mItemList.add(exposeAppData);
                }

                //纯实时曝光 或 未在等待执行曝光 或 exposeItemInterface==null才能继续执行
                if (!immediately && mIsWaitingToExecuteReport) {
                    return;
                }

            } else if (!immediately || mItemList.size() == 0) {
                //exposeItemInterface==null && 不马上上报，则没必要继续执行
                return;
            }

            mIsWaitingToExecuteReport = true;

        }
        HideVThread.getInstance().run(new Runnable() {
            @Override
            public void run() {

                JSONArray appExpData = new JSONArray();
                synchronized (mItemList) {
                    if (mItemList.size() == 0) {
                        mIsWaitingToExecuteReport = false;
                        return;
                    }
                    for (ExposeAppData item : mItemList) {
                        if (mReportType.isFilterShortExpose() && item.isExposeTooShortTime()) {
                            continue;
                        }
                        int count;
                        if ((count = item.getExposeCount()) > 0) {
                            HideVlog.debugExpose(TAG, "exposeReport|" + mReportType.getPage() + "|" + count + "|" + item.getDebugDescribe() + "|" + item.hashCode());
                        }
                        appExpData.put(item.toJsonObject(true));
                        item.resetExpCount();
                    }
                    mItemList.clear();
                    mIsWaitingToExecuteReport = false;
                }

                if (appExpData.length() == 0) {
                    return;
                }

                JSONObject json = new JSONObject();
                try {
                    json.put(getSpecialDate(System.currentTimeMillis()), appExpData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HideVlog.d(TAG, "reportStatData json  " + json.toString());

                ReporterConnectCallback callback = mReportType.getReporter();
                if (callback != null) {
                    callback.reportExposeData(mReportType, json.toString());
                }
            }
        }, TAG_REC_EXP, immediately ? 0 : HideVlog.sDebugSDK ? TEN_SECOND : ONE_MINUTE);
    }


    @SuppressLint("SimpleDateFormat")
    public static String getSpecialDate(long timeMills) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeMills);
        return sdf.format(date);
    }
}
