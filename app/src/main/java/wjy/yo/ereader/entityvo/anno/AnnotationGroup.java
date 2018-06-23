package wjy.yo.ereader.entityvo.anno;

import android.arch.persistence.room.Relation;

import java.util.List;

import wjy.yo.ereader.entity.anno.Anno;
import wjy.yo.ereader.entity.anno.AnnoGroup;

public class AnnotationGroup extends AnnoGroup {

    @Relation(parentColumn = "_id", entityColumn = "groupId")
    private List<Anno> annotations;

    public List<Anno> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Anno> annotations) {
        this.annotations = annotations;
    }

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
