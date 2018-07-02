package wjy.yo.ereader;

import android.app.Activity;
import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.reactivex.plugins.RxJavaPlugins;
import wjy.yo.ereader.di.AppInjector;

public class EreaderApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
        JodaTimeAndroid.init(this);

        RxJavaPlugins.setErrorHandler((Throwable t) -> {
            System.out.println("xxxxxxxxxxxxxx");
            t.printStackTrace();
        });
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

}
