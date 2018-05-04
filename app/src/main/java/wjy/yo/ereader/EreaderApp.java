package wjy.yo.ereader;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import wjy.yo.ereader.di.AppInjector;

public class EreaderApp extends Application implements HasActivityInjector/*, HasSupportFragmentInjector*/ {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

//    @Inject
//    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

//    @Override
//    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
//        return fragmentInjector;
//    }
}
