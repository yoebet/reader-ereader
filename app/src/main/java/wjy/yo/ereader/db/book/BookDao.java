package wjy.yo.ereader.db.book;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.book.BookDetail;

@Dao
public interface BookDao extends BaseDao<Book> {

    @Query("SELECT * FROM book")
    Flowable<List<Book>> loadAll();

//    @Query("DELETE FROM book")
//    int deleteAll();

    @Query("SELECT * FROM book WHERE id = :id")
    Maybe<Book> load(String id);

    @Transaction
    @Query("SELECT * FROM book WHERE id = :id")
    Maybe<BookDetail> loadDetail(String id);
}
