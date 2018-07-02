package wjy.yo.ereader.service;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.BookDetail;

public interface BookService {

    Maybe<Book> loadBook(String bookId);

    Maybe<Chap> loadChap(String chapId);

    Flowable<BookDetail> loadBookDetail(String bookId);

    Flowable<BookDetail> loadBookWithUserChaps(String bookId);
}
