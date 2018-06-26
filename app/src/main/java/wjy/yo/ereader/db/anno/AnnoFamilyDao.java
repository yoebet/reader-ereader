package wjy.yo.ereader.db.anno;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import io.reactivex.Maybe;
import wjy.yo.ereader.db.BaseDao;
import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entityvo.anno.AnnotationFamily;

@Dao
public interface AnnoFamilyDao extends BaseDao<AnnoFamily> {

    @Query("DELETE FROM anno_family WHERE id = :id")
    void delete(String id);

    @Transaction
    @Query("SELECT * FROM anno_family WHERE id = :id")
    Maybe<AnnotationFamily> loadAnnotations(String id);
}
