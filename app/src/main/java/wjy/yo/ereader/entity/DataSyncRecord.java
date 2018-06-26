package wjy.yo.ereader.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wjy.yo.ereader.entity.userdata.User;

@Entity(tableName = "data_sync_record", indices = {@Index(value = {"category", "direction", "userName"}, unique = true)})
@Data
@NoArgsConstructor
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
