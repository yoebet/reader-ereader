package wjy.yo.ereader.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by wsx on 2018/1/30.
 */

@Entity
public class Chap  extends BaseModel {
    @ForeignKey(entity = Book.class, parentColumns = "_id", childColumns = "bookId", onDelete = ForeignKey.CASCADE)
    private String bookId;
    private String name;
    private String zhName;

    @Ignore
    private List<Para> paras;

    public Chap(@NonNull String id, String name, String zhName) {
        this._id = id;
        this.name = name;
        this.zhName = zhName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
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

    public List<Para> getParas() {
        return paras;
    }

    public void setParas(List<Para> paras) {
        this.paras = paras;
    }

    @Override
    public String toString() {
        return _id + " " + name;
    }
}
