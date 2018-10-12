package com.vivo.expose.model;

import android.text.TextUtils;

import com.vivo.expose.utils.HideVlog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 11070542 on 2017/12/6.
 * 与Item唯一绑定
 */

public final class ExposeAppData implements Serializable {

    private static final String TAG = "ExposeAppData";

    private static final String SOURCE = "source";
    private static final String POS = "pos";
    private static final String IC_POS = "icpos";

    private static final String ID = "id";
    private static final String AID = "aid";
    private static final String CP = "cp";
    private static final String CT = "ct";
    private static final String CPD_PS = "cpdps";
    private static final String OBJECT_ID = "object_id";
    private static final String TYPE = "type";
    private static final String STYLE = "style";
    private static final String LIST_POS = "listpos";
    private static final String C_FROM = "cfrom";
    private static final String REQ_ID = "req_id";
    private static final String MODULE_ID = "module_id";
    private static final String RELATE_ID = "relatedID";
    private static final String UPDATE = "update";
    private static final String Dt = "dt";
    private static final String KEY_STATUS = "kst";

    private static final String COUNT = "count";
    private static final String DUR = "dur";
    private static final String GAME_REC_ID = "recom_reqid";

    /**
     * 这里的一堆全是埋点设计表中需要的，至于在哪putXXX,根据数据来源定。
     * 如：
     * Item重写了getExposeAppData,设置Pos、icpos;
     * Packagefile或Adv都有对应重写getExposeAppData
     * AdvJsonParser里给Item设置mSource
     * 等等
     */
    private String mId;
    private String mAId;
    private String mPos;
    private String mICPos;
    private String mCp;
    private String mCt;
    private String mCpdPs;
    private String mObjectId;
    private String mType;
    private String mStyle;
    private String mListPos;
    private String mCFrom;
    private String mReqId;
    private String mModuleId;
    private String mSource;
    private String mRelatedId;
    private String mUpdate;
    private String mDt;
    private String mKeyStatus;
    private String mGameRecId;

    private final HashMap<String, String> mCustomMap = new HashMap<>();

    private final ExposeStatus mExposeStatus = new ExposeStatus();

    private String mDebugDescribe;

    /**
     * 是否展示曝光过，在展示曝光中使用
     */
    private boolean mHasOnceExpose = false;

    /**
     * 针对单个item的展示曝光
     */
    private boolean mCanSingleExpose = false;

    /**
     * 自定义绑定数据
     */
    private Serializable mCustomBindData;

    public void setCustomBindData(Serializable customBindData) {
        this.mCustomBindData = customBindData;
    }

    public Serializable getCustomBindData() {
        return mCustomBindData;
    }

    public void setCanSingleExpose(boolean canSingleExpose) {
        this.mCanSingleExpose = canSingleExpose;
    }

    public boolean isCanSingleExpose() {
        return mCanSingleExpose;
    }

    boolean isHasOnceExpose() {
        return mHasOnceExpose;
    }

    void setHasOnceExpose(boolean hasOnceExpose) {
        this.mHasOnceExpose = hasOnceExpose;
    }

    public String getDebugDescribe() {
        return TextUtils.isEmpty(mDebugDescribe) ? "null" : mDebugDescribe;
    }

    public void setDebugDescribe(String debugDescribe) {
        this.mDebugDescribe = debugDescribe;
    }

    public int getExposeCount() {
        return mExposeStatus.getExpCount();
    }

    public boolean setExpStatus(boolean expStatus, String page, ExposeAppData exposeAppData) {
        return mExposeStatus.setmExpStatus(expStatus, page, exposeAppData);
    }

    public void setExpCanExposeStart(boolean canExposeStart) {
        mExposeStatus.setCanExposeStart(canExposeStart);
    }

    public void resetExpCount() {
        mExposeStatus.resetExpCount();
    }

    public boolean isExposeTooShortTime() {
        return mExposeStatus.isExposeTooShortDelay();
    }

    public ExposeAppData put(String key, String value) {
        mCustomMap.put(key, value);
        return this;
    }

    public ExposeAppData putId(String mId) {
        this.mId = mId;
        return this;
    }

    public ExposeAppData putAId(String mAId) {
        this.mAId = mAId;
        return this;
    }

    public ExposeAppData putPos(String mPos) {
        this.mPos = mPos;
        return this;
    }

    public ExposeAppData putCp(String mCp) {
        this.mCp = mCp;
        return this;
    }

    public ExposeAppData putCt(String mCt) {
        this.mCt = mCt;
        return this;
    }

