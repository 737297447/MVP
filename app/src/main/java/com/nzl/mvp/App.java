package com.nzl.mvp;

import android.app.Application;

import com.nzl.mvp.component.AppComponent;
import com.nzl.mvp.component.DaggerAppComponent;
import com.nzl.mvp.module.ApiModule;
import com.nzl.mvp.module.AppModule;
import com.nzl.mvp.util.FileUtil;

/**
 * Created by C on 2017/5/17.
 */

public class App extends Application {

    private static App instance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;

        initComponent();

        FileUtil.initFileCache(this);

    }


    public static App getInstance() {
        return instance;
    }

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .apiModule(new ApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
