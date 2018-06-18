package wjy.yo.ereader.db.anno;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.IdVersion;
import wjy.yo.ereader.entity.anno.AnnoFamily;

@Dao
public interface AnnoFamilyDao extends BaseDao<AnnoFamily> {

    @Query("DELETE FROM anno_family WHERE _id = :id")
    void delete(String id);

    @Query("DELETE FROM anno_family WHERE _id in (:ids)")
    void deleteByIds(List<String> ids);

    @Query("SELECT _id,version FROM anno_family WHERE _id = :id")
    IdVersion loadIdVersion(String id);
}
