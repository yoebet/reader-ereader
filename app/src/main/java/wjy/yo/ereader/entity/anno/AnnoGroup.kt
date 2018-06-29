package wjy.yo.ereader.entity.anno

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

import wjy.yo.ereader.entity.FetchedData
import wjy.yo.ereader.entity.Ordered

@Entity(tableName = "anno_group")
open class AnnoGroup : FetchedData(""), Ordered {

    var name: String? = null

    var nameEn: String? = null

    var dataName: String? = null

    var tagName: String? = null

    var cssClass: String? = null

    override var no: Long = 0

    @ForeignKey(entity = AnnoFamily::class, parentColumns = arrayOf("id"), childColumns = arrayOf("familyId"), onDelete = ForeignKey.CASCADE)
    var familyId: String? = null

}
