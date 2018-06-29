package wjy.yo.ereader.entity.userdata

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

import java.util.Date

import wjy.yo.ereader.entity.FetchedData

@Entity(tableName = "user", indices = arrayOf(Index(value = ["name"], unique = true)))
class User(// user id
        var name: String) : FetchedData("") {

    var nickName: String? = null

    var accessToken: String? = null

    var isCurrent: Boolean = false

    var lastLoginAt: Date? = null

    override fun toString(): String {
        return "$name $nickName"
    }
}
