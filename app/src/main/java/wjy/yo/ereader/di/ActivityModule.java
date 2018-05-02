package wjy.yo.ereader.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import wjy.yo.ereader.MainActivity;
import wjy.yo.ereader.reader.ReaderActivity;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector()
    abstract ReaderActivity contributeReaderActivity();
}
