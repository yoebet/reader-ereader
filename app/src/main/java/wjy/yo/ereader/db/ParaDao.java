package wjy.yo.ereader.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.model.IdVersion;
import wjy.yo.ereader.model.Para;

@Dao
public interface ParaDao extends BaseDao<Para> {

    @Query("SELECT * FROM para where chapId = :chapId")
    LiveData<List<Para>> loadParas(String chapId);

    @Query("DELETE FROM para where chapId = :chapId")
    int deleteChapParas(String chapId);

    @Query("DELETE FROM para where _id = :id")
    void delete(String id);

    @Query("SELECT * FROM para WHERE _id = :id")
    LiveData<Para> load(String id);


    @Query("SELECT _id,_version FROM para where chapId = :chapId")
    List<IdVersion> loadIdVersions(String chapId);

    @Query("SELECT _id,_version FROM para WHERE _id = :id")
    IdVersion loadIdVersion(String id);
}
