package wjy.yo.ereader.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import wjy.yo.ereader.entity.userdata.User;

@Entity(tableName = "data_sync_record", indices = {@Index(value = {"category", "direction", "userName"}, unique = true)})
public class DataSyncRecord implements Cloneable {
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

    private long dataVersion;

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

    public long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(long dataVersion) {
        this.dataVersion = dataVersion;
    }

    public TimeUnit getSyncPeriodTimeUnit() {
        switch (syncPeriodUnit) {
            case "I":
                return TimeUnit.MILLISECONDS;
            case "S":
                return TimeUnit.SECONDS;
            case "M":
                return TimeUnit.MINUTES;
            case "H":
                return TimeUnit.HOURS;
            case "D":
                return TimeUnit.DAYS;
        }
        return TimeUnit.MINUTES;
    }

    public void setSyncPeriodTimeUnit(TimeUnit tu) {
        switch (tu) {
            case MILLISECONDS:
                syncPeriodUnit = "I";
                break;
            case SECONDS:
                syncPeriodUnit = "S";
                break;
            case MINUTES:
                syncPeriodUnit = "M";
                break;
            case HOURS:
                syncPeriodUnit = "H";
                break;
            case DAYS:
                syncPeriodUnit = "D";
                break;
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
