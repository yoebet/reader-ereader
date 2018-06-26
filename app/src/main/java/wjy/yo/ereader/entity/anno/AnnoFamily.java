package wjy.yo.ereader.entity.anno;

import android.arch.persistence.room.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.FetchedData;

@Entity(tableName = "anno_family")
@Data
@EqualsAndHashCode(callSuper = true)
public class AnnoFamily extends FetchedData {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
