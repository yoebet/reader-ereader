package wjy.yo.ereader.entity.book;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import java.util.Date;

import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entity.userdata.UserBook;


@Entity(tableName = "book")
public class Book extends FetchedData {

    private String code;

    private String name;

    private String zhName;

    private String author;

    private String zhAuthor;

    private String tags;

    @ForeignKey(entity = AnnoFamily.class, parentColumns = "id", childColumns = "annotationFamilyId")
    private String annotationFamilyId;

    private Date chapsLastFetchAt;

    @Ignore
    private UserBook userBook;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public Date getChapsLastFetchAt() {
        return chapsLastFetchAt;
    }

    public void setChapsLastFetchAt(Date chapsLastFetchAt) {
        this.chapsLastFetchAt = chapsLastFetchAt;
    }

    public UserBook getUserBook() {
        return userBook;
    }

    public void setUserBook(UserBook userBook) {
        this.userBook = userBook;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
