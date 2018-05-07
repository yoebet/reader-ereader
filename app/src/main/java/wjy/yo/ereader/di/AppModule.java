package wjy.yo.ereader.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.ChapService;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.service.impl.AccountServiceImpl;
import wjy.yo.ereader.service.impl.BookServiceImpl;
import wjy.yo.ereader.service.impl.ChapServiceImpl;
import wjy.yo.ereader.service.impl.DictServiceImpl;
import wjy.yo.ereader.service.impl.VocabularyServiceImpl;

@Module
abstract class AppModule {

    @Binds
    abstract Context provideContext(Application context);
}
