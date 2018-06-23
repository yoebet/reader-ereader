package wjy.yo.ereader.entityvo.anno;

import android.arch.persistence.room.Relation;

import java.util.List;

import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entity.anno.AnnoGroup;

public class AnnotationFamily extends AnnoFamily {

    @Relation(parentColumn = "_id", entityColumn = "familyId", entity = AnnoGroup.class)
    private List<AnnotationGroup> groups;

    public List<AnnotationGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<AnnotationGroup> groups) {
        this.groups = groups;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(_id).append(" ").append(getName()).append("\n");
        if (groups != null) {
            for (AnnotationGroup group : groups) {
                sb.append(group);
            }
        }
        return sb.toString();
    }
}
