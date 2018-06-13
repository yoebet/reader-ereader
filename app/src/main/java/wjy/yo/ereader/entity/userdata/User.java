package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import java.util.Date;

import wjy.yo.ereader.entity.BaseModel;

@Entity(tableName = "user", indices = {@Index(value = "name", unique = true)})
public class User extends BaseModel {

    private String name;// user id

    private String nickName;

    private String accessToken;

    private boolean current;

    private Date lastLoginAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
