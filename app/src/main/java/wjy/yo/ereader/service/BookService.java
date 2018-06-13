package wjy.yo.ereader.service;

import android.arch.lifecycle.LiveData;

import java.util.List;

import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.entityvo.book.ChapDetail;

public interface BookService {

    LiveData<List<Book>> loadBooks();

    LiveData<BookDetail> loadBookDetail(String bookId);

    LiveData<ChapDetail> loadChapDetail(String chapId);
}
