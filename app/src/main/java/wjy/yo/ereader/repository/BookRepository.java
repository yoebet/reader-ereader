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
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.remote.BooksAPI;

@Singleton
public class BookRepository {
    private DB db;
    private BookDao bookDao;
    private BooksAPI booksAPI;


    private final AppExecutors appExecutors;

    private RateLimiter<String> bookRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    public BookRepository(DB db, BookDao bookDao, BooksAPI booksAPI, AppExecutors appExecutors) {
        this.db = db;
        this.bookDao = bookDao;
        this.booksAPI = booksAPI;
        this.appExecutors = appExecutors;
    }

    public LiveData<List<Book>> loadBooks() {
        return new NetworkBoundResource<List<Book>>(appExecutors) {
            @Override
            protected void saveCallResult(List<Book> books) {
                System.out.println("saveCallResult ...");
                bookDao.insertBooks(books);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Book> data) {
                return data == null || data.isEmpty() || bookRateLimit.shouldFetch("bookList");
            }

//            @NonNull
            @Override
            protected LiveData<List<Book>> loadFromDb() {
                System.out.println("loadFromDb ...");
                return bookDao.loadBooks();
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
}
