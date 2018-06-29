package wjy.yo.ereader.entity.book

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

import wjy.yo.ereader.entity.FetchedData
import wjy.yo.ereader.entity.Ordered

@Entity(tableName = "book_para")
class Para : FetchedData(""), Ordered {

    @ForeignKey(entity = Book::class, parentColumns = arrayOf("id"), childColumns = arrayOf("bookId"))
    var bookId: String? = null

    @ForeignKey(entity = Chap::class, parentColumns = arrayOf("id"), childColumns = arrayOf("chapId"))
    var chapId: String? = null

    var content: String? = null

    var trans: String? = null

    override var no: Long = 0
}
