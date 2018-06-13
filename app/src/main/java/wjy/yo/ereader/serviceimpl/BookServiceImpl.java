package wjy.yo.ereader.serviceimpl;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.BookDao;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.db.book.ParaDao;
import wjy.yo.ereader.entity.BaseModel;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.entityvo.book.ChapDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.serviceimpl.common.ModelChanges;
import wjy.yo.ereader.serviceimpl.common.NetworkBoundResource;
import wjy.yo.ereader.serviceimpl.common.RateLimiter;
import wjy.yo.ereader.util.AppExecutors;

@Singleton
public class BookServiceImpl implements BookService {

    private static final String BOOK_LIST_KEY = "BOOK_LIST";
    private static final String BOOK_KEY_PREFIX = "BOOK_";
    private static final String CHAP_KEY_PREFIX = "CHAP_";

    private DB db;
    private BookDao bookDao;
    private ChapDao chapDao;
    private ParaDao paraDao;
    private BookAPI bookAPI;


    private final AppExecutors appExecutors;

    private RateLimiter<String> bookRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    BookServiceImpl(DB db, BookAPI bookAPI, AppExecutors appExecutors) {
        this.db = db;
        this.bookDao = db.bookDao();
        this.chapDao = db.chapDao();
        this.paraDao = db.paraDao();
        this.bookAPI = bookAPI;
        this.appExecutors = appExecutors;
        System.out.println("new BookServiceImpl");
    }

    public LiveData<List<Book>> loadBooks() {
        return new NetworkBoundResource<List<Book>>(appExecutors) {
            @Override
            protected void saveCallResult(List<Book> books, List<Book> localBooks) {
                System.out.println("1 saveCallResult ...");

                ModelChanges.Changes changes = ModelChanges.evaluateChanges(
                        (List<BaseModel>) (List<?>) books,
                        (List<BaseModel>) (List<?>) localBooks);
                ModelChanges.applyChanges(changes, bookDao, true);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Book> books) {
                if (books == null) {
                    bookRateLimit.touch(BOOK_LIST_KEY);
                    return true;
                }
                return bookRateLimit.shouldFetch(BOOK_LIST_KEY);
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
                return bookAPI.listAllBooks();
            }

        }.asLiveData();
    }


    public LiveData<BookDetail> loadBookDetail(String bookId) {
        return new NetworkBoundResource<BookDetail>(appExecutors) {

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
                        (List<BaseModel>) (List<?>) chaps,
                        (List<BaseModel>) (List<?>) localChaps);
                ModelChanges.applyChanges(changes, chapDao, true);
            }

            @Override
            protected boolean shouldFetch(@Nullable BookDetail book) {
                if (book == null) {
                    return true;
                }
                List<Chap> chaps = book.getChaps();
                String key = BOOK_KEY_PREFIX + book.getId();
                if (chaps == null) {
                    bookRateLimit.touch(key);
                    return true;
                }
                return bookRateLimit.shouldFetch(key);
            }

            @Override
            protected LiveData<BookDetail> loadFromDb() {
                System.out.println("2 loadFromDb ...");
                return bookDao.loadDetail(bookId);
            }

            @NonNull
            @Override
            protected LiveData<BookDetail> createCall() {
                return bookAPI.getBookDetail(bookId);
            }
        }.asLiveData();
    }


    public LiveData<ChapDetail> loadChapDetail(String chapId) {
        return new NetworkBoundResource<ChapDetail>(appExecutors) {
            @Override
            protected void saveCallResult(ChapDetail chap, ChapDetail localChap) {
                System.out.println("3 saveCallResult ...");

                ModelChanges.saveIfNeeded(chap, localChap, chapDao);

                List<Para> paras = chap.getParas();
                if (paras == null) {
                    return;
                }

                String chapId = chap.getId();
                for (Para para : paras) {
                    para.setBookId(chap.getBookId());
                    para.setChapId(chapId);
                }

                List<Para> localParas = null;
                if (localChap != null) {
                    localParas = localChap.getParas();
                }

                ModelChanges.Changes changes = ModelChanges.evaluateChanges(
                        (List<BaseModel>) (List<?>) paras,
                        (List<BaseModel>) (List<?>) localParas);
                ModelChanges.applyChanges(changes, paraDao, true);
            }

            @Override
            protected boolean shouldFetch(@Nullable ChapDetail chap) {
                if (chap == null) {
                    return true;
                }
                List<Para> paras = chap.getParas();
                String key = CHAP_KEY_PREFIX + chap.getId();
                if (paras == null) {
                    bookRateLimit.touch(key);
                    return true;
                }
                return bookRateLimit.shouldFetch(key);
            }

            @Override
            protected LiveData<ChapDetail> loadFromDb() {
                System.out.println("3 loadFromDb ...");
                return chapDao.loadDetail(chapId);
            }

            @NonNull
            @Override
            protected LiveData<ChapDetail> createCall() {
                return bookAPI.getChapDetail(chapId);
            }
        }.asLiveData();
    }
}
