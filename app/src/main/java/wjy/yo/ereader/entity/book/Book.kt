package wjy.yo.ereader.entity.book

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore

import java.util.Date

import wjy.yo.ereader.entity.FetchedData
import wjy.yo.ereader.entity.anno.AnnoFamily
import wjy.yo.ereader.entity.userdata.UserBook


@Entity(tableName = "book")
open class Book : FetchedData("") {

    var code: String? = null

    var name: String? = null

    var zhName: String? = null

    var author: String? = null

    var zhAuthor: String? = null

    var tags: String? = null

    @ForeignKey(entity = AnnoFamily::class, parentColumns = arrayOf("id"), childColumns = arrayOf("annotationFamilyId"))
    var annotationFamilyId: String? = null

    var chapsLastFetchAt: Date? = null

    @Ignore
    var userBook: UserBook? = null

    override fun toString(): String {
        return "$id $name"
    }
}
