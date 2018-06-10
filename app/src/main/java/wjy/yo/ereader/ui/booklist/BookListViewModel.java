package wjy.yo.ereader.ui.booklist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.repository.BookRepository;

@Singleton
public class BookListViewModel extends ViewModel {

    private final LiveData<List<Book>> books;

    @Inject
    public BookListViewModel(BookRepository bookRepository) {
        this.books = bookRepository.loadBooks();

        System.out.println("new BookListViewModel: " + this);
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }
}
