package com.example.data.tracker.mylibrary.editor;

import android.app.Activity;
import android.util.JsonWriter;
import android.view.View;

import com.example.data.tracker.mylibrary.editor.snapshot.SnapshotCrawler;
import com.example.data.tracker.mylibrary.editor.viewTree.ViewTreeCrawler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ViewCrawler {
    private SnapshotCrawler snapshotCrawler;
    private ViewTreeCrawler viewTreeCrawler;

    public ViewCrawler() {
        snapshotCrawler = new SnapshotCrawler();
        viewTreeCrawler = new ViewTreeCrawler();
    }

    public void writeScreenInfo(OutputStream outputStream, Activity activity) {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        JsonWriter jsonWriter = new JsonWriter(writer);
        View rootView = activity.getWindow().getDecorView().getRootView();
        try {
            viewTreeCrawler.writeViewTreeInfo(rootView,jsonWriter);
            snapshotCrawler.writeSnapshotData(rootView,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
