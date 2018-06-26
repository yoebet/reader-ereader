package wjy.yo.ereader.entityvo.anno;

import android.arch.persistence.room.Relation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entity.anno.AnnoGroup;

public class AnnotationFamily extends AnnoFamily {

    @Setter
    @Getter
    @Relation(parentColumn = "id", entityColumn = "familyId", entity = AnnoGroup.class)
    private List<AnnotationGroup> groups;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(id).append(" ").append(getName()).append("\n");
        if (groups != null) {
            for (AnnotationGroup group : groups) {
                sb.append(group);
            }
        }
        return sb.toString();
    }
}
