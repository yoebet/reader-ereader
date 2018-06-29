package wjy.yo.ereader.entity;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;


public class FetchedData {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    protected String id;

    protected long version;

    protected Date createdAt;

    protected Date updatedAt;

    protected Date lastFetchAt;

    @NonNull
    public String getId() {
        return this.id;
    }

    public long getVersion() {
        return this.version;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public Date getLastFetchAt() {
        return this.lastFetchAt;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLastFetchAt(Date lastFetchAt) {
        this.lastFetchAt = lastFetchAt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FetchedData)) {
            return false;
        }
        FetchedData other = (FetchedData) o;
        return Objects.equals(id, other.id) && Objects.equals(version, other.version);
    }

    public String toString() {
        return getClass().getSimpleName() + "(#" + this.getId() + " v" + this.getVersion() + ")";
    }
}
