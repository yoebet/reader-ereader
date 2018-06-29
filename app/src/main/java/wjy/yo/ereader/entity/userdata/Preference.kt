package wjy.yo.ereader.entity.userdata

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

import wjy.yo.ereader.entity.UserData

@Entity(tableName = "user_preference", indices = arrayOf(Index(value = arrayOf("userName", "code"), unique = true)))
class Preference(var code: String) : UserData() {

    var value: String? = null
}
