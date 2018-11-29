package com.easyliu.test.shareelementdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * @author easyliu
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
    }
}
