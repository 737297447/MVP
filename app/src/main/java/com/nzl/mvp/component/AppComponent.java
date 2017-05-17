package com.nzl.mvp.component;

import android.content.Context;

import com.nzl.mvp.api.Api;
import com.nzl.mvp.module.ApiModule;
import com.nzl.mvp.module.AppModule;

import dagger.Component;

/** Component(组件)
 * Created by Administrator on 2016/11/7.
 */
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    Context getContext();

    Api getApi();

}
