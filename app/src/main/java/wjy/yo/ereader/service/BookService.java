package wjy.yo.ereader.service;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.book.BookDetail;

public interface BookService {

    Flowable<List<Book>> loadBooks();

    Flowable<BookDetail> loadBookDetail(String bookId);
}
