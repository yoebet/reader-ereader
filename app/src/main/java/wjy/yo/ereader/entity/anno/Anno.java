package wjy.yo.ereader.entity.anno;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import wjy.yo.ereader.entity.Ordered;

@Entity(tableName = "annotation")
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
    private AnnoGroup group;


    @NonNull
    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public String getDataValue() {
        return this.dataValue;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public long getNo() {
        return this.no;
    }

    public AnnoGroup getGroup() {
        return this.group;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public void setGroup(AnnoGroup group) {
        this.group = group;
    }


    @Override
    public String toString() {
        return "\t\t" + nameEn + " " + name + " " + dataValue + "\n";
    }

}
