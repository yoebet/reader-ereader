package wjy.yo.ereader.entity.book

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore

import java.util.Date

import wjy.yo.ereader.entity.FetchedData
import wjy.yo.ereader.entity.Ordered
import wjy.yo.ereader.entity.userdata.UserChap


@Entity(tableName = "book_chap")
open class Chap : FetchedData(""), Ordered {

    @ForeignKey(entity = Book::class, parentColumns = arrayOf("id"), childColumns = arrayOf("bookId"))
    var bookId: String? = null

    var name: String? = null

    var zhName: String? = null

    override var no: Long = 0

    var parasLastFetchAt: Date? = null

    @Ignore
    var userChap: UserChap? = null

    override fun toString(): String {
        return "$id $name"
    }
}
