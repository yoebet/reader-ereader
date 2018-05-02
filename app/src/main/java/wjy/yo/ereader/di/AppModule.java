package wjy.yo.ereader.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wjy.yo.ereader.service.VocabularyService;

@Module
public class AppModule {

    @Singleton @Provides
    VocabularyService provideVocabularyService(){
        return new VocabularyService();
    }
}
