package wjy.yo.ereader.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import wjy.yo.ereader.ui.book.BookDetailActivity;
import wjy.yo.ereader.ui.booklist.BookListActivity;
import wjy.yo.ereader.ui.LaunchScreenActivity;
import wjy.yo.ereader.ui.dict.DictActivity;
import wjy.yo.ereader.ui.reader.ReaderActivity;
import wjy.yo.ereader.ui.vocabulary.VocabularyActivity;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract LaunchScreenActivity contributeLaunchScreenActivity();

    @ContributesAndroidInjector()
    abstract BookListActivity contributeBookListActivity();

    @ContributesAndroidInjector
    abstract BookDetailActivity contributeBookDetailActivity();

    @ContributesAndroidInjector()
    abstract ReaderActivity contributeReaderActivity();

    @ContributesAndroidInjector()
    abstract VocabularyActivity contributeVocabularyActivity();

    @ContributesAndroidInjector()
    abstract DictActivity contributeDictActivity();

}
