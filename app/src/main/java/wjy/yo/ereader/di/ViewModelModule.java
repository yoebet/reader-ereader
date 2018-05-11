package wjy.yo.ereader.di;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import wjy.yo.ereader.viewmodel.BooksViewModel;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ClassKey(BooksViewModel.class)
    abstract ViewModel bindUserViewModel(BooksViewModel booksViewModel);

}
