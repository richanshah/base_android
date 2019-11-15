package com.example.ui;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.R;
import com.example.databinding.ActivitySplashBinding;
import com.example.ui.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    ActivitySplashBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        runSplashActivity();


    }

    private void runSplashActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },2000);
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(SplashActivity.this, cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
