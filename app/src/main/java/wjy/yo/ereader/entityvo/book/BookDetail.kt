package wjy.yo.ereader.entityvo.book

import android.arch.persistence.room.Relation
import java.util.Objects

import wjy.yo.ereader.entity.book.Book
import wjy.yo.ereader.entity.book.Chap

class BookDetail : Book() {

    @Relation(parentColumn = "id", entityColumn = "bookId")
    var chaps: List<Chap>? = null

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (!super.equals(other)) {
            return false
        }
        val bd = other as BookDetail?
        return chaps == bd!!.chaps
    }
}
