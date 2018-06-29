package wjy.yo.ereader.remotevo

import wjy.yo.ereader.entity.userdata.UserWord

class UserWordForAdd(var word: String?) {

    var familiarity: Int = 0

    var bookId: String? = null

    var chapId: String? = null

    var paraId: String? = null

    companion object {

        fun from(uw: UserWord): UserWordForAdd {
            val vo = UserWordForAdd(uw.word)
            vo.familiarity = uw.familiarity
            vo.bookId = uw.bookId
            vo.chapId = uw.chapId
            vo.paraId = uw.paraId
            return vo
        }
    }

}
