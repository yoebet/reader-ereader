package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.BookDao;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.db.userdata.UserBookDao;
import wjy.yo.ereader.db.userdata.UserChapDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.userdata.UserBook;
import wjy.yo.ereader.entity.userdata.UserChap;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.util.Utils;

import static wjy.yo.ereader.util.RateLimiter.RequestFailOrNoDataRetryRateLimit;
import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_BOOK_CHAPS;
import static wjy.yo.ereader.util.Constants.DSR_DIRECTION_DOWN;

@Singleton
public class BookServiceImpl extends UserDataService implements BookService {

    private static final String BOOK_KEY_PREFIX = "BOOK_";

    private DB db;
    private BookDao bookDao;
    private ChapDao chapDao;
    private UserBookDao userBookDao;
    private UserChapDao userChapDao;

    @Inject
    BookAPI bookAPI;

    @Inject
    LocalSettingService settingService;

    @Inject
    BookServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.db = db;
        this.bookDao = db.bookDao();
        this.chapDao = db.chapDao();
        this.userBookDao = db.userBookDao();
        this.userChapDao = db.userChapDao();
        observeUserChange();
    }


    public Maybe<Book> loadBook(String bookId) {
        return bookDao.load(bookId);
    }

    public Maybe<Chap> loadChap(String chapId) {
        return chapDao.load(chapId);
    }

    private void saveBookChaps(BookDetail book, BookDetail localBook) {
        System.out.println("saveBookChaps ...");

        Date now = new Date();
        book.setLastFetchAt(now);
        book.setChapsLastFetchAt(now);
        if (localBook == null) {
            bookDao.insert(book);
            System.out.println("inserted: " + book);
        } else {
            bookDao.update(book);
            System.out.println("updated: " + book);
        }

        List<Chap> chaps = (book.getChaps() == null) ? new ArrayList<>(0) : book.getChaps();

        for (Chap chap : chaps) {
            chap.setBookId(book.getId());
        }

        List<Chap> localChaps;
        if (localBook == null || localBook.getChaps() == null) {
            localChaps = new ArrayList<>(0);
        } else {
            localChaps = localBook.getChaps();
        }

        db.runInTransaction(() ->
                Utils.updateData(
                        chaps, localChaps, chapDao,
                        true,
                        (remote, local) -> remote.setParasLastFetchAt(local.getParasLastFetchAt()))
        );
    }

    @SuppressLint("CheckResult")
    private void doFetchBookDetail(FlowableEmitter<BookDetail> emitter, String bookId, BookDetail localBook) {
        bookAPI.getBookDetail(bookId).subscribe(
                (BookDetail book) -> {
                    emitter.onNext(book);
                    saveBookChaps(book, localBook);
                },
                Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void fetchBookDetail(FlowableEmitter<BookDetail> emitter, String bookId, BookDetail localBook) {
        if (localBook == null) {
            doFetchBookDetail(emitter, bookId, null);
            return;
        }
        bookAPI.getChapVersions(bookId).subscribe(
                (List<FetchedData> fds) -> {
                    List<Chap> chaps = localBook.getChaps();
                    if (Utils.versionEquals(fds, chaps)) {
                        System.out.println("Chaps Version Not Change.");

                        Date now = new Date();
                        localBook.setChapsLastFetchAt(now);
                        bookDao.update(localBook);
                        return;
                    }
                    doFetchBookDetail(emitter, bookId, localBook);
                },
                Throwable::printStackTrace);
    }

    private Maybe<BookDetail> loadBookDetailFromDB(String bookId) {
        return bookDao.loadDetail(bookId).map((BookDetail book) -> {
            List<Chap> chaps = book.getChaps();
            if (chaps != null && chaps.size() > 1) {
                Collections.sort(chaps, Ordered.Comparator);
            }
            return book;
        });
    }


    public Flowable<BookDetail> loadBookDetail(String bookId) {

        return Flowable.<BookDetail>create(emitter -> {
            loadBookDetailFromDB(bookId).subscribe(
                    (BookDetail localBook) -> {
                        emitter.onNext(localBook);
                        if (settingService.isOffline()) {
                            return;
                        }
                        if (localBook.getChaps() == null || localBook.getChaps().size() == 0) {
                            String key = BOOK_KEY_PREFIX + bookId;
                            if (!RequestFailOrNoDataRetryRateLimit.shouldFetch(key)) {
                                return;
                            }
                        }
                        DataSyncRecord dsr = dataSyncService.getCommonDataSyncRecord(
                                DSR_CATEGORY_BOOK_CHAPS, DSR_DIRECTION_DOWN);
                        Date cslf = localBook.getChapsLastFetchAt();
                        if (!dataSyncService.checkTimeout(dsr, cslf)) {
                            return;
                        }

                        fetchBookDetail(emitter, bookId, localBook);
                    },
                    Throwable::printStackTrace,
                    () -> fetchBookDetail(emitter, bookId, null));
        }, BackpressureStrategy.LATEST)
                .distinctUntilChanged();
    }


    private Single<List<UserChap>> getUserChaps(String bookId) {

        if (userName == null) {
            return Single.just(new ArrayList<>(0));
        }
        return userChapDao.loadChaps(userName, bookId);
    }

    public Flowable<BookDetail> loadBookWithUserChaps(String bookId) {
        if (userName == null) {
            return loadBookDetail(bookId);
        }
        UserBook nullUserBook = new UserBook();
        return Flowable.combineLatest(
                loadBookDetail(bookId),
                userBookDao.load(userName, bookId).toSingle(nullUserBook).toFlowable(),
                getUserChaps(bookId).toFlowable(),
                (bookDetail, userBook, userChaps) -> {
                    if (userBook != nullUserBook) {
                        bookDetail.setUserBook(userBook);
                    }
                    List<Chap> chaps = bookDetail.getChaps();
                    if (chaps == null || chaps.size() == 0 || userChaps.size() == 0) {
                        return bookDetail;
                    }
                    Map<String, Chap> chapsMap = Utils.collectIdMap(chaps);
                    for (UserChap uc : userChaps) {
                        Chap chap = chapsMap.get(uc.getChapId());
                        if (chap == null) {
                            System.out.println("Chap Not Found: " + uc.getChapId());
                            continue;
                        }
                        chap.setUserChap(uc);
                    }
                    return bookDetail;
                });
    }

}
