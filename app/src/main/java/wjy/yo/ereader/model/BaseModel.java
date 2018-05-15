package wjy.yo.ereader.model;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public abstract class BaseModel {
    @PrimaryKey
    @NonNull
    protected String _id;

    protected long _version;


    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public long getVersion() {
        return _version;
    }

    public void setVersion(long version) {
        this._version = version;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "#" + _id;
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
