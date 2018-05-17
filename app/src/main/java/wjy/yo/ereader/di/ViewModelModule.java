package wjy.yo.ereader.di;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import wjy.yo.ereader.ui.booklist.BookListViewModel;
import wjy.yo.ereader.ui.book.BookViewModel;
import wjy.yo.ereader.ui.reader.ReaderViewModel;

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

    @Binds
    @IntoMap
    @ClassKey(ReaderViewModel.class)
    abstract ViewModel bindReaderViewModel(ReaderViewModel readerViewModel);
}
