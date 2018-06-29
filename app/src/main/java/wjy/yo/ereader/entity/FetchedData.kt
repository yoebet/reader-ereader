package wjy.yo.ereader.entity

import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.SerializedName

import java.util.Date


open class FetchedData(@PrimaryKey
                       @SerializedName("_id")
                       var id: String) {

    var version: Long = 0

    var createdAt: Date? = null

    var updatedAt: Date? = null

    var lastFetchAt: Date? = null

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other !is FetchedData) {
            return false
        }
        val otherFD = other as FetchedData?
        return id == otherFD!!.id && version == otherFD.version
    }

    override fun toString(): String {
        return javaClass.simpleName + "(#" + this.id + " v" + this.version + ")"
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + version.hashCode()
        return result
    }
}
