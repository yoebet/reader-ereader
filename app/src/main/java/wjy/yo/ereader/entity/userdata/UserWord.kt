package wjy.yo.ereader.entity.userdata

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

import wjy.yo.ereader.entity.UserData

@Entity(tableName = "user_word", indices = arrayOf(Index(value = arrayOf("userName", "word"), unique = true)))
class UserWord(var word: String) : UserData() {

    var familiarity = FamiliarityLowest

    var bookId: String? = null

    var chapId: String? = null

    var paraId: String? = null

    var changeFlag: String? = null

    override fun toString(): String {
        return "$word $familiarity $changeFlag"
    }

    companion object {

        const val FamiliarityLowest = 1

        const val FamiliarityHighest = 3

        const val ChangeFlagAll = "A"

        const val ChangeFlagCreate = "C"

        const val ChangeFlagDelete = "D"

        const val ChangeFlagFamiliarity = "F"
    }
}
