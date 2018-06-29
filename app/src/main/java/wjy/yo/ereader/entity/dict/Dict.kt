package wjy.yo.ereader.entity.dict

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

import com.google.gson.annotations.SerializedName

import wjy.yo.ereader.entity.FetchedData

@Entity(tableName = "dict", indices = arrayOf(Index(value = ["word"], unique = true)))
open class Dict(var word: String? = null) : FetchedData("") {

    var isPhrase: Boolean = false

    var baseForm: String? = null

    @Embedded
    var phonetics: Phonetics? = null

    class Phonetics {

        @SerializedName("UK")
        var phoneticUK: String? = null

        @SerializedName("US")
        var phoneticUS: String? = null
    }
}

