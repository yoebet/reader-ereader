package wjy.yo.ereader.service;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.book.BookDetail;

public interface BookService {

    Flowable<List<Book>> loadBooks();

    Flowable<List<Book>> loadBooksWithUserBook();

    Flowable<Book> loadBook(String bookId);

    Flowable<BookDetail> loadBookDetail(String bookId);

    Flowable<BookDetail> loadBookWithUserChaps(String bookId);
}
