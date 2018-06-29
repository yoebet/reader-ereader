package wjy.yo.ereader.entity.userdata

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index

import wjy.yo.ereader.entity.UserData

@Entity(tableName = "user_book", indices = arrayOf(Index(value = arrayOf("userName", "bookId"), unique = true)))
class UserBook : UserData() {

    var bookId: String? = null

    var role: String? = null

    var isAllChaps: Boolean = false

    @Ignore
    var chaps: List<UserChap>? = null
}
