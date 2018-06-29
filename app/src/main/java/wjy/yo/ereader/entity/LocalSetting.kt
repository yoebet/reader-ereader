package wjy.yo.ereader.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import java.util.Date

@Entity(tableName = "setting")
class LocalSetting(@PrimaryKey var code: String) {

    var value: String? = null

    var updatedAt: Date? = null
}
