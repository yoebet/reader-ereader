package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.BaseModel;

@Entity(tableName = "user_chap", indices = {@Index(value = {"userName", "chapId"}, unique = true)})
public class UserChap extends BaseModel {

    private String userName;

    private String bookId;

    private String chapId;

    private Integer progress;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getChapId() {
        return chapId;
    }

    public void setChapId(String chapId) {
        this.chapId = chapId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
