package wjy.yo.ereader.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import wjy.yo.ereader.entity.userdata.User;

@Entity(tableName = "data_sync_record", indices = {@Index(value = {"category", "direction", "userName"}, unique = true)})
public class DataSyncRecord {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ForeignKey(entity = User.class, parentColumns = "name", childColumns = "userName")
    private String userName;

    private String category;

    private String direction;

    private boolean stale;

    private Integer syncPeriod;

    private String syncPeriodUnit;

    private Date lastSyncAt;

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isStale() {
        return stale;
    }

    public void setStale(boolean stale) {
        this.stale = stale;
    }

    public Integer getSyncPeriod() {
        return syncPeriod;
    }

    public void setSyncPeriod(Integer syncPeriod) {
        this.syncPeriod = syncPeriod;
    }

    public String getSyncPeriodUnit() {
        return syncPeriodUnit;
    }

    public void setSyncPeriodUnit(String syncPeriodUnit) {
        this.syncPeriodUnit = syncPeriodUnit;
    }

    public Date getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(Date lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }
}
