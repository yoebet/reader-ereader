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

    private void saveUserBooks(DataSyncRecord dsr, List<UserBook> newData, List<UserBook> localData) {
        System.out.println("saveUserBooks ...");

        db.runInTransaction(() -> {
            if (dsr != null) {
                dataSyncService.renewSyncRecord(dsr);
            }

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

    Single<List<UserBook>> getUserBooks() {
        if (userName == null) {
            return Single.just(new ArrayList<>(0));
        }
        return Single.create(emitter -> {
            userBookDao.loadUserBooks(userName).subscribe(localData -> {

                DataSyncRecord dsr = dataSyncService.getUserDataSyncRecord(userName,
                        DSR_CATEGORY_USER_BOOKS, DSR_DIRECTION_DOWN);
                if (!dsr.isStale() && !dataSyncService.checkTimeout(dsr)) {
                    emitter.onSuccess(localData);
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
                                    saveUserBooks(dsr, myBooks, localData);
                                    emitter.onSuccess(myBooks);
                                },
                                e -> {
                                    e.printStackTrace();
                                    emitter.onSuccess(localData);
                                });
            });
        });
    }


    private void saveBooks(DataSyncRecord dsr, List<Book> books, List<Book> localBooks) {
        System.out.println("saveBooks ...");

        if (dsr != null) {
            dataSyncService.renewSyncRecord(dsr);
        }

        if (Objects.equals(books, localBooks)) {
            return;
        }

        db.runInTransaction(() ->
                Utils.updateData(
                        books, localBooks, bookDao,
                        true,
                        (remote, local) -> remote.setChapsLastFetchAt(local.getChapsLastFetchAt()))
        );
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


    public Flowable<List<Book>> loadBooksWithUserBook() {
        if (userName == null) {
            return loadBooks();
        }
        return Flowable.combineLatest(
                loadBooks(),
                getUserBooks().toFlowable(),
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
