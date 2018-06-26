package wjy.yo.ereader.entityvo.anno;

import android.arch.persistence.room.Relation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import wjy.yo.ereader.entity.anno.Anno;
import wjy.yo.ereader.entity.anno.AnnoGroup;

public class AnnotationGroup extends AnnoGroup {

    @Setter
    @Getter
    @Relation(parentColumn = "id", entityColumn = "groupId")
    private List<Anno> annotations;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append(getNameEn()).append(" ").append(getName())
                .append(" [").append(getDataName()).append("]\n");
        if (annotations != null) {
            for (Anno anno : annotations) {
                sb.append(anno);
            }
        }
        return sb.toString();
    }
}
