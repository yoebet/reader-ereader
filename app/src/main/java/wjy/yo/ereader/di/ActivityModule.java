package wjy.yo.ereader.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import wjy.yo.ereader.MainActivity;
import wjy.yo.ereader.activity.BookDetailActivity;
import wjy.yo.ereader.activity.BookListActivity;
import wjy.yo.ereader.reader.ReaderActivity;

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector()
    abstract ReaderActivity contributeReaderActivity();

    @ContributesAndroidInjector()
    abstract BookListActivity contributeBookListActivity();

    @ContributesAndroidInjector(modules = BookDetailFragmentModule.class)
    abstract BookDetailActivity contributeBookDetailActivity();
}
