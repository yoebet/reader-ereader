package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.UserData;

@Entity(tableName = "user_preference", indices = {@Index(value = {"userName", "code"}, unique = true)})
@Data
@EqualsAndHashCode(callSuper = true)
public class Preference extends UserData {

    private String code;

    private String value;
}
