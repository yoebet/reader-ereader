package wjy.yo.ereader.entity.dict

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "dict_word_rank", indices = arrayOf(Index(value = arrayOf("word", "name"), unique = true)))
class WordRank {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0

    @ForeignKey(entity = Dict::class, parentColumns = arrayOf("word"), childColumns = arrayOf("word"), onDelete = ForeignKey.CASCADE)
    var word: String? = null

    var name: String? = null

    var rank: Int? = null
}
