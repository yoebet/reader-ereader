package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.BaseModel;

@Entity(tableName = "user_book", indices = {@Index(value = {"userName", "bookId"}, unique = true)})
public class UserBook extends BaseModel {

    private String userName;

    private String bookId;

    private String role;

    private boolean isAllChaps;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAllChaps() {
        return isAllChaps;
    }

    public void setAllChaps(boolean allChaps) {
        isAllChaps = allChaps;
    }
}
