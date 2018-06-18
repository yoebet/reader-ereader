package wjy.yo.ereader.serviceimpl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.BookDao;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.serviceimpl.common.ModelChanges;
import wjy.yo.ereader.serviceimpl.common.NetworkBoundResource;
import wjy.yo.ereader.serviceimpl.common.RateLimiter;

@Singleton
public class BookServiceImpl implements BookService {

    private static final String BOOK_LIST_KEY = "BOOK_LIST";
    private static final String BOOK_KEY_PREFIX = "BOOK_";

    private BookDao bookDao;
    private ChapDao chapDao;
    private BookAPI bookAPI;

    private RateLimiter<String> bookRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    BookServiceImpl(DB db, BookAPI bookAPI) {
        this.bookDao = db.bookDao();
        this.chapDao = db.chapDao();
        this.bookAPI = bookAPI;
        System.out.println("new BookServiceImpl");
    }

    public Flowable<List<Book>> loadBooks() {
        return new NetworkBoundResource<List<Book>>() {
            @Override
            protected void saveCallResult(List<Book> books, List<Book> localBooks) {
                System.out.println("1 saveCallResult ...");

                ModelChanges.Changes changes = ModelChanges.evaluateChanges(
                        (List<FetchedData>) (List<?>) books,
                        (List<FetchedData>) (List<?>) localBooks);
                ModelChanges.applyChanges(changes, bookDao, true);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Book> books) {
                return bookRateLimit.shouldFetch(BOOK_LIST_KEY);
            }

            //@NonNull
            @Override
            protected Flowable<List<Book>> loadFromDb() {
                System.out.println("1 loadFromDb ...");
                return bookDao.loadAll();
            }

            @NonNull
            @Override
            protected Flowable<List<Book>> createCall() {
                return bookAPI.listAllBooks();
            }

        }.asFlowable();
    }


    public Flowable<BookDetail> loadBookDetail(String bookId) {
        return new NetworkBoundResource<BookDetail>() {

            @Override
            protected void saveCallResult(BookDetail book, BookDetail localBook) {
                System.out.println("2 saveCallResult ...");

                ModelChanges.saveIfNeeded(book, localBook, bookDao);

                List<Chap> chaps = book.getChaps();
                if (chaps == null) {
                    return;
                }

                String bookId = book.getId();
                for (Chap chap : chaps) {
                    chap.setBookId(bookId);
                }

                List<Chap> localChaps = null;
                if (localBook != null) {
                    localChaps = localBook.getChaps();
                }

                ModelChanges.Changes changes = ModelChanges.evaluateChanges(
                        (List<FetchedData>) (List<?>) chaps,
                        (List<FetchedData>) (List<?>) localChaps);
                ModelChanges.applyChanges(changes, chapDao, true);
            }

            @Override
            protected boolean shouldFetch(@Nullable BookDetail book) {
                String key = BOOK_KEY_PREFIX + bookId;
                return bookRateLimit.shouldFetch(key);
            }

            @Override
            protected Flowable<BookDetail> loadFromDb() {
                System.out.println("2 loadFromDb ...");
                return bookDao.loadDetail(bookId);
            }

            @NonNull
            @Override
            protected Flowable<BookDetail> createCall() {
                return bookAPI.getBookDetail(bookId);
            }
        }.asFlowable();
    }

}
