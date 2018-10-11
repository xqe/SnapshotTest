package com.vivo.expose.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vivo.expose.model.ExposeItemInterface;
import com.vivo.expose.model.PromptlyOption;
import com.vivo.expose.model.ReportType;

/**
 * Created by 11070542 on 2017/12/7.
 * 可监听Item曝光的LinearLayout
 */

public class ExposableLinearLayout extends LinearLayout implements ExposableViewInterface, ExposableLayoutInterface {

    @NonNull
    private ExposeItemInterface[] mItemDatas = new ExposeItemInterface[0];

    private ReportType mReportType;

    private boolean mCanDeepExpose = false;

    public ExposableLinearLayout(Context context) {
        super(context);
    }

    public ExposableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExposableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public PromptlyOption getPromptlyOption() {
        return null;
    }

    @Override
    public ExposeItemInterface[] getItemsToDoExpose() {
        return mItemDatas;
    }

    @Override
    public ReportType getReportType() {
        return mReportType;
    }

    @Override
    public boolean canDeepExpose() {
        return mCanDeepExpose;
    }


    /**
     * 绑定实时曝光的items
     *
     * @param reportType 上报的类型，HidePromptlyReporterUtils.TYPE_XXX;-1表示不检测曝光
     * @param items      要绑定的items
     */
    @Override
    public void bindExposeItemList(ReportType reportType, ExposeItemInterface... items) {
        mReportType = reportType;
        mItemDatas = items == null ? new ExposeItemInterface[0] : items;
    }

    @Override
    public void setCanDeepExpose() {
        mCanDeepExpose = true;
    }

}
