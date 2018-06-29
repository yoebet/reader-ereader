package wjy.yo.ereader.remotevo

import java.util.ArrayList
import java.util.Date

import wjy.yo.ereader.entity.userdata.UserWord

open class UserWordForSync internal constructor(var word: String) {

    class UserWordRemove internal constructor(word: String) : UserWordForSync(word) {
        var removeFlag = true
    }

    open class UserWordFamiliarity internal constructor(word: String, var familiarity: Int, var updatedAt: Date?) : UserWordForSync(word)

    open class UserWordUpdateAll internal constructor(word: String, familiarity: Int, updatedAt: Date?, source: TextSource) : UserWordFamiliarity(word, familiarity, updatedAt) {

        var bookId: String?

        var chapId: String?

        var paraId: String?

        init {
            this.bookId = source.bookId
            this.chapId = source.chapId
            this.paraId = source.paraId
        }
    }

    class UserWordCreate internal constructor(word: String, familiarity: Int, updatedAt: Date?, source: TextSource, var createdAt: Date?) : UserWordUpdateAll(word, familiarity, updatedAt, source)

    internal class TextSource(var bookId: String?, var chapId: String?, var paraId: String?)

    companion object {


        fun fromUserWords(userWords: List<UserWord>): List<UserWordForSync> {

            val forSyncList = ArrayList<UserWordForSync>(userWords.size)

            for (uw in userWords) {
                var changeFlag = uw.changeFlag
                if (changeFlag == null) {
                    changeFlag = UserWord.ChangeFlagCreate
                }

                val word = uw.word

                if (changeFlag == UserWord.ChangeFlagDelete) {
                    val uwr = UserWordRemove(word)
                    forSyncList.add(uwr)
                    continue
                }

                val familiarity = uw.familiarity
                val updatedAt = uw.updatedAt

                if (changeFlag == UserWord.ChangeFlagFamiliarity) {
                    val uwf = UserWordFamiliarity(word, familiarity, updatedAt)
                    forSyncList.add(uwf)
                    continue
                }

                val source = TextSource(uw.bookId, uw.chapId, uw.paraId)

                if (changeFlag == UserWord.ChangeFlagAll) {
                    val uwa = UserWordUpdateAll(word, familiarity, updatedAt, source)
                    forSyncList.add(uwa)
                    continue
                }

                if (changeFlag == UserWord.ChangeFlagCreate) {
                    val createdAt = uw.createdAt
                    val uwc = UserWordCreate(word, familiarity, updatedAt, source, createdAt)
                    forSyncList.add(uwc)
                }
            }

            return forSyncList
        }
    }
}
