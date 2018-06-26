package wjy.yo.ereader.entity.book;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entity.userdata.UserBook;

/**
 * Created by wsx on 2018/1/30.
 */

@Entity(tableName = "book")
@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends FetchedData {

    private String code;

    private String name;

    private String zhName;

    private String author;

    private String zhAuthor;

    private String tags;

    @ForeignKey(entity = AnnoFamily.class, parentColumns = "id", childColumns = "annotationFamilyId")
    private String annotationFamilyId;

    @EqualsAndHashCode.Exclude
    private Date chapsLastFetchAt;

    @Ignore
    @EqualsAndHashCode.Exclude
    private UserBook userBook;


    @Override
    public String toString() {
        return id + " " + name;
    }
}
