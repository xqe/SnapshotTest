package com.example.data.tracker.mylibrary.editor.connect;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;

class EditorOutputStream extends OutputStream {

    private EditorClient editorClient;

    EditorOutputStream(EditorClient editorClient) {
        this.editorClient = editorClient;
    }

    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) b;
        write(bytes,0,bytes.length);
    }

    @Override
    public void write(@NonNull byte[] b) throws IOException {
        write(b,0,b.length);
    }

    @Override
    public void write(@NonNull byte[] b, int off, int len) throws IOException {
        editorClient.send(b);
    }
}
