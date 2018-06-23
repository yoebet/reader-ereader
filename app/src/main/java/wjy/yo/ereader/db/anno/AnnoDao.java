package wjy.yo.ereader.db.anno;

import android.arch.persistence.room.Dao;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.anno.Anno;
import wjy.yo.ereader.entity.anno.AnnoGroup;

@Dao
public interface AnnoDao extends BaseDao<Anno> {

//    @Query("DELETE FROM annotation WHERE _id = :id")
//    void delete(String id);

}
