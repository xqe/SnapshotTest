package com.vivo.expose.view;

import com.vivo.expose.model.ExposeItemInterface;
import com.vivo.expose.model.ReportType;

/**
 * Created by 11070542 on 2017/12/20.
 * 曝光区
 */

public interface ExposableLayoutInterface {
    void bindExposeItemList(ReportType reportType, ExposeItemInterface... items);

    void setCanDeepExpose();
}
