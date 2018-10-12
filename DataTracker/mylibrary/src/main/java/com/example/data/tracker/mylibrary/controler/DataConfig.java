package com.example.data.tracker.mylibrary.controler;

import java.util.HashMap;
import java.util.Map;

public class DataConfig {

    //ViewID,EventID
    private Map<String,String> configViewMap = new HashMap<>();

    //for test
    String[] viewIDs = {
      "android.widget.LinearLayout[0]-android.widget.FrameLayout[0]#content-android.widget.LinearLayout[0]-android.support.v7.widget.RecyclerView[0]#recycler_view",
            "android.widget.LinearLayout[0]-android.widget.FrameLayout[0]#content-com.example.data.tracker.mylibrary.expose.TrackRootView[0]-android.widget.LinearLayout[0]-android.support.v7.widget.RecyclerView[0]#recycler_view"
    };

    private DataConfig() {
        //for test
        for (int i = 0; i < viewIDs.length; i++) {
            configViewMap.put(viewIDs[i],i+"");
        }
    }

    public static DataConfig getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DataConfig INSTANCE = new DataConfig();
    }

    public void setConfigViewMap(Map<String, String> configViewMap) {
        this.configViewMap = configViewMap;
    }

    public Map<String, String> getConfigViewMap() {
        return configViewMap;
    }
}
