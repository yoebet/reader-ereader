package wjy.yo.ereader.di;

import dagger.Binds;
import dagger.Module;
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

@Module(includes = RemoteAPI.class)
abstract class ServiceModule {

    @Binds
    abstract AccountService accountService(AccountServiceImpl accountService);

    @Binds
    abstract BookService bookService(BookServiceImpl bookService);

    @Binds
    abstract ChapService chapService(ChapServiceImpl chapService);

    @Binds
    abstract DictService dictService(DictServiceImpl dictService);

    @Binds
    abstract VocabularyService vocabularyService(VocabularyServiceImpl vocabularyService);

}
