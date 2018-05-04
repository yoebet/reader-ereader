package wjy.yo.ereader.di;

import dagger.Binds;
import dagger.Module;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.ChapService;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.service.impl.BookServiceImpl;
import wjy.yo.ereader.service.impl.ChapServiceImpl;
import wjy.yo.ereader.service.impl.DictServiceImpl;
import wjy.yo.ereader.service.impl.VocabularyServiceImpl;

@Module
abstract class AppModule {

    @Binds
    abstract VocabularyService vocabularyService(VocabularyServiceImpl vocabularyService);

    @Binds
    abstract BookService bookService(BookServiceImpl bookService);

    @Binds
    abstract ChapService chapService(ChapServiceImpl chapService);

    @Binds
    abstract DictService dictService(DictServiceImpl dictService);

/*    @Singleton  @Provides
    public VocabularyService provideVocabularyService() {
        return new VocabularyServiceImpl();
    }*/
}
