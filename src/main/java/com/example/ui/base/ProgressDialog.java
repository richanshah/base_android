package com.example.ui.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.R;
import com.example.databinding.DialogProgressBinding;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

public class ProgressDialog extends AlertDialog {
    private String message;

    public ProgressDialog(Context context) {
        this(context, null);
    }

    public ProgressDialog(Context context, String message) {
        super(context, R.style.ProgressDialog);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogProgressBinding mBinder = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_progress, null, false);
        setContentView(mBinder.getRoot());
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        if (message != null) mBinder.tvMessage.setText(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
