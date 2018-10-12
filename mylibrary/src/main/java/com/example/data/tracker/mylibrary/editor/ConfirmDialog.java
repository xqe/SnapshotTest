package com.example.data.tracker.mylibrary.editor;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.data.tracker.mylibrary.R;

public class ConfirmDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_editor,container,false);
        return view;
    }
}
