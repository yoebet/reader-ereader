package wjy.yo.ereader.entity.anno;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;

@Entity(tableName = "anno_group")
public class AnnoGroup extends FetchedData implements Ordered {

    protected String name;

    protected String nameEn;

    protected String dataName;

    protected String tagName;

    protected String cssClass;

    protected long no;

    @ForeignKey(entity = AnnoFamily.class, parentColumns = "id", childColumns = "familyId", onDelete = ForeignKey.CASCADE)
    private String familyId;

    public String getName() {
        return this.name;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public String getDataName() {
        return this.dataName;
    }

    public String getTagName() {
        return this.tagName;
    }

    public String getCssClass() {
        return this.cssClass;
    }

    public long getNo() {
        return this.no;
    }

    public String getFamilyId() {
        return this.familyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

}
