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

@Dao
public interface ChapDao  extends BaseDao<Chap> {

    @Query("SELECT * FROM chap where bookId = :bookId")
    LiveData<List<Chap>> loadChaps(String bookId);

    @Query("DELETE FROM chap where bookId = :bookId")
    int deleteBookChaps(String bookId);

    @Query("SELECT * FROM chap WHERE _id = :id")
    LiveData<Chap> load(String id);

}
