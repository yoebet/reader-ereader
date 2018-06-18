package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.UserData;

@Entity(tableName = "user_word_tag", indices = {@Index(value = {"userName", "categoryCode"}, unique = true)})
public class UserWordTag extends UserData {

    private String categoryCode;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
