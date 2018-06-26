package wjy.yo.ereader.entity.anno;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;

@Entity(tableName = "annotation")
@Data
@NoArgsConstructor
public class Anno implements Ordered {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    private String name;

    private String nameEn;

    private String dataValue;

    @ForeignKey(entity = AnnoGroup.class, parentColumns = "id", childColumns = "groupId", onDelete = ForeignKey.CASCADE)
    private String groupId;

    private long no;

    @Ignore
    @EqualsAndHashCode.Exclude
    private AnnoGroup group;

    @Override
    public String toString() {
        return "\t\t" + nameEn + " " + name + " " + dataValue + "\n";
    }
}
