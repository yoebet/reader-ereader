package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

import java.util.List;

import wjy.yo.ereader.entity.UserData;

@Entity(tableName = "user_book", indices = {@Index(value = {"userName", "bookId"}, unique = true)})
public class UserBook extends UserData {

    private String bookId;

    private String role;

    private boolean isAllChaps;

    @Ignore
    private List<UserChap> chaps;

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

    public List<UserChap> getChaps() {
        return chaps;
    }

    public void setChaps(List<UserChap> chaps) {
        this.chaps = chaps;
    }
}
