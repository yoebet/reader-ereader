package wjy.yo.ereader.entity;

import android.arch.persistence.room.ForeignKey;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.userdata.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserData extends FetchedData {

    @ForeignKey(entity = User.class, parentColumns = "name", childColumns = "userName")
    protected String userName;

    protected boolean local;
}
