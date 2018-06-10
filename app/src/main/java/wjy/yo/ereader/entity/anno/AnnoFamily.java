package wjy.yo.ereader.entity.anno;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;

import wjy.yo.ereader.entity.BaseModel;

@Entity(tableName = "anno_family")
public class AnnoFamily extends BaseModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
