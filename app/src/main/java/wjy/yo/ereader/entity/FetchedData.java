package wjy.yo.ereader.entity;

import java.util.Date;

public abstract class FetchedData extends IdVersion {

    protected Date lastFetchAt;

    protected Date createdAt;

    protected Date updatedAt;

    public Date getLastFetchAt() {
        return lastFetchAt;
    }

    public void setLastFetchAt(Date lastFetchAt) {
        this.lastFetchAt = lastFetchAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
