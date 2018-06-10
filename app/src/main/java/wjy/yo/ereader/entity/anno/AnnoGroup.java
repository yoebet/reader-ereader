package wjy.yo.ereader.entity.anno;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import wjy.yo.ereader.entity.BaseModel;

@Entity(tableName = "anno_group")
public class AnnoGroup extends BaseModel {

    private String name;

    private String nameEn;

    private String dataName;

    private String tagName;

    private String cssClass;

    private Integer no;

    @ForeignKey(entity = AnnoFamily.class, parentColumns = "_id", childColumns = "familyId", onDelete = ForeignKey.CASCADE)
    private String familyId;

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

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }
}
