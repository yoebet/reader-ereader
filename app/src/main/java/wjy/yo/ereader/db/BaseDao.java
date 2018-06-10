package wjy.yo.ereader.db;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

import wjy.yo.ereader.entityvo.IdVersion;

public interface BaseDao<M> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(M ms);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<M> ms);

    @Update
    void update(M ms);

    @Update
    void update(List<M> ms);

    @Delete
    void delete(M ms);

    @Delete
    void delete(List<M> ms);


    void delete(String id);

    void deleteByIds(List<String> ids);

//    List<IdVersion> loadAllIdVersion();

    IdVersion loadIdVersion(String id);
}
