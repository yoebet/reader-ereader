package wjy.yo.ereader.ui.book;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.repository.BookRepository;

@Singleton
public class BookViewModel extends ViewModel {

    private final MutableLiveData<String> liveBookId;

    private final LiveData<Book> bookWithChaps;

    @Inject
    public BookViewModel(BookRepository bookRepository) {
        this.liveBookId = new MutableLiveData<>();
        this.bookWithChaps = Transformations.switchMap(this.liveBookId, bookRepository::loadBookDetail);

        System.out.println("new BookViewModel: " + this);
    }

    public void setBookId(String id) {
        this.liveBookId.setValue(id);
    }

    public LiveData<Book> getBookWithChaps() {
        return bookWithChaps;
    }
}
