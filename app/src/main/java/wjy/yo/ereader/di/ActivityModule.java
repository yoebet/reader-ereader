package wjy.yo.ereader.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import wjy.yo.ereader.activity.BookDetailActivity;
import wjy.yo.ereader.activity.BookListActivity;
import wjy.yo.ereader.activity.LaunchScreenActivity;
import wjy.yo.ereader.reader.ReaderActivity;

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
