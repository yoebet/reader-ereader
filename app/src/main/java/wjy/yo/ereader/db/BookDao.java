package wjy.yo.ereader.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.model.IdVersion;

@Dao
public interface BookDao extends BaseDao<Book> {

    @Query("SELECT * FROM book")
    LiveData<List<Book>> loadAll();

//    @Query("SELECT * FROM book")
//    List<Book> loadAllSync();

    @Query("DELETE FROM book WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM book WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT * FROM book WHERE _id = :id")
    LiveData<Book> load(String id);

//    @Query("SELECT * FROM book WHERE _id = :id")
//    Book loadSync(String id);

    @Query("SELECT _id,_version FROM book")
    List<IdVersion> loadAllIdVersion();

    @Query("SELECT _id,_version FROM book WHERE _id = :id")
    IdVersion loadIdVersion(String id);
}
