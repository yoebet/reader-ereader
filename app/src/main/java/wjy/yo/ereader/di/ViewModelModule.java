package wjy.yo.ereader.di;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import wjy.yo.ereader.ui.book.BooksViewModel;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ClassKey(BooksViewModel.class)
    abstract ViewModel bindUserViewModel(BooksViewModel booksViewModel);

}
