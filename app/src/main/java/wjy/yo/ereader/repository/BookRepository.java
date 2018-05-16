package wjy.yo.ereader.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.db.BookDao;
import wjy.yo.ereader.db.ChapDao;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.model.BaseModel;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.model.IdVersion;
import wjy.yo.ereader.remote.BooksAPI;
import wjy.yo.ereader.util.AppExecutors;

@Singleton
public class BookRepository {
    private DB db;
    private BookDao bookDao;
    private ChapDao chapDao;
    private BooksAPI booksAPI;


    private final AppExecutors appExecutors;

    private RateLimiter<String> bookRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    BookRepository(DB db, BooksAPI booksAPI, AppExecutors appExecutors) {
        this.db = db;
        this.bookDao = db.bookDao();
        this.chapDao = db.chapDao();
        this.booksAPI = booksAPI;
        this.appExecutors = appExecutors;
    }

    public LiveData<List<Book>> loadBooks() {
        return new NetworkBoundResource<List<Book>>(appExecutors) {
            @Override
            protected void saveCallResult(List<Book> books) {
                System.out.println("1 saveCallResult ...");

                List<IdVersion> ivs = bookDao.loadAllIdVersion();

                ModelChanges.Changes changes = ModelChanges.evaluateChanges((List<BaseModel>) (List<?>) books, ivs);
                ModelChanges.applyChanges(changes, bookDao, true);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Book> data) {
                return data == null || data.isEmpty() || bookRateLimit.shouldFetch("bookList");
            }

            //@NonNull
            @Override
            protected LiveData<List<Book>> loadFromDb() {
                System.out.println("1 loadFromDb ...");
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
                System.out.println("2 saveCallResult ...");

                ModelChanges.saveIfNeeded(book, bookDao);

                List<Chap> chaps = book.getChaps();
                if (chaps == null) {
                    return;
                }

                String bookId = book.getId();
                for (Chap chap : chaps) {
                    chap.setBookId(bookId);
                }

                List<IdVersion> ivs = chapDao.loadIdVersions(bookId);

                ModelChanges.Changes changes = ModelChanges.evaluateChanges((List<BaseModel>) (List<?>) chaps, ivs);
                ModelChanges.applyChanges(changes, chapDao, true);
            }

            @Override
            boolean shouldFetch(@Nullable Book book) {
                return book == null || book.getChaps() == null || bookRateLimit.shouldFetch("book_" + book.getId());
            }

            @Override
            LiveData<Book> loadFromDb() {
                System.out.println("2 loadFromDb ...");
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
