package wjy.yo.ereader.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import wjy.yo.ereader.activity.BookDetailFragment;

@Module
public abstract class BookDetailFragmentModule {

    @ContributesAndroidInjector
    abstract BookDetailFragment contributeBookDetailFragment();
}
