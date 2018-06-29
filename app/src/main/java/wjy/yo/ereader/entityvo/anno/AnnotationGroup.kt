package wjy.yo.ereader.entityvo.anno

import android.arch.persistence.room.Relation

import wjy.yo.ereader.entity.anno.Anno
import wjy.yo.ereader.entity.anno.AnnoGroup

class AnnotationGroup : AnnoGroup() {

    @Relation(parentColumn = "id", entityColumn = "groupId")
    var annotations: List<Anno>? = null

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("\t").append(nameEn).append(" ").append(name)
                .append(" [").append(dataName).append("]\n")
        if (annotations != null) {
            for (anno in annotations!!) {
                sb.append(anno)
            }
        }
        return sb.toString()
    }
}
