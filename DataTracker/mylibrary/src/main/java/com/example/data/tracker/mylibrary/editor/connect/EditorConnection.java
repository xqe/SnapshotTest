package com.example.data.tracker.mylibrary.editor.connect;

import java.io.BufferedOutputStream;
import java.net.URI;

public class EditorConnection {
    private EditorClient editorClient;

    public EditorConnection(URI uri,int socketTimeout){
        editorClient = new EditorClient(uri,socketTimeout);
        try {
            editorClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public BufferedOutputStream getBufferedOutputStream() {
        return new BufferedOutputStream(new EditorOutputStream(editorClient));
    }

    public boolean isDisconnected(){
        return editorClient.isClosed();
    }
}
