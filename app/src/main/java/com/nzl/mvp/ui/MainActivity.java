package com.nzl.mvp.ui;

import android.graphics.Color;

import com.githang.statusbar.StatusBarCompat;
import com.nzl.mvp.R;
import com.nzl.mvp.base.BaseActivity;
import com.nzl.mvp.component.AppComponent;

public class MainActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void configViews() {
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"), true);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }
}
