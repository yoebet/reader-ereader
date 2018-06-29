package wjy.yo.ereader.entity.dict

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

import wjy.yo.ereader.entity.Ordered

@Entity(tableName = "dict_meaning_item", indices = arrayOf(Index(value = arrayOf("word", "pos"), unique = true)))
class MeaningItem : Ordered {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0

    @ForeignKey(entity = Dict::class, parentColumns = arrayOf("word"), childColumns = arrayOf("word"), onDelete = ForeignKey.CASCADE)
    var word: String? = null

    var pos: String? = null

    var exp: String? = null

    override var no: Long = 0
}
