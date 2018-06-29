package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.UserData;

@Entity(tableName = "user_preference", indices = {@Index(value = {"userName", "code"}, unique = true)})
public class Preference extends UserData {

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
