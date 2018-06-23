package wjy.yo.ereader.db.anno;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import io.reactivex.Maybe;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.IdVersion;
import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entity.anno.AnnoGroup;
import wjy.yo.ereader.entityvo.anno.AnnotationFamily;
import wjy.yo.ereader.entityvo.anno.AnnotationGroup;

@Dao
public interface AnnoGroupDao extends BaseDao<AnnoGroup> {

//    @Query("DELETE FROM anno_group WHERE _id = :id")
//    void delete(String id);

//    @Transaction
//    @Query("SELECT * FROM anno_group WHERE _id = :id")
//    Maybe<AnnotationGroup> loadAnnotationGroups(String id);
}
