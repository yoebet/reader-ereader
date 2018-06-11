package wjy.yo.ereader.entity.book;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.util.List;

import wjy.yo.ereader.entity.BaseModel;
import wjy.yo.ereader.entity.anno.AnnoFamily;

/**
 * Created by wsx on 2018/1/30.
 */

@Entity(tableName = "book")
public class Book extends BaseModel {

    private String code;

    private String name;

    private String zhName;

    private String author;

    private String zhAuthor;

    private String tags;

    @ForeignKey(entity = AnnoFamily.class, parentColumns = "_id", childColumns = "annotationFamilyId")
    private String annotationFamilyId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getZhAuthor() {
        return zhAuthor;
    }

    public void setZhAuthor(String zhAuthor) {
        this.zhAuthor = zhAuthor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAnnotationFamilyId() {
        return annotationFamilyId;
    }

    public void setAnnotationFamilyId(String annotationFamilyId) {
        this.annotationFamilyId = annotationFamilyId;
    }

    @Override
    public String toString() {
        return _id + " " + name;
    }
}
