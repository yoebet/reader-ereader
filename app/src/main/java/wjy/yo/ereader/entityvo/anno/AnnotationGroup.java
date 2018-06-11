package wjy.yo.ereader.entityvo.anno;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import wjy.yo.ereader.entity.anno.Anno;
import wjy.yo.ereader.entity.anno.AnnoGroup;

public class AnnotationGroup {

    @Embedded
    private AnnoGroup group;

    @Relation(parentColumn = "_id", entityColumn = "groupId")
    private List<Anno> annotations;

    public AnnoGroup getGroup() {
        return group;
    }

    public void setGroup(AnnoGroup group) {
        this.group = group;
    }

    public List<Anno> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Anno> annotations) {
        this.annotations = annotations;
    }
}
