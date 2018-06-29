package wjy.yo.ereader.entity.dict

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index

import wjy.yo.ereader.entity.FetchedData

@Entity(tableName = "dict_word_category", indices = arrayOf(Index(value = ["code"], unique = true)))
@ForeignKey(entity = WordCategory::class, parentColumns = arrayOf("code"), childColumns = arrayOf("extendTo"), onDelete = ForeignKey.CASCADE)
class WordCategory : FetchedData("") {

    var code: String? = null

    var name: String? = null

    var dictKey: String? = null

    var dictValue: Int? = null

    var dictOperator: String? = null

    var description: String? = null

    var wordCount: Int? = null

    var extendTo: String? = null

    var extendedWordCount: Int? = null

    var no: Long = 0

    @Ignore
    var extend: WordCategory? = null
}
