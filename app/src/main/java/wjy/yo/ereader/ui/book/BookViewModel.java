package wjy.yo.ereader.ui.book;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.service.BookService;

@Singleton
public class BookViewModel extends ViewModel {

    private final MutableLiveData<String> liveBookId;

    private final LiveData<BookDetail> bookWithChaps;

    @Inject
    public BookViewModel(BookService bookService) {
        this.liveBookId = new MutableLiveData<>();
        this.bookWithChaps = Transformations.switchMap(this.liveBookId, bookService::loadBookDetail);

        System.out.println("new BookViewModel: " + this);
    }

    public void setBookId(String id) {
        this.liveBookId.setValue(id);
    }

    public LiveData<BookDetail> getBookWithChaps() {
        return bookWithChaps;
    }
}
