package wjy.yo.ereader.entity.anno;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;

@Entity(tableName = "anno_group")
@Data
@EqualsAndHashCode(callSuper = true)
public class AnnoGroup extends FetchedData implements Ordered {

    private String name;

    private String nameEn;

    private String dataName;

    private String tagName;

    private String cssClass;

    private long no;

    @ForeignKey(entity = AnnoFamily.class, parentColumns = "id", childColumns = "familyId", onDelete = ForeignKey.CASCADE)
    private String familyId;

}
