package wjy.yo.ereader.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import wjy.yo.ereader.ui.book.BookDetailActivity;
import wjy.yo.ereader.ui.book.BookListActivity;
import wjy.yo.ereader.ui.LaunchScreenActivity;
import wjy.yo.ereader.ui.reader.ReaderActivity;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract LaunchScreenActivity contributeLaunchScreenActivity();

    @ContributesAndroidInjector()
    abstract BookListActivity contributeBookListActivity();

    @ContributesAndroidInjector(modules = BookDetailFragmentModule.class)
    abstract BookDetailActivity contributeBookDetailActivity();

    @ContributesAndroidInjector()
    abstract ReaderActivity contributeReaderActivity();

}
