package wjy.yo.ereader.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;
import wjy.yo.ereader.model.BaseModel;
import wjy.yo.ereader.model.IdVersion;

import java.util.List;

public interface BaseDao<M extends BaseModel> {

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

//    LiveData<M> load(String id);
//
//    M loadSync(String id);
//
//    IdVersion loadIdVersion(String id);
}
