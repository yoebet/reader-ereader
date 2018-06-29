package wjy.yo.ereader.entityvo.anno

import android.arch.persistence.room.Relation

import wjy.yo.ereader.entity.anno.AnnoFamily
import wjy.yo.ereader.entity.anno.AnnoGroup

class AnnotationFamily : AnnoFamily() {

    @Relation(parentColumn = "id", entityColumn = "familyId", entity = AnnoGroup::class)
    var groups: List<AnnotationGroup>? = null

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("#").append(id).append(" ").append(name).append("\n")
        if (groups != null) {
            for (group in groups!!) {
                sb.append(group)
            }
        }
        return sb.toString()
    }
}
