package wjy.yo.ereader.entityvo.book

import android.arch.persistence.room.Relation
import java.util.Objects

import wjy.yo.ereader.entity.book.Chap
import wjy.yo.ereader.entity.book.Para

class ChapDetail : Chap() {

    @Relation(parentColumn = "id", entityColumn = "chapId")
    var paras: List<Para>? = null

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (!super.equals(other)) {
            return false
        }
        val cd = other as ChapDetail?
        return paras == cd!!.paras
    }

}
