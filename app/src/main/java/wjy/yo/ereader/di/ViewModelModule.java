package wjy.yo.ereader.di;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import wjy.yo.ereader.ui.booklist.BookListViewModel;
import wjy.yo.ereader.ui.book.BookViewModel;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ClassKey(BookListViewModel.class)
    abstract ViewModel bindUserViewModel(BookListViewModel bookListViewModel);

    @Binds
    @IntoMap
    @ClassKey(BookViewModel.class)
    abstract ViewModel bindBookViewModel(BookViewModel bookViewModel);
}
