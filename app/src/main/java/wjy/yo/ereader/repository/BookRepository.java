package wjy.yo.ereader.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.db.BookDao;
import wjy.yo.ereader.db.ChapDao;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.ParaDao;
import wjy.yo.ereader.model.BaseModel;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.model.IdVersion;
import wjy.yo.ereader.model.Para;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.util.AppExecutors;

@Singleton
public class BookRepository {
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
    BookRepository(DB db, BookAPI bookAPI, AppExecutors appExecutors) {
        this.db = db;
        this.bookDao = db.bookDao();
        this.chapDao = db.chapDao();
        this.paraDao = db.paraDao();
        this.bookAPI = bookAPI;
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
                return data == null || data.isEmpty() || bookRateLimit.shouldFetch(BOOK_LIST_KEY);
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
                return bookAPI.listAllBooks2();
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
                return book == null || book.getChaps() == null || bookRateLimit.shouldFetch(BOOK_KEY_PREFIX + book.getId());
            }

            @Override
            LiveData<Book> loadFromDb() {
                System.out.println("2 loadFromDb ...");
                return loadBookDetailFromDb(bookId);
            }

            @NonNull
            @Override
            LiveData<Book> createCall() {
                return bookAPI.getBookWithChaps(bookId);
            }
        }.asLiveData();
    }


    public LiveData<Chap> loadChapDetail(String chapId) {
        return new NetworkBoundResource<Chap>(appExecutors) {
            @Override
            void saveCallResult(Chap chap) {
                System.out.println("3 saveCallResult ...");

                ModelChanges.saveIfNeeded(chap, chapDao);

                List<Para> paras = chap.getParas();
                if (paras == null) {
                    return;
                }

                String chapId = chap.getId();
                for (Para para : paras) {
                    para.setBookId(chap.getBookId());
                    para.setChapId(chapId);
                }

                List<IdVersion> ivs = paraDao.loadIdVersions(chapId);

                ModelChanges.Changes changes = ModelChanges.evaluateChanges((List<BaseModel>) (List<?>) paras, ivs);
                ModelChanges.applyChanges(changes, paraDao, true);
            }

            @Override
            boolean shouldFetch(@Nullable Chap chap) {
                return chap == null || chap.getParas() == null || bookRateLimit.shouldFetch(CHAP_KEY_PREFIX + chap.getId());
            }

            @Override
            LiveData<Chap> loadFromDb() {
                System.out.println("3 loadFromDb ...");
                return loadChapDetailFromDb(chapId);
            }

            @NonNull
            @Override
            LiveData<Chap> createCall() {
                return bookAPI.getChapWithParas(chapId);
            }
        }.asLiveData();
    }

    LiveData<Book> loadBookDetailFromDb(String bookId) {
        LiveData<Book> book = bookDao.load(bookId);
        LiveData<List<Chap>> chaps = chapDao.loadChaps(bookId);
        return new OneToManyLiveData<>(book, chaps, Book::setChaps);
    }

    LiveData<Chap> loadChapDetailFromDb(String chapId) {
        LiveData<Chap> chap = chapDao.load(chapId);
        LiveData<List<Para>> paras = paraDao.loadParas(chapId);
        return new OneToManyLiveData<>(chap, paras, Chap::setParas);
    }

}
