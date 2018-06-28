package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wjy.yo.ereader.entity.UserData;

@Entity(tableName = "user_chap", indices = {@Index(value = {"userName", "chapId"}, unique = true)})
@Data
@NoArgsConstructor
public class UserChap {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ForeignKey(entity = User.class, parentColumns = "name", childColumns = "userName")
    protected String userName;

    private String bookId;

    private String chapId;
}
