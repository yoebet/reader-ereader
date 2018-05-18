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
public interface ChapDao extends BaseDao<Chap> {

    @Query("SELECT * FROM chap WHERE bookId = :bookId")
    LiveData<List<Chap>> loadChaps(String bookId);

    @Query("DELETE FROM chap WHERE bookId = :bookId")
    int deleteBookChaps(String bookId);

    @Query("DELETE FROM chap WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM chap WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT * FROM chap WHERE _id = :id")
    LiveData<Chap> load(String id);


    @Query("SELECT _id,_version FROM chap WHERE bookId = :bookId")
    List<IdVersion> loadIdVersions(String bookId);

    @Query("SELECT _id,_version FROM chap WHERE _id = :id")
    IdVersion loadIdVersion(String id);
}
