package wjy.yo.ereader.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

import java.util.Date
import java.util.concurrent.TimeUnit

import wjy.yo.ereader.entity.userdata.User

@Entity(tableName = "data_sync_record", indices = arrayOf(Index(value = arrayOf("category", "direction", "userName"), unique = true)))
class DataSyncRecord : Cloneable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0

    @ForeignKey(entity = User::class, parentColumns = arrayOf("name"), childColumns = arrayOf("userName"))
    var userName: String? = null

    var category: String? = null

    var direction: String? = null

    var isStale: Boolean = false

    var syncPeriod: Int? = null

    var syncPeriodUnit: String? = null

    var lastSyncAt: Date? = null

    var dataVersion: Long = 0

    var syncPeriodTimeUnit: TimeUnit
        get() {
            when (syncPeriodUnit) {
                "I" -> return TimeUnit.MILLISECONDS
                "S" -> return TimeUnit.SECONDS
                "M" -> return TimeUnit.MINUTES
                "H" -> return TimeUnit.HOURS
                "D" -> return TimeUnit.DAYS
            }
            return TimeUnit.MINUTES
        }
        set(tu) {
            when (tu) {
                TimeUnit.MILLISECONDS -> syncPeriodUnit = "I"
                TimeUnit.SECONDS -> syncPeriodUnit = "S"
                TimeUnit.MINUTES -> syncPeriodUnit = "M"
                TimeUnit.HOURS -> syncPeriodUnit = "H"
                TimeUnit.DAYS -> syncPeriodUnit = "D"
                else -> {
                }
            }
        }

    public override fun clone(): DataSyncRecord {
        try {
            return super.clone() as DataSyncRecord
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }
        return this
    }
}
