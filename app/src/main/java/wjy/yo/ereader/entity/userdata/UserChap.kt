package wjy.yo.ereader.entity.userdata

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user_chap", indices = arrayOf(Index(value = arrayOf("userName", "chapId"), unique = true)))
class UserChap {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0

    @ForeignKey(entity = User::class, parentColumns = arrayOf("name"), childColumns = arrayOf("userName"))
    var userName: String? = null

    var bookId: String? = null

    var chapId: String? = null
}
