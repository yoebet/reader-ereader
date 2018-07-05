package wjy.yo.ereader.di;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import dagger.android.AndroidInjection;
import dagger.android.HasActivityInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import wjy.yo.ereader.EreaderApp;

public class AppInjector {

    private AppInjector() {
    }

    public static void init(EreaderApp ereaderApp) {
        DaggerAppComponent.builder()
                .application(ereaderApp)
                .build().inject(ereaderApp);
        ereaderApp
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        System.out.println("onActivityCreated: " + activity);
                        handleActivity(activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {

                    }
                });
    }

    private static void handleActivity(Activity activity) {
        try {
            AndroidInjection.inject(activity);
            if (!(activity instanceof FragmentActivity)) {
                return;
            }
            FragmentActivity fa = (FragmentActivity) activity;
            FragmentManager fm = fa.getSupportFragmentManager();

            fm.registerFragmentLifecycleCallbacks(
                    new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentCreated(FragmentManager fm, Fragment f,
                                                      Bundle savedInstanceState) {
                            if (f instanceof Injectable) {
                                try {
                                    AndroidSupportInjection.inject(f);
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }, true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
