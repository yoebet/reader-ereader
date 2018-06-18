package wjy.yo.ereader.entity;

import android.arch.persistence.room.ForeignKey;

import wjy.yo.ereader.entity.userdata.User;

public class UserData extends FetchedData {

    @ForeignKey(entity = User.class, parentColumns = "name", childColumns = "userName")
    protected String userName;

    protected boolean local;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

}
