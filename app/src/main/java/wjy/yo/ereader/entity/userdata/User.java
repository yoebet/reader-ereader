package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.FetchedData;

@Entity(tableName = "user", indices = {@Index(value = "name", unique = true)})
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends FetchedData {

    private String name;// user id

    private String nickName;

    private String accessToken;

    private boolean current;

    @EqualsAndHashCode.Exclude
    private Date lastLoginAt;

}
