package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.BaseModel;

@Entity(tableName = "user_word_tag", indices = {@Index(value = {"userName", "categoryCode"}, unique = true)})
public class UserWordTag extends BaseModel {

    @ForeignKey(entity = User.class, parentColumns = "name", childColumns = "userName", onDelete = ForeignKey.CASCADE)
    private String userName;

    private String categoryCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
