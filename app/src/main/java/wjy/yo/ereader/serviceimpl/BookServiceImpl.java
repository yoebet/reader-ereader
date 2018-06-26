package wjy.yo.ereader.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.BookDao;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.remote.UserBookAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;

import static wjy.yo.ereader.util.RateLimiter.RequestFailOrNoDataRetryRateLimit;
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
    UserBookAPI userBookAPI;

    @Inject
    LocalSettingService settingService;

    @Inject
    BookServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.db = db;
        this.bookDao = db.bookDao();
        this.chapDao = db.chapDao();
        observeUserChange();
    }

    private void saveBooks(DataSyncRecord dsr, List<Book> books, List<Book> localBooks) {
        System.out.println("1 saveCallResult ...");

        if (Objects.equals(books, localBooks)) {
            return;
        }
        Date now = new Date();
        if (dsr != null) {
            dsr.setLastSyncAt(now);
            dsr.setStale(false);
            dataSyncService.saveDataSyncRecord(dsr);
        }

        bookDao.deleteAll();
        for (Book b : books) {
            b.setLastFetchAt(now);
            bookDao.insert(b);
        }
    }

    public Flowable<List<Book>> loadBooks() {

        return Flowable.create(emitter -> {

            bookDao.loadAll().subscribe(localBooks -> {
                if (settingService.isOffline()) {
                    emitter.onNext(localBooks);
                    return;
                }
                if (localBooks.size() == 0) {
                    boolean fetch = RequestFailOrNoDataRetryRateLimit.shouldFetch(BOOK_LIST_KEY);
                    if (!fetch) {
                        emitter.onNext(localBooks);
                        return;
                    }
                }
                DataSyncRecord dsr = dataSyncService.getUserDataSyncRecord(userName,
                        DSR_CATEGORY_BOOK_LIST, DSR_DIRECTION_DOWN);
                if (!dsr.isStale() && !dataSyncService.checkTimeout(dsr)) {
                    emitter.onNext(localBooks);
                    return;
                }

                bookAPI.listAllBooks().subscribe(
                        books -> {
                            emitter.onNext(books);
                            saveBooks(dsr, books, localBooks);
                        },
                        e -> {
                            e.printStackTrace();
                            emitter.onNext(localBooks);
                        });
            });
        }, BackpressureStrategy.LATEST);
    }


    private void saveBook(BookDetail book, BookDetail localBook) {
        System.out.println("2 saveCallResult ...");

        if (Objects.equals(book, localBook)) {
            return;
        }
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

        chapDao.deleteBookChaps(book.getId());
        for (Chap chap : chaps) {
            chap.setBookId(book.getId());
            chap.setLastFetchAt(now);
            chapDao.insert(chap);
        }

    }


    public Flowable<BookDetail> loadBookDetail(String bookId) {

        return Flowable.create(emitter -> {
            bookDao.loadDetail(bookId).subscribe((BookDetail localBook) -> {
                if (settingService.isOffline()) {
                    emitter.onNext(localBook);
                    return;
                }
                if (localBook.getChaps() == null || localBook.getChaps().size() == 0) {
                    String key = BOOK_KEY_PREFIX + bookId;
                    if (!RequestFailOrNoDataRetryRateLimit.shouldFetch(key)) {
                        emitter.onNext(localBook);
                        return;
                    }
                }
                DataSyncRecord dsr = dataSyncService.getCommonDataSyncRecord(
                        DSR_CATEGORY_BOOK_CHAPS, DSR_DIRECTION_DOWN);
                Date cslf = localBook.getChapsLastFetchAt();
                if (!dataSyncService.checkTimeout(dsr, cslf)) {
                    emitter.onNext(localBook);
                    return;
                }

                bookAPI.getBookDetail(bookId).subscribe(
                        (BookDetail book) -> {
                            emitter.onNext(book);
                            saveBook(book, localBook);
                        },
                        e -> {
                            e.printStackTrace();
                            emitter.onNext(localBook);
                        });
            });
        }, BackpressureStrategy.LATEST);
    }

}
