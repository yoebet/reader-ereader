package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.UserData;

@Entity(tableName = "user_book", indices = {@Index(value = {"userName", "bookId"}, unique = true)})
@Data
@EqualsAndHashCode(callSuper = true)
public class UserBook extends UserData {

    private String bookId;

    private String role;

    private boolean isAllChaps;
}
