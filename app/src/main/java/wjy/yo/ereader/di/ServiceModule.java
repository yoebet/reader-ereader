package wjy.yo.ereader.di;

import dagger.Binds;
import dagger.Module;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.serviceimpl.AccountServiceImpl;
import wjy.yo.ereader.serviceimpl.BookServiceImpl;
import wjy.yo.ereader.serviceimpl.DictServiceImpl;
import wjy.yo.ereader.serviceimpl.VocabularyServiceImpl;

@Module(includes = RemoteAPI.class)
abstract class ServiceModule {

    @Binds
    abstract AccountService accountService(AccountServiceImpl accountService);

    @Binds
    abstract BookService bookService(BookServiceImpl bookService);

    @Binds
    abstract DictService dictService(DictServiceImpl dictService);

    @Binds
    abstract VocabularyService vocabularyService(VocabularyServiceImpl vocabularyService);

}
