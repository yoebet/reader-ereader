package wjy.yo.ereader;

import android.app.Activity;
import android.app.Application;

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

        try {
            RxJavaPlugins.setErrorHandler((Throwable t) -> {
                System.out.println("xxxxxxxxxxxxxx");
                t.printStackTrace();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

}
