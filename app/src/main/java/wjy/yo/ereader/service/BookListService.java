package wjy.yo.ereader.service;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.book.BookDetail;

public interface BookListService {

    Flowable<List<Book>> loadBooks();

    Flowable<List<Book>> loadBooksWithUserBook();
}
