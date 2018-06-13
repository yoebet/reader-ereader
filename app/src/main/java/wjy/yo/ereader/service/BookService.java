package wjy.yo.ereader.service;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.entityvo.book.ChapDetail;

public interface BookService {

    Flowable<List<Book>> loadBooks();

    Flowable<BookDetail> loadBookDetail(String bookId);

    Flowable<ChapDetail> loadChapDetail(String chapId);
}
