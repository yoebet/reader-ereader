package wjy.yo.ereader.service;

import io.reactivex.Flowable;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.book.BookDetail;

public interface BookService {

    Flowable<Book> loadBook(String bookId);

    Flowable<BookDetail> loadBookDetail(String bookId);

    Flowable<BookDetail> loadBookWithUserChaps(String bookId);
}
