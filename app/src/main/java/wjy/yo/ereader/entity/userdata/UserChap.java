package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.UserData;

@Entity(tableName = "user_chap", indices = {@Index(value = {"userName", "chapId"}, unique = true)})
@Data
@EqualsAndHashCode(callSuper = true)
public class UserChap extends UserData {

    private String bookId;

    private String chapId;

    private Integer progress;
}
