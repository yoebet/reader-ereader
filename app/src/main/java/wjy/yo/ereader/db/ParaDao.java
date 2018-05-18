package wjy.yo.ereader.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.model.IdVersion;
import wjy.yo.ereader.model.Para;

@Dao
public interface ParaDao extends BaseDao<Para> {

    @Query("SELECT * FROM para WHERE chapId = :chapId ORDER BY `no`")
    LiveData<List<Para>> loadParas(String chapId);

    @Query("DELETE FROM para WHERE chapId = :chapId")
    int deleteChapParas(String chapId);

    @Query("DELETE FROM para WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM para WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT * FROM para WHERE _id = :id")
    LiveData<Para> load(String id);


    @Query("SELECT _id,_version FROM para WHERE chapId = :chapId")
    List<IdVersion> loadIdVersions(String chapId);

    @Query("SELECT _id,_version FROM para WHERE _id = :id")
    IdVersion loadIdVersion(String id);
}
