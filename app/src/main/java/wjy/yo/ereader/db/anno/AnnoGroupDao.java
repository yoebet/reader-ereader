package wjy.yo.ereader.db.anno;

import android.arch.persistence.room.Dao;

import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.anno.AnnoGroup;

@Dao
public interface AnnoGroupDao extends BaseDao<AnnoGroup> {

//    @Query("DELETE FROM anno_group WHERE id = :id")
//    void delete(String id);

//    @Transaction
//    @Query("SELECT * FROM anno_group WHERE id = :id")
//    Maybe<AnnotationGroup> loadAnnotationGroups(String id);
}
