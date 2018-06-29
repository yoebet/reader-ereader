package wjy.yo.ereader.entity

import android.arch.persistence.room.ForeignKey

import wjy.yo.ereader.entity.userdata.User

open class UserData : FetchedData("") {

    @ForeignKey(entity = User::class, parentColumns = arrayOf("name"), childColumns = arrayOf("userName"))
    var userName: String? = null

    var isLocal: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other !is UserData) {
            return false
        }
        val otherUD = other as UserData?
        return userName == otherUD!!.userName && isLocal == otherUD.isLocal
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (userName?.hashCode() ?: 0)
        result = 31 * result + isLocal.hashCode()
        return result
    }

}
