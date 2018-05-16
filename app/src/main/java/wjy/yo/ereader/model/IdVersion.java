package wjy.yo.ereader.model;

public class IdVersion extends BaseModel {

    @Override
    public String toString() {
        return _id + "@" + _version;
    }
}
