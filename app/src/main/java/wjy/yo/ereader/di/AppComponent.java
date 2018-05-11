package wjy.yo.ereader.di;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import wjy.yo.ereader.EreaderApp;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        DbModule.class,
        ServiceModule.class,
        ActivityModule.class,
        ViewModelModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(EreaderApp ereaderApp);
}
