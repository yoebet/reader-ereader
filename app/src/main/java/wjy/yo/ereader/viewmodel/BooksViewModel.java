package wjy.yo.ereader.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.repository.BookRepository;

public class BooksViewModel extends ViewModel {

    private LiveData<List<Book>> booksLiveData;

    @Inject
    public BooksViewModel(BookRepository bookRepository) {
        this.booksLiveData = bookRepository.loadBooks();
    }

    public LiveData<List<Book>> getBooksLiveData() {
        return booksLiveData;
    }
}
