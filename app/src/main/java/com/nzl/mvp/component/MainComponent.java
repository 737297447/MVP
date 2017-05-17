package com.nzl.mvp.component;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/7.
 * 用@Component表示这个接口是一个连接器，能用@Component注解的只能是interface或者抽象类
 */

@Component(dependencies = AppComponent.class)
public interface MainComponent {


//    AboutActivity inject(AboutActivity activity);

}
