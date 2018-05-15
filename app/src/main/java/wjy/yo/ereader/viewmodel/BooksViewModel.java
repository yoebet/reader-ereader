package wjy.yo.ereader.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import wjy.yo.ereader.db.BookDao;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.repository.BookRepository;

public class BooksViewModel extends ViewModel {

    private final LiveData<List<Book>> books;

    private final MutableLiveData<String> liveBookId;

//    private final LiveData<Book> book;

    private final LiveData<Book> bookWithChaps;

    @Inject
    public BooksViewModel(BookDao bookDao, BookRepository bookRepository) {
        this.books = bookRepository.loadBooks();
        this.liveBookId = new MutableLiveData<>();
//        this.book = Transformations.switchMap(this.liveBookId, bookDao::load);
        this.bookWithChaps = Transformations.switchMap(this.liveBookId, bookRepository::loadBookDetail);
    }

    public void setBookId(String id) {
        this.liveBookId.setValue(id);
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }

//    public LiveData<Book> getBook() {
//        return book;
//    }


    public LiveData<Book> getBookWithChaps() {
        return bookWithChaps;
    }
}
