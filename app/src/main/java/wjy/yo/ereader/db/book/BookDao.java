package wjy.yo.ereader.db.book;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.IdVersion;
import wjy.yo.ereader.entityvo.book.BookDetail;

@Dao
public interface BookDao extends BaseDao<Book> {

    @Query("SELECT * FROM book")
    LiveData<List<Book>> loadAll();

    @Query("SELECT * FROM book WHERE _id = :id")
    LiveData<Book> load(String id);

    @Transaction
    @Query("SELECT * FROM book WHERE _id = :id")
    LiveData<BookDetail> loadDetail(String id);
}
