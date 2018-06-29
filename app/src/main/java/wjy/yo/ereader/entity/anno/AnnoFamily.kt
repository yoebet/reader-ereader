package wjy.yo.ereader.entity.anno

import android.arch.persistence.room.Entity

import wjy.yo.ereader.entity.FetchedData

@Entity(tableName = "anno_family")
open class AnnoFamily : FetchedData("") {

    var name: String? = null
}
