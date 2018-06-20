package wjy.yo.ereader.di;

import dagger.Binds;
import dagger.Module;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookContentService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.serviceimpl.AccountServiceImpl;
import wjy.yo.ereader.serviceimpl.BookContentServiceImpl;
import wjy.yo.ereader.serviceimpl.BookServiceImpl;
import wjy.yo.ereader.serviceimpl.DataSyncServiceImpl;
import wjy.yo.ereader.serviceimpl.DictServiceImpl;
import wjy.yo.ereader.serviceimpl.LocalSettingServiceImpl;
import wjy.yo.ereader.serviceimpl.UserWordServiceImpl;
import wjy.yo.ereader.serviceimpl.VocabularyServiceImpl;

@Module(includes = RemoteAPI.class)
abstract class ServiceModule {

    @Binds
    abstract AccountService accountService(AccountServiceImpl s);

    @Binds
    abstract BookService bookService(BookServiceImpl s);

    @Binds
    abstract BookContentService bookContentService(BookContentServiceImpl s);

    @Binds
    abstract DictService dictService(DictServiceImpl s);

    @Binds
    abstract UserWordService userWordService(UserWordServiceImpl s);

    @Binds
    abstract VocabularyService vocabularyService(VocabularyServiceImpl s);

    @Binds
    abstract DataSyncService dataSyncService(DataSyncServiceImpl s);

    @Binds
    abstract LocalSettingService localSettingService(LocalSettingServiceImpl s);

}
