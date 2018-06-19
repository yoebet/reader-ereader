package wjy.yo.ereader.entity.book;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import wjy.yo.ereader.entity.FetchedData;

/**
 * Created by wsx on 2018/4/19.
 */

@Entity(tableName = "book_para")
public class Para extends FetchedData {

    @NonNull
    private String bookId;

    @NonNull
    @ForeignKey(entity = Chap.class, parentColumns = "_id", childColumns = "chapId")
    private String chapId;

    private String content;

    private String trans;

    private long no;


    public Para(String id, String bookId, String chapId, String content) {
        this._id = id;
        this.bookId = bookId;
        this.chapId = chapId;
        this.content = content;
    }

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull String bookId) {
        this.bookId = bookId;
    }

    @NonNull
    public String getChapId() {
        return chapId;
    }

    public void setChapId(@NonNull String chapId) {
        this.chapId = chapId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }
}
