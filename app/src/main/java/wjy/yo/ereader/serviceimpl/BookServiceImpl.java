package wjy.yo.ereader.serviceimpl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.BookDao;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.serviceimpl.common.ModelChanges;
import wjy.yo.ereader.serviceimpl.common.NetworkBoundResource;
import wjy.yo.ereader.serviceimpl.common.RateLimiter;

import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_BOOK_CHAPS;
import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_BOOK_LIST;
import static wjy.yo.ereader.util.Constants.DSR_DIRECTION_DOWN;

@Singleton
public class BookServiceImpl extends UserDataService implements BookService {

    private static final String BOOK_LIST_KEY = "BOOK_LIST";
    private static final String BOOK_KEY_PREFIX = "BOOK_";

    private DB db;
    private BookDao bookDao;
    private ChapDao chapDao;

    @Inject
    BookAPI bookAPI;

    @Inject
    LocalSettingService settingService;

    private RateLimiter<String> bookRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    BookServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.db = db;
        this.bookDao = db.bookDao();
        this.chapDao = db.chapDao();
        observeUserChange();
    }

    public Flowable<List<Book>> loadBooks() {

        return new NetworkBoundResource<List<Book>>("BookList") {

            DataSyncRecord dsr;

            @Override
            protected Flowable<List<Book>> loadFromDb() {
                System.out.println("1 loadFromDb ...");
                return bookDao.loadAll();
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Book> books) {
                if (settingService.isOffline()) {
                    return false;
                }
                if (!accountService.isLogin()) {
                    return false;
                }
                if (books == null || books.size() == 0) {
                    return bookRateLimit.shouldFetch(BOOK_LIST_KEY);
                }
                dsr = dataSyncService.getUserDataSyncRecord(userName,
                        DSR_CATEGORY_BOOK_LIST, DSR_DIRECTION_DOWN);
                if (dsr.isStale()) {
                    dsr.setStale(false);
                    return true;
                }
                return dataSyncService.checkTimeout(dsr);
            }

            @NonNull
            @Override
            protected Observable<List<Book>> createCall() {
                return bookAPI.listAllBooks();
            }

            @Override
            protected void saveCallResult(List<Book> books, List<Book> localBooks) {
                System.out.println("1 saveCallResult ...");

                db.runInTransaction(() -> {
                    Date now = new Date();
                    if (dsr != null) {
                        dsr.setLastSyncAt(now);
                        dataSyncService.saveDataSyncRecord(dsr);
                        dsr = null;
                    }

                    bookDao.deleteAll();
                    for (Book b : books) {
                        b.setLastFetchAt(now);
                        bookDao.insert(b);
                    }
                });

            }

        }.asFlowable();
    }


    public Flowable<BookDetail> loadBookDetail(String bookId) {
        return new NetworkBoundResource<BookDetail>("BookDetail") {

            @Override
            protected Flowable<BookDetail> loadFromDb() {
                System.out.println("2 loadFromDb ...");
                return bookDao.loadDetail(bookId);
            }

            @Override
            protected boolean shouldFetch(@Nullable BookDetail book) {
                if (settingService.isOffline()) {
                    return false;
                }
                if (!accountService.isLogin()) {
                    return false;
                }
                if (book == null || book.getChaps() == null || book.getChaps().size() == 0) {
                    String key = BOOK_KEY_PREFIX + bookId;
                    return bookRateLimit.shouldFetch(key);
                }
                DataSyncRecord dsr = dataSyncService.getCommonDataSyncRecord(
                        DSR_CATEGORY_BOOK_CHAPS, DSR_DIRECTION_DOWN);
                Date cslf = book.getChapsLastFetchAt();
                return dataSyncService.checkTimeout(dsr, cslf);
            }

            @NonNull
            @Override
            protected Observable<BookDetail> createCall() {
                return bookAPI.getBookDetail(bookId);
            }

            @Override
            protected void saveCallResult(BookDetail book, BookDetail localBook) {
                System.out.println("2 saveCallResult ...");

                db.runInTransaction(() -> {
                    Date now = new Date();
                    book.setLastFetchAt(now);
                    book.setChapsLastFetchAt(now);
                    if (localBook == null) {
                        bookDao.insert(book);
                    } else {
                        bookDao.update(book);
                    }
                    System.out.println((localBook == null) ? "inserted: " : "updated: " + book);

                    List<Chap> chaps = book.getChaps();
                    if (chaps == null) {
                        return;
                    }

                    chapDao.deleteBookChaps(bookId);
                    for (Chap chap : chaps) {
                        chap.setBookId(bookId);
                        chap.setLastFetchAt(now);
                        chapDao.insert(chap);
                    }
                });

            }
        }.asFlowable();
    }

}
