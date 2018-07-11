package wjy.yo.ereader.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.BookDao;
import wjy.yo.ereader.db.userdata.UserBookDao;
import wjy.yo.ereader.db.userdata.UserChapDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.userdata.UserBook;
import wjy.yo.ereader.entity.userdata.UserChap;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.remote.UserBookAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookListService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.util.Utils;

import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_BOOK_LIST;
import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_USER_BOOKS;
import static wjy.yo.ereader.util.Constants.DSR_DIRECTION_DOWN;
import static wjy.yo.ereader.util.RateLimiter.RequestFailOrNoDataRetryRateLimit;

@Singleton
public class BookListServiceImpl extends UserDataService implements BookListService {

    private static final String BOOK_LIST_KEY = "BOOK_LIST";

    private DB db;
    private BookDao bookDao;
    private UserBookDao userBookDao;
    private UserChapDao userChapDao;

    @Inject
    BookAPI bookAPI;

    @Inject
    UserBookAPI userBookAPI;

    @Inject
    LocalSettingService settingService;

    @Inject
    BookListServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.db = db;
        this.bookDao = db.bookDao();
        this.userBookDao = db.userBookDao();
        this.userChapDao = db.userChapDao();
        observeUserChange();
    }

    private void saveUserBooks(List<UserBook> newData, List<UserBook> localData) {
        System.out.println("saveUserBooks ...");

        db.runInTransaction(() -> {
            Set<String> keepIds = Utils.updateData(newData, localData, userBookDao, true);
            for (UserBook ub : newData) {
                String bookId = ub.getBookId();
                if (keepIds.contains(bookId)) {
                    continue;
                }
                userChapDao.deleteChaps(userName, bookId);
                List<UserChap> chaps = ub.getChaps();
                if (chaps != null) {
                    for (UserChap uc : chaps) {
                        userChapDao.insert(uc);
                    }
                }
            }
        });
    }

    private Flowable<List<UserBook>> getUserBooks() {
        if (userName == null) {
            return Flowable.just(new ArrayList<>(0));
        }
        return Flowable.<List<UserBook>>create(emitter -> {
            userBookDao.loadUserBooks(userName).subscribe(localData -> {

                emitter.onNext(localData);

                DataSyncRecord dsr = dataSyncService.getUserDataSyncRecord(userName,
                        DSR_CATEGORY_USER_BOOKS, DSR_DIRECTION_DOWN);
                if (!dsr.isStale() && !dataSyncService.checkTimeout(dsr)) {
                    return;
                }

                userBookAPI.getMyBooks()
                        .map(myBooks -> {
                            for (UserBook ub : myBooks) {
                                ub.setUserName(userName);
                                List<UserChap> chaps = ub.getChaps();
                                if (chaps != null) {
                                    String bookId = ub.getBookId();
                                    for (UserChap uc : chaps) {
                                        uc.setUserName(userName);
                                        uc.setBookId(bookId);
                                    }
                                }
                            }
                            return myBooks;
                        })
                        .subscribe(
                                myBooks -> {
                                    emitter.onNext(myBooks);
                                    dataSyncService.renewSyncRecord(dsr);
                                    saveUserBooks(myBooks, localData);
                                },
                                Throwable::printStackTrace);
            });
        }, BackpressureStrategy.LATEST)
                .distinctUntilChanged();
    }

    public Flowable<List<Book>> loadBooks() {

        return Flowable.<List<Book>>create(emitter -> {

            bookDao.loadAll().subscribe(localBooks -> {

                emitter.onNext(localBooks);

                if (settingService.isOffline()) {
                    return;
                }
                if (localBooks.size() == 0) {
                    boolean fetch = RequestFailOrNoDataRetryRateLimit.shouldFetch(BOOK_LIST_KEY);
                    if (!fetch) {
                        return;
                    }
                }
                DataSyncRecord dsr = dataSyncService.getUserDataSyncRecord(userName,
                        DSR_CATEGORY_BOOK_LIST, DSR_DIRECTION_DOWN);
                if (localBooks.size() > 0 &&
                        !dsr.isStale() && !dataSyncService.checkTimeout(dsr)) {
                    return;
                }

                bookAPI.listAllBooks().subscribe(
                        books -> {
                            dataSyncService.renewSyncRecord(dsr);
                            if (Objects.equals(books, localBooks)) {
                                return;
                            }
                            emitter.onNext(books);

                            db.runInTransaction(() ->
                                    Utils.updateData(
                                            books, localBooks, bookDao,
                                            true,
                                            (remote, local) -> remote.setChapsLastFetchAt(local.getChapsLastFetchAt()))
                            );
                        },
                        Throwable::printStackTrace);
            });
        }, BackpressureStrategy.LATEST)
                .distinctUntilChanged();
    }


    public Flowable<List<Book>> loadBooksWithUserBook() {
        if (userName == null) {
            return loadBooks();
        }
        return Flowable.combineLatest(
                loadBooks(),
                getUserBooks(),
                (books, userBooks) -> {
                    if (userBooks.size() == 0) {
                        return books;
                    }
                    Map<String, Book> booksMap = Utils.collectIdMap(books);
                    for (UserBook ub : userBooks) {
                        Book book = booksMap.get(ub.getBookId());
                        if (book == null) {
                            System.out.println("Book Not Found: " + ub.getBookId());
                            continue;
                        }
                        book.setUserBook(ub);
                    }
                    return books;
                });
    }


}
