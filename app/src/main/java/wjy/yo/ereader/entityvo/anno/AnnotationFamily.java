package wjy.yo.ereader.entityvo.anno;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import wjy.yo.ereader.entity.anno.AnnoFamily;

public class AnnotationFamily {

    @Embedded
    private AnnoFamily family;

    @Relation(parentColumn = "_id", entityColumn = "groupId")
    private List<AnnotationGroup> groups;

    public AnnoFamily getFamily() {
        return family;
    }

    public void setFamily(AnnoFamily family) {
        this.family = family;
    }

    public List<AnnotationGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<AnnotationGroup> groups) {
        this.groups = groups;
    }
}
