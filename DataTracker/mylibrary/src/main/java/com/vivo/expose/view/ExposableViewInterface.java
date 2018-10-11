package com.vivo.expose.view;

import android.support.annotation.Nullable;

import com.vivo.expose.model.PromptlyOptionInterface;
import com.vivo.expose.PromptlyReporterCenter;
import com.vivo.expose.model.ExposeItemInterface;
import com.vivo.expose.model.ReportType;

/**
 * Created by 11070542 on 2017/11/28.
 * 需要曝光的View实现本接口
 */

public interface ExposableViewInterface {


    /**
     * 曝光定制化需求
     *
     * @return 曝光定制化需求，无定制化则可直接return null
     * @see PromptlyReporterCenter#PROMPTLY_OPTION_DEFAULT
     * @see PromptlyReporterCenter#PROMPTLY_OPTION_NO_EXPOSE_END
     */
    @Nullable
    PromptlyOptionInterface getPromptlyOption();


    /**
     * 要曝光的item
     *
     * @return new Item[]{mItem}这种
     */
    ExposeItemInterface[] getItemsToDoExpose();

    /**
     * 哪种type
     *
     * @return 从PromptlyReporterCenter中去取
     */
    ReportType getReportType();


    /**
     * 是否能继续往子View遍历曝光
     *
     * @return 一般为false，如果子View里面还有ExposableViewInterface，就true
     */
    boolean canDeepExpose();

}
