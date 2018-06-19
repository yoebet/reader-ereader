package wjy.yo.ereader.entity.book;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Date;

import wjy.yo.ereader.entity.FetchedData;

/**
 * Created by wsx on 2018/1/30.
 */

@Entity(tableName = "book_chap")
public class Chap extends FetchedData {

    @NonNull
    @ForeignKey(entity = Book.class, parentColumns = "_id", childColumns = "bookId")
    private String bookId;

    private String name;

    private String zhName;

    private long no;

    private Date parasLastFetchAt;

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull String bookId) {
        this.bookId = bookId;
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

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public Date getParasLastFetchAt() {
        return parasLastFetchAt;
    }

    public void setParasLastFetchAt(Date parasLastFetchAt) {
        this.parasLastFetchAt = parasLastFetchAt;
    }

    @Override
    public String toString() {
        return _id + " " + name;
    }
}
