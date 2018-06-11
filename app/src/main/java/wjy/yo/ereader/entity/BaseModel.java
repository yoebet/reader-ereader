package wjy.yo.ereader.entity;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

import wjy.yo.ereader.entityvo.IdVersion;

public abstract class BaseModel {
    @PrimaryKey
    @NonNull
    protected String _id;

    protected long version;


    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "#" + _id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseModel baseModel = (BaseModel) o;
        return version == baseModel.version &&
                Objects.equals(_id, baseModel._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, version);
    }

    public boolean changed(BaseModel iv) {
        return version != iv.version ||
                !Objects.equals(_id, iv._id);
    }


    /*    static sequenceNo(_id: string, bytes: number = 3): number {
        if (!_id) {
            return parseInt('' + (1 << bytes * 8) * Math.random());
        }
        let hexChars = bytes * 2;
        return parseInt(_id.substr(_id.length - hexChars, hexChars), 16);
    }

    static timestampOfObjectId(_id: string): Date {
        if (!_id) {
            return null;
        }
        let seconds = parseInt(_id.substr(0, 8), 16);
        return new Date(seconds * 1000);
    }*/
}
