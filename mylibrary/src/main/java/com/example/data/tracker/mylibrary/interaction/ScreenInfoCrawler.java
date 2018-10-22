package com.example.data.tracker.mylibrary.interaction;

import android.app.Activity;
import android.util.JsonWriter;
import android.view.View;

import com.example.data.tracker.mylibrary.AOP.expose.LogUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ScreenInfoCrawler {

    private static final String TAG = "ScreenInfoCrawler";
    private SnapshotCrawler snapshotCrawler;
    private ViewTreeCrawler viewTreeCrawler;

    public ScreenInfoCrawler() {
        snapshotCrawler = new SnapshotCrawler();
        viewTreeCrawler = new ViewTreeCrawler();
    }

    //todo 子线程执行
    void writeScreenInfo(OutputStream outputStream, Activity activity) {
        LogUtil.i(TAG, "writeScreenInfo: ");
        try {
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            View rootView = activity.getWindow().getDecorView().getRootView();
            writer.write("{");
            writer.write("\"activity\":");
            writer.write(JSONObject.quote(activity.getClass().getCanonicalName()));
            writer.write(",");
            writer.write("\"scale\":");
            writer.write(String.format("%s", 1));
            writer.write(",");
            writer.write("\"serialized_objects\":");
            JsonWriter jsonWriter = new JsonWriter(writer);
            viewTreeCrawler.writeViewTreeInfo(rootView,jsonWriter);
            writer.write(",");
            writer.write("\"screenshot\":");
            writer.flush();
            snapshotCrawler.writeSnapshotData(rootView,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