    public ExposeAppData putCpdPs(String mCpdPs) {
        this.mCpdPs = mCpdPs;
        return this;
    }


    public ExposeAppData putObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
        return this;
    }

    public ExposeAppData putType(String mType) {
        this.mType = mType;
        return this;
    }

    public ExposeAppData putStyle(String mStyle) {
        this.mStyle = mStyle;
        return this;
    }

    public ExposeAppData putListPos(String mListPos) {
        this.mListPos = mListPos;
        return this;
    }

    public ExposeAppData putCFrom(String mCFrom) {
        this.mCFrom = mCFrom;
        return this;
    }

    public ExposeAppData putReqId(String mReqId) {
        this.mReqId = mReqId;
        return this;
    }

    public ExposeAppData putModuleId(String mModuleId) {
        this.mModuleId = mModuleId;
        return this;
    }

    public ExposeAppData putSource(String mSource) {
        this.mSource = mSource;
        return this;
    }

    public ExposeAppData putRelatedId(String mRelatedId) {
        this.mRelatedId = mRelatedId;
        return this;
    }

    public ExposeAppData putUpdate(String mUpdate) {
        this.mUpdate = mUpdate;
        return this;
    }

    public ExposeAppData putICPos(String mICPos) {
        this.mICPos = mICPos;
        return this;
    }

    public ExposeAppData putDt(String mDt) {
        this.mDt = mDt;
        return this;
    }

    public ExposeAppData putKeyStatus(String installed) {
        this.mKeyStatus = installed;
        return this;
    }

    public ExposeAppData putGameRecId(String gameRecId) {
        this.mGameRecId = gameRecId;
        return this;
    }

    private HashMap<String, String> toHashMap(boolean needCount) {
        HashMap<String, String> params = new HashMap<>(mCustomMap);

        if (needCount) {
            params.put(COUNT, Integer.toString(mExposeStatus.getExpCount()));
            String dur = mExposeStatus.getmExpDurString();
            if (!TextUtils.isEmpty(dur)) {
                params.put(DUR, dur);
            }
        }

        if (mId != null) {
            params.put(ID, mId);
        }
        if (mAId != null) {
            params.put(AID, mAId);
        }
        if (mPos != null) {
            params.put(POS, mPos);
        }
        if (mICPos != null) {
            params.put(IC_POS, mICPos);
        }
        if (mCp != null) {
            params.put(CP, mCp);
        }
        if (mCt != null) {
            params.put(CT, mCt);
        }
        if (mCpdPs != null) {
            params.put(CPD_PS, mCpdPs);
        }
        if (mObjectId != null) {
            params.put(OBJECT_ID, mObjectId);
        }
        if (mType != null) {
            params.put(TYPE, mType);
        }
        if (mStyle != null) {
            params.put(STYLE, mStyle);
        }
        if (mListPos != null) {
            params.put(LIST_POS, mListPos);
        }
        if (mCFrom != null) {
            params.put(C_FROM, mCFrom);
        }
        if (mReqId != null) {
            params.put(REQ_ID, mReqId);
        }
        if (mModuleId != null) {
            params.put(MODULE_ID, mModuleId);
        }
        if (mSource != null) {
            params.put(SOURCE, mSource);
        }
        if (mRelatedId != null) {
            params.put(RELATE_ID, mRelatedId);
        }
        if (mUpdate != null) {
            params.put(UPDATE, mUpdate);
        }
        if (mDt != null) {
            params.put(Dt, mDt);
        }
        if (mKeyStatus != null) {
            params.put(KEY_STATUS, mKeyStatus);
        }
        if (mGameRecId != null) {
            params.put(GAME_REC_ID, mGameRecId);
        }
        return params;
    }

    /**
     * 到jsonObject
     *
     * @return
     */
    public JSONObject toJsonObject(boolean needCount) {
        JSONObject params = new JSONObject();
        try {
            HashMap<String, String> map = toHashMap(needCount);
            if (!map.isEmpty()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (TextUtils.isEmpty(key)) {
                        continue;
                    }
                    if (!needCount) {
                        //展示曝光要对pos减1
                        if (key.equals(POS) || key.equals(IC_POS)) {
                            value = decreaseOne(value);
                        }
                    }
                    params.put(key, value);
                }
            }
        } catch (JSONException e) {
            HideVlog.d(TAG, "JSONException");
        }
        return params;
    }


    public String getSource() {
        return mSource;
    }

    private static String decreaseOne(String number) {
        try {
            int n = Integer.parseInt(number);
            return Integer.toString(n - 1);
        } catch (Exception e) {
            return "-1";
        }
    }
}
