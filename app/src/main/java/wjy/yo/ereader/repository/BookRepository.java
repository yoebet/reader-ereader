package wjy.yo.ereader.repository;


import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.AppExecutors;
import wjy.yo.ereader.db.BookDao;
import wjy.yo.ereader.db.ChapDao;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.remote.BooksAPI;

@Singleton
public class BookRepository {
    private DB db;
    private BookDao bookDao;
    private ChapDao chapDao;
    private BooksAPI booksAPI;


    private final AppExecutors appExecutors;

    private RateLimiter<String> bookRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    BookRepository(DB db, BookDao bookDao, ChapDao chapDao, BooksAPI booksAPI, AppExecutors appExecutors) {
        this.db = db;
        this.bookDao = bookDao;
        this.chapDao = chapDao;
        this.booksAPI = booksAPI;
        this.appExecutors = appExecutors;
    }

    public LiveData<List<Book>> loadBooks() {
        return new NetworkBoundResource<List<Book>>(appExecutors) {
            @Override
            protected void saveCallResult(List<Book> books) {
                System.out.println("saveCallResult ...");
                bookDao.insert(books);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Book> data) {
                return data == null || data.isEmpty() || bookRateLimit.shouldFetch("bookList");
            }

            //@NonNull
            @Override
            protected LiveData<List<Book>> loadFromDb() {
                System.out.println("loadFromDb ...");
                return bookDao.loadAll();
            }

            @NonNull
            @Override
            protected LiveData<List<Book>> createCall() {
                return booksAPI.listAllBooks2();
            }

            @Override
            protected void onFetchFailed() {
                bookRateLimit.reset("bookList");
            }
        }.asLiveData();
    }


    public LiveData<Book> loadBookDetail(String bookId) {
        return new NetworkBoundResource<Book>(appExecutors) {

            @Override
            void saveCallResult(Book book) {
                bookDao.update(book);
                List<Chap> chaps = book.getChaps();
                if (chaps == null) {
                    return;
                }
                for (Chap chap : chaps) {
                    if (chap.getBookId() == null) {
                        chap.setBookId(book.getId());
                    }
                }

                int deleted = chapDao.deleteBookChaps(book.getId());
                System.out.println("delete chaps: " + deleted);
                chapDao.insert(chaps);
                System.out.println("insert chaps: " + chaps.size());
            }

            @Override
            boolean shouldFetch(@Nullable Book book) {
                return book == null || book.getChaps() == null || bookRateLimit.shouldFetch("book_" + book.getId());
            }

            @Override
            LiveData<Book> loadFromDb() {
                return loadBookDetailFromDb(bookId);
            }

            @NonNull
            @Override
            LiveData<Book> createCall() {
                return booksAPI.getBookWithChaps(bookId);
            }
        }.asLiveData();
    }

    LiveData<Book> loadBookDetailFromDb(String bookId) {
        LiveData<Book> book = bookDao.load(bookId);
        LiveData<List<Chap>> chaps = chapDao.loadChaps(bookId);
        return new OneToManyLiveData<>(book, chaps, Book::setChaps);
    }

}
