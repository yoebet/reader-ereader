package wjy.yo.ereader.entityvo.anno;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;
import java.util.Map;

import wjy.yo.ereader.entity.anno.Anno;
import wjy.yo.ereader.entity.anno.AnnoFamily;
import wjy.yo.ereader.entity.anno.AnnoGroup;

public class AnnotationFamily extends AnnoFamily {

    @Relation(parentColumn = "id", entityColumn = "familyId", entity = AnnoGroup.class)
    private List<AnnotationGroup> groups;

    @Ignore
    private Map<String, Anno> annosMap;

    public List<AnnotationGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<AnnotationGroup> groups) {
        this.groups = groups;
    }

    public Map<String, Anno> getAnnosMap() {
        return annosMap;
    }

    public void setAnnosMap(Map<String, Anno> annosMap) {
        this.annosMap = annosMap;
    }

    public static String buildAnnoKey(String dataName, String dataValue) {
        return dataName + "=" + dataValue;
    }

    public Anno findAnno(String dataName, String dataValue) {
        Map<String, Anno> annosMap = getAnnosMap();
        if (annosMap == null) {
            return null;
        }
        String annoKey = buildAnnoKey(dataName, dataValue);
        return annosMap.get(annoKey);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(id).append(" ").append(name).append("\n");
        if (groups != null) {
            for (AnnotationGroup group : groups) {
                sb.append(group);
            }
        }
        return sb.toString();
    }
}
