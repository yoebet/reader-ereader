package wjy.yo.ereader.db.anno;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Maybe;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.IdVersion;
import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entityvo.anno.AnnotationFamily;

@Dao
public interface AnnoFamilyDao extends BaseDao<AnnoFamily> {

    @Query("DELETE FROM anno_family WHERE _id = :id")
    void delete(String id);

    @Query("SELECT _id,version FROM anno_family WHERE _id = :id")
    IdVersion loadIdVersion(String id);

    @Transaction
    @Query("SELECT * FROM anno_family WHERE _id = :id")
    Maybe<AnnotationFamily> loadAnnotations(String id);
}
