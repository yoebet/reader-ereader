package wjy.yo.ereader.entityvo;

import wjy.yo.ereader.entity.BaseModel;

public class IdVersion extends BaseModel {

    @Override
    public String toString() {
        return _id + "@" + _version;
    }
}
