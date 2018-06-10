package wjy.yo.ereader.db.book;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entityvo.IdVersion;

@Dao
public interface BookDao extends BaseDao<Book> {

    @Query("DELETE FROM book WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM book WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,_version FROM book WHERE _id = :id")
    IdVersion loadIdVersion(String id);

    @Query("SELECT _id,_version FROM book")
    List<IdVersion> loadAllIdVersion();

    @Query("SELECT * FROM book")
    LiveData<List<Book>> loadAll();

    @Query("SELECT * FROM book WHERE _id = :id")
    LiveData<Book> load(String id);
}
