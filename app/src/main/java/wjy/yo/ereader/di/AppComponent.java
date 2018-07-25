package wjy.yo.ereader.di;


import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import wjy.yo.ereader.EreaderApp;
import wjy.yo.ereader.ui.dict.DictView;
import wjy.yo.ereader.ui.dict.WordTextPagerAdapter;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        DbModule.class,
        ServiceModule.class,
        ActivityModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(EreaderApp ereaderApp);

    void inject(WordTextPagerAdapter pagerAdapter);

    void inject(DictView dictView);
}
