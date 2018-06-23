package wjy.yo.ereader.entity.anno;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;

@Entity(tableName = "annotation")
public class Anno implements Ordered {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    private String name;

    private String nameEn;

    private String dataValue;

    @ForeignKey(entity = AnnoGroup.class, parentColumns = "_id", childColumns = "groupId", onDelete = ForeignKey.CASCADE)
    private String groupId;

    @Ignore
    private AnnoGroup group;

    private long no;


    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getCssClass() {
        return this.group.getCssClass();
    }

    public String getTagName() {
        return this.group.getTagName();
    }

    public String getDataName() {
        return this.group.getDataName();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public AnnoGroup getGroup() {
        return group;
    }

    public void setGroup(AnnoGroup group) {
        this.group = group;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "\t\t" + nameEn + " " + name + " " + dataValue + "\n";
    }
}
