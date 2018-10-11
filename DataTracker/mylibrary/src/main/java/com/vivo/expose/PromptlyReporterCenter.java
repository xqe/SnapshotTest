package com.vivo.expose;

import android.view.View;

import com.vivo.expose.model.PromptlyOption;
import com.vivo.expose.model.PromptlyOptionInterface;
import com.vivo.expose.utils.HideExposeUtils;
import com.vivo.expose.utils.HideVlog;

/**
 * Created by 11070542 on 2017/12/15.
 * 实时曝光用到，该类中放置新增的埋点类型
 * <p>
 * 更新日志：
 * v1.1
 * 2018.3.27 修复Item的equal被重写导致的缺曝光bug
 * v1.2
 * 2018.3.28 新增立即上报所有item的支持
 * v1.3
 * 2018.3.29 修复RecyclerView未露出50%有可能也算曝光的一个问题
 * v1.5
 * 迁移为maven
 * v1.6
 * 2018.4.8 支持定制滚动容器的曝光区域RootViewOption
 * v1.7.1
 * 2018.4.19 减少getAppExposeData()调用次数
 * v1.7.3
 * 2018.5.31 定制滚动容器的曝光区域支持使用接口RootViewOptionInterface
 * v1.7.4
 * 2018.6.19 局部刷新的一种特殊场景处理，判断RootViewInterface的isEnable再决定是否尝试开始曝光；HideExposeUtils的三个重载方法压缩成一个方法
 * v1.7.5
 * 2018.6.20 局部刷新的结束曝光或尝试开始曝光可传入View，不必要求ViewGroup
 * v1.8.0
 * 2018.7.10 新增一种新需求：可能需要在埋点外层加参数（page同级，不是json里面），所以ReportType加入了一个HashMap成员变量
 * v1.8.1
 * 2018.7.10 enableDebug()后10秒上报变成enableMasterDebug()后10秒上报，enableDebug()后依然使用60秒上报
 * v1.8.2
 * 2018.7.12 支持继续遍历ExposableViewInterface的子View，通过setCanDeepExpose()方法允许遍历其子View
 * v1.8.3
 * 2018.7.24 新增ExposableTextView;PromptlyReporterCenter.attemptToExposeStart()支持传入滚动容器；ReportType允许putExtra
 * v1.8.5
 * 2018.8.14 可设置ReportType过滤闪现曝光（50毫秒以内）；默认不过滤
 * v1.8.6
 * 2018.8.15 新增方法PromptlyReporterCenter.disableItemsExposeInView(),可阻止View中所有子View绑定的item开始曝光
 * v1.8.8
 * 2018.9.4 新增支持展示曝光 ReportType.setOnceExposePage()、展示曝光会对pos、icpos减1再上报
 * v1.8.9
 * 2018.9.17 支持非聚合上报实时曝光的开始曝光；ScrollView支持PromptlyReporterCenter.attemptToExposeStart()；ExposeAppData支持绑定自定义数据
 */

public class PromptlyReporterCenter {

    //默认的实时曝光要求，无特殊定制
    public static final PromptlyOptionInterface PROMPTLY_OPTION_DEFAULT = new PromptlyOption();
    //小程序Banner的曝光定制选项，不重复曝光
    public static final PromptlyOptionInterface PROMPTLY_OPTION_NO_EXPOSE_END = new PromptlyOption().setVisiblePercentToDoExpose(PromptlyOption.PERCENT_FULL).disableExposeEnd();

    public static void enableMasterDebug() {
        HideVlog.sDebugSDK = true;
    }

    /**
     * 开启Logcat检测曝光缺失率
     * 默认不开启
     */
    public static void enableDebug() {
        HideVlog.sDebugExpose = true;
    }

    /**
     * 强制结束曝光ListView中的一个ViewGroup
     * 用于在横向可横滑控件中recycler时调用
     *
     * @param view listView中的View
     */
    public static void attemptToExposeEnd(View view) {
        HideExposeUtils.attemptToExposeEnd(view);
    }

    /**
     * 尝试性曝光ListView中的一个ViewGroup
     * 用于在横向可横滑控件中滑动停止时调用
     *
     * @param subViewInListView listView中的View
     */
    public static void attemptToExposeStart(View subViewInListView) {
        HideExposeUtils.attemptToExposeStartSubView(subViewInListView);
    }

    /**
     * 尝试性曝光ListView中的一个ViewGroup
     * 用于在横向可横滑控件中滑动停止时调用
     *
     * @param subViewInListView listView中的View
     */
    public static void attemptToExposeStartAfterLayout(View subViewInListView) {
        HideExposeUtils.attemptToExposeStartAfterLayout(subViewInListView);
    }

    /**
     * 强制开始曝光一个ViewGroup
     *
     * @param subViewInListView listView中的View
     */
    public static void attemptToExposeStartForce(View subViewInListView) {
        HideExposeUtils.attemptToExposeStartForce(subViewInListView);
    }

    public static void disableItemsExposeInView(View view) {
        HideExposeUtils.setItemsCanExposeStart(view, false);
    }

}
